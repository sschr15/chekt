package com.sschr15.chekt

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.createIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irIfThen
import org.jetbrains.kotlin.backend.common.lower.irThrow
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.jvm.compiler.report
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrDeclarationBase
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrSyntheticBody
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.starProjectedType
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

class IrDestructuringFinder(
    val context: IrPluginContext,
    val config: CompilerConfiguration,
) : IrElementTransformerVoid() {
    override fun visitDeclaration(declaration: IrDeclarationBase): IrStatement {
        if (declaration.annotations.any { it.isAnnotationWithEqualFqName(FqName("com.sschr15.chekt.SkipDestructuringChecks")) }) {
            return declaration
        }

        return super.visitDeclaration(declaration)
    }

    private val collectionsSize = context.irBuiltIns.collectionClass.getPropertyGetter("size")!!
    private val illegalArgumentException = listOf(
        context.referenceConstructors(ClassId(FqName("kotlin"), Name.identifier("IllegalArgumentException"))),
        context.referenceConstructors(ClassId(FqName("java.lang"), Name.identifier("IllegalArgumentException"))),
    ).flatten().first { ctor ->
        ctor.owner
            .parameters
            .singleOrNull()
            ?.type
            ?.classFqName
            ?.asString()
            ?.matches("""^(kotlin|java\.lang)\.String$""".toRegex()) == true
    }

    val destructuringMap = config.getMap(DestructuringFinder)

    override fun visitFunction(declaration: IrFunction): IrStatement {
        if (declaration.annotations.any { it.isAnnotationWithEqualFqName(FqName("com.sschr15.chekt.SkipDestructuringChecks")) }) {
            return declaration
        }

        val destructs = destructuringMap[declaration.getPackageFragment().packageFqName.child(declaration.name)]
            ?: return super.visitFunction(declaration)

        var currentDestruct = 0
        if (declaration.body is IrSyntheticBody) return super.visitFunction(declaration) // synthetic bodies have no statements
        val statements = declaration.body?.statements?.toMutableList() ?: return super.visitFunction(declaration)

        fun runOnStatement(stat: IrValueDeclaration): IrStatement {
            val expected = destructs[currentDestruct++]
            return context.irBuiltIns.createIrBuilder(stat.symbol, stat.startOffset, stat.endOffset).irBlock {
                +irIfThen(
                    condition = irCall(context.irBuiltIns.andandSymbol).apply {
                        arguments[0] = irIs(irGet(stat), context.irBuiltIns.collectionClass.starProjectedType)
                        arguments[1] = irNotEquals(
                            irCall(collectionsSize).apply {
                                dispatchReceiver = irGet(stat)
                            },
                            irInt(expected)
                        )
                    },
                    thenPart=irThrow(irCallConstructor(illegalArgumentException, emptyList()).apply {
                        arguments[0] = irConcat().apply {
                            arguments.addAll(listOf(
                                irString("expected to destruct exactly $expected elements, but "),
                                irCall(collectionsSize).apply {
                                    dispatchReceiver = irGet(stat)
                                },
                                irString(" were in the collection")
                            ))
                        }
                    }),
                )
            }
        }

        //TODO: handle destructuring in parameters
//        statements.addAll(
//            0,
//            declaration.valueParameters
//                .filter { it.name == SpecialNames.DESTRUCT }
//                .map { stat -> runOnStatement(stat) }
//        )

        statements.transformFlat { stat ->
            if (stat !is IrVariable) return@transformFlat null
            if (stat.name != SpecialNames.DESTRUCT) return@transformFlat null
            if (currentDestruct >= destructs.size) {
                config.report(CompilerMessageSeverity.STRONG_WARNING, "Destructuring ${stat.dumpKotlinLike()}, but no destructuring count was provided")
                return@transformFlat null
            }

            listOf(
                stat,
                runOnStatement(stat),
            )
        }
        return super.visitFunction(declaration)
    }
}
