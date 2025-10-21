package com.sschr15.chekt

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.getCompilerMessageLocation
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.konan.report
import org.jetbrains.kotlin.builtins.PrimitiveType
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.jvm.compiler.report
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression
import org.jetbrains.kotlin.ir.symbols.IrBindableSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.util.isSubtypeOf
import org.jetbrains.kotlin.ir.util.isSubtypeOfClass
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

@OptIn(UnsafeDuringIrConstructionAPI::class)
class OverflowChecker(private val context: IrPluginContext, private val config: CompilerConfiguration) : IrElementTransformerVoid() {
    private val typeSystem = IrTypeSystemContextImpl(context.irBuiltIns)
    private val sequenceClass = context.referenceClass(ClassId(FqName("kotlin.sequences"), Name.identifier("Sequence")))!!
    private val skipCheckAnnotation = FqName("com.sschr15.chekt.SkipOverflowChecks")

    private val singleTypeChecks = setOf(
        "plus", "minus", "times",
        "inc", "dec",
        "unaryMinus", "abs",
        "rem",
    )

    private val singleArgumentTypeChecks = setOf(
        "inc", "dec", "unaryMinus", "abs",
    )

    private val collectionChecks = setOf("sum", "sumOf", "sumBy").flatMap { s ->
        listOf("kotlin.collections", "kotlin.sequences").map(::FqName).map { it.child(Name.identifier(s)) }
    }

    private val inputTypes = with(context.irBuiltIns) {
        setOf(byteType, charType, shortType, intType, longType, null)
    }

    private val outputTypes = with(context.irBuiltIns) {
        setOf(byteType, shortType, intType, longType)
    }

    private val intConversions = setOf("toFloat")
    private val longConversions = setOf("toInt", "toDouble", "toFloat")
    private val absoluteValueName = Name.special("<get-absoluteValue>")

    private fun visitConversionCall(expression: IrCall): IrExpression {
        val par0 = expression.receiverAndArgs().first()
        return context.irBuiltIns
            .createIrBuilder(expression.symbol, expression.startOffset, expression.endOffset)
            .irCall(
                context.referenceFunctions(
                    CallableId(
                        FqName("com.sschr15.chekt"),
                        null,
                        expression.symbol.owner.name,
                    )
                ).single { it.owner.parameters.single().type == par0.type }
            ).apply {
                arguments[0] = par0
            }
    }

    lateinit var parent: IrDeclarationParent
    lateinit var file: IrFile

    private fun IrType.isSumCandidate() =
        isSubtypeOfClass(context.irBuiltIns.iterableClass) || isArray() || isPrimitiveArray() || isSubtypeOfClass(sequenceClass)

    private fun visitSumCall(expression: IrCall): IrExpression {
        if (expression.symbol.owner.parameters.count { it.kind == IrParameterKind.Regular } > 1) return super.visitCall(expression)

        val collection = expression.arguments.first() ?: error("Expected collection")

        if (expression.symbol.owner.parameters.size == 1) {
            val type = expression.symbol.owner.returnType
            if (type != context.irBuiltIns.intType && type != context.irBuiltIns.longType) return super.visitCall(expression)

            return context.irBuiltIns
                .createIrBuilder(expression.symbol, expression.startOffset, expression.endOffset)
                .irCall(
                    context.referenceFunctions(CallableId(FqName("com.sschr15.chekt"), null, Name.identifier("sum")))
                        .single { collection.type.isSubtypeOf(it.owner.parameters.single().type, typeSystem) }
                ).apply {
                    arguments[0] = collection
                }
        }

        // Lambda
        val lambda = expression.arguments.filterIsInstance<IrFunctionExpression>().singleOrNull() ?: error("Expected lambda")

        if (!lambda.type.isFunctionTypeOrSubtype()) error("Expected lambda to be of function type")

        val (input, output) = (lambda.type as IrSimpleType).arguments

        if (input.typeOrNull !in inputTypes) return super.visitCall(expression)
        if (output.typeOrFail !in outputTypes) return super.visitCall(expression)

        if (expression.symbol.owner.name.asString() == "sumBy") {
            config.report(CompilerMessageSeverity.WARNING, "sumBy is deprecated. Overflow checking is silently replacing with sumOf.")
        }

        val newLambda = lambda.deepCopyWithSymbols(parent)
        return context.irBuiltIns.createIrBuilder(expression.symbol, expression.startOffset, expression.endOffset)
            .irCall(context.referenceFunctions(CallableId(
                FqName("com.sschr15.chekt"),
                null,
                Name.identifier("sumOf"),
                )).filter { collection.type.isSubtypeOfClass(it.owner.parameters[0].type.classOrFail) }
                .single { (it.owner.parameters[1].type as? IrSimpleType)?.arguments?.get(1)?.typeOrFail == output.typeOrFail }
            ).apply {
                arguments[0] = collection
                arguments[1] = newLambda
            }
    }

