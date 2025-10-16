package com.sschr15.chekt

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.getCompilerMessageLocation
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irIfThen
import org.jetbrains.kotlin.backend.common.lower.irNot
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactory0
import org.jetbrains.kotlin.diagnostics.Severity
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addBackingField
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.symbols.impl.IrPropertySymbolImpl
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import kotlin.reflect.full.allSupertypes

@OptIn(UnsafeDuringIrConstructionAPI::class)
class Memoizer(private val context: IrPluginContext) : IrElementTransformerVoid() {
    private val memoizedOrigin = object : IrDeclarationOrigin {
        override val isSynthetic = true
        override val name = "From Memoize annotation"
    }

    private val mapGet = context.irBuiltIns.mapClass.functions.single { it.owner.name.asString() == "get" && it.owner.parameters.size == 2 }
    private val mapPut = context.irBuiltIns.mutableMapClass.functions.single { it.owner.name.asString() == "put" && it.owner.parameters.size == 3 }

    private val mutableMapOf = context.referenceFunctions(CallableId(
        packageName = FqName("kotlin.collections"),
        className = null,
        callableName = Name.identifier("mutableMapOf")
    )).single { it.owner.parameters.none { par -> par.kind == IrParameterKind.Regular } }

    private val listOf = context.referenceFunctions(CallableId(
        packageName = FqName("kotlin.collections"),
        className = null,
        callableName = Name.identifier("listOf")
    )).single { it.owner.parameters.singleOrNull { par -> par.kind == IrParameterKind.Regular }?.isVararg == true }

    private val pair = context.referenceClass(ClassId(FqName("kotlin"), Name.identifier("Pair")))!!
    private val triple = context.referenceClass(ClassId(FqName("kotlin"), Name.identifier("Triple")))!!

    private val memoizeAnnotation = FqName("com.sschr15.chekt.Memoize")

    override fun visitFunction(declaration: IrFunction): IrStatement {
        if (declaration.annotations.none { it.isAnnotationWithEqualFqName(memoizeAnnotation) })
            return super.visitFunction(declaration)

        val memoizeMap = context.irFactory.createProperty(
            name = Name.identifier($$"$memoized-$${declaration.name}-map"),
            origin = memoizedOrigin,
            visibility = DescriptorVisibilities.PRIVATE,
            modality = Modality.FINAL,
            symbol = IrPropertySymbolImpl(),
            isVar = false,
            isConst = false,
            isLateinit = false,
            isDelegated = false,
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
        )

        var parent = declaration.parent
        while (parent !is IrDeclarationContainer) {
            if (parent !is IrDeclaration) {
                error(parent::class.allSupertypes + "\n" + parent.dump())
            }
            parent = parent.parent
        }
        parent.addChild(memoizeMap)

        val memoizeMapField = memoizeMap.addBackingField {
            type = context.irBuiltIns.mutableMapClass.typeWith(
                memoizeMapKeyType(declaration),
                declaration.returnType
            )
            isStatic = declaration.receiver == null
        }
        memoizeMapField.initializer = context.irFactory.createExpressionBody(
            expression = context.irBuiltIns.createIrBuilder(memoizeMap.symbol).run {
                irCall(mutableMapOf).apply {
                    type = memoizeMapField.type
                    typeArguments[0] = memoizeMapKeyType(declaration)
                    typeArguments[1] = declaration.returnType
                }
            },
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET
        )

        val body = declaration.body ?: run {
            context.messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "Function ${declaration.name} is annotated with @Memoize but has no body",
                declaration.getCompilerMessageLocation(declaration.file)
            )
            return super.visitFunction(declaration)
        }

        body.transformChildrenVoid(object : IrElementTransformerVoid() {
            override fun visitReturn(expression: IrReturn): IrExpression {
                return context.irBuiltIns.createIrBuilder(expression.returnTargetSymbol).irBlock {
                    val value = createTmpVariable(expression.value)
                    +irCall(mapPut).apply {
                        val mapType = context.irBuiltIns.mapClass.typeWith(declaration.returnType)
                        arguments[0] = irGetField(declaration.receiver?.let(::irGet), memoizeMapField, mapType)
                        arguments[1] = memoizeMapKeyValue(declaration.parameters)
                        arguments[2] = irGet(value)
                    }
                    +irReturn(irGet(value))
                }
            }
        })

        val statements = body.statements.toMutableList()

        val toAdd = context.irBuiltIns.createIrBuilder(declaration.symbol).irBlock {
            val check = createTmpVariable(irCall(mapGet).apply {
                val mapType = context.irBuiltIns.mapClass.typeWith(declaration.returnType)
                arguments[0] = irGetField(declaration.receiver?.let(::irGet), memoizeMapField, mapType)
                arguments[1] = memoizeMapKeyValue(declaration.parameters)
            })
            +irIfThen(
                condition = irNot(irEqualsNull(irGet(check))),
                thenPart = irReturn(irGet(check))
            )
        }
        statements.add(0, toAdd)

        declaration.body = context.irFactory.createBlockBody(body.startOffset, body.endOffset, statements)

        return declaration
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    private fun IrBuilderWithScope.memoizeMapKeyValue(params: List<IrValueParameter>): IrExpression = when (params.size) {
        0 -> irGetObject(context.irBuiltIns.unitClass)
        1 -> irGet(params.single())
        2 -> irCall(pair.constructors.single()).apply {
            arguments[0] = irGet(params[0])
            arguments[1] = irGet(params[1])
        }
        3 -> irCall(triple.constructors.single()).apply {
            arguments[0] = irGet(params[0])
            arguments[1] = irGet(params[1])
            arguments[2] = irGet(params[2])
        }
        else -> irCall(listOf).apply {
            arguments[0] = irVararg(context.irBuiltIns.anyType, params.map { irGet(it) })
        }
    }

    private fun IrPluginContext.memoizeMapKeyType(params: List<IrValueParameter>): IrType = when (params.size) {
        0 -> irBuiltIns.unitType
        1 -> params.single().type
        2 -> pair.typeWith(params.map { it.type })
        3 -> triple.typeWith(params.map { it.type })
        else -> irBuiltIns.listClass.typeWith(irBuiltIns.anyType)
    }

    private fun memoizeMapKeyType(declaration: IrFunction) = context.memoizeMapKeyType(declaration.parameters)

    private val IrFunction.receiver get() = parameters.firstOrNull {
        it.kind == IrParameterKind.DispatchReceiver || it.kind == IrParameterKind.ExtensionReceiver
    }
}