    private fun absoluteValueCheck(expression: IrMemberAccessExpression<*>): IrExpression? {
        val symbol = expression.symbol as? IrBindableSymbol<*, *> ?: return null
        val owner = symbol.owner as? IrDeclarationWithName ?: return null
        val extension = expression.arguments.firstOrNull() ?: return null
        val primitiveType = extension.type.getPrimitiveType() ?: return null
        if (primitiveType != PrimitiveType.INT && primitiveType != PrimitiveType.LONG) return null
        if (owner.name != absoluteValueName) return null
        return context.irBuiltIns.createIrBuilder(expression.symbol, expression.startOffset, expression.endOffset)
            .irCall(context.referenceFunctions(CallableId(
                FqName("com.sschr15.chekt"),
                null,
                Name.identifier("abs"),
            )).single { it.owner.parameters.single().type == extension.type })
            .apply {
                arguments[0] = extension
            }
    }

    override fun visitFile(declaration: IrFile): IrFile {
        file = declaration
        return super.visitFile(declaration)
    }

    override fun visitDeclaration(declaration: IrDeclarationBase): IrStatement {
        if (declaration.annotations.any { it.isAnnotationWithEqualFqName(FqName("com.sschr15.chekt.ExportIr")) }) {
            config.report(CompilerMessageSeverity.WARNING, declaration.dumpKotlinLike())
            config.report(CompilerMessageSeverity.WARNING, declaration.dump())
        }

        if (declaration.annotations.any { it.isAnnotationWithEqualFqName(skipCheckAnnotation) })
            return declaration

        return super.visitDeclaration(declaration)
    }

    override fun visitExpression(expression: IrExpression): IrExpression {
        if (expression is IrMemberAccessExpression<*>) {
            val abs = absoluteValueCheck(expression)
            if (abs != null) return abs
        }
        return super.visitExpression(expression)
    }

    override fun visitFunction(declaration: IrFunction): IrStatement {
        parent = declaration
        return super.visitFunction(declaration)
    }

    override fun visitCall(expression: IrCall): IrExpression {
        try {
            if (!expression.type.isPrimitiveType()) return super.visitCall(expression)

            val par0 = expression.arguments.firstOrNull() ?: return super.visitCall(expression)
            if (par0.type.isSumCandidate()) {
                if (expression.symbol.owner.kotlinFqName in collectionChecks) {
                    return visitSumCall(expression)
                }
            }

            if (par0.type.isPrimitiveType()) {
                val primitiveType = par0.type.getPrimitiveType()!!
                if (primitiveType == PrimitiveType.INT && expression.symbol.owner.name.asString() in intConversions) {
                    return visitConversionCall(expression)
                } else if (primitiveType == PrimitiveType.LONG && expression.symbol.owner.name.asString() in longConversions) {
                    return visitConversionCall(expression)
                }
            }

            val primitiveType = expression.type.getPrimitiveType() ?: return super.visitCall(expression)
            if (primitiveType != PrimitiveType.INT && primitiveType != PrimitiveType.LONG) return super.visitCall(expression)
            if (expression.symbol.owner.name.asString() !in singleTypeChecks) return super.visitCall(expression)
            if (
                (expression.arguments.size != 2 || expression.arguments[0]?.type != expression.arguments[1]?.type) &&
                (expression.arguments.size != 1 || expression.symbol.owner.name.asString() !in singleArgumentTypeChecks)
            ) {
                config.report(
                    CompilerMessageSeverity.WARNING,
                    "Unexpected arguments for ${expression.symbol.owner.name}, skipping",
                    expression.symbol.owner.fileOrNull?.let { expression.getCompilerMessageLocation(it) }
                        ?: expression.getCompilerMessageLocation(file)
                )
                return super.visitCall(expression)
            }

            return context.irBuiltIns.createIrBuilder(expression.symbol, expression.startOffset, expression.endOffset)
                .irCall(
                    context.referenceFunctions(
                        CallableId(
                            FqName("com.sschr15.chekt"),
                            null,
                            expression.symbol.owner.name,
                        )
                    ).single { it.owner.parameters.first { par -> par.kind == IrParameterKind.Regular }.type == expression.type }
                ).apply {
                    arguments[0] = par0
                    if (expression.arguments.size == 2) arguments[1] = expression.arguments[1]
                }
        } catch (e: Exception) {
            config.report(
                CompilerMessageSeverity.ERROR,
                "Error while checking for overflow: ${e.stackTraceToString()}",
                expression.symbol.owner.fileOrNull?.let { expression.getCompilerMessageLocation(it) }
                    ?: expression.getCompilerMessageLocation(file)
            )
            return expression
        }
    }
}
