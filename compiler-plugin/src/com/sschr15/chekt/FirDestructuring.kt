package com.sschr15.chekt

import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.declarations.FirProperty
import org.jetbrains.kotlin.fir.expressions.FirComponentCall
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar
import org.jetbrains.kotlin.fir.extensions.FirStatusTransformerExtension
import org.jetbrains.kotlin.fir.packageFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.SpecialNames

class DestructuringFinder(session: FirSession, val config: CompilerConfiguration) : FirStatusTransformerExtension(session) {
    override fun needTransformStatus(declaration: FirDeclaration): Boolean {
        if (declaration is FirFunction) {
            val body = declaration.body ?: return false
            var currentDestruct: FirProperty? = null
            var currentDestructCount = 0
            val destructs = mutableListOf<Int>()

            //TODO: handle destructuring in parameters
//            val destructedParams = mutableListOf<FirValueParameter>()
//
//            for (parameter in declaration.valueParameters) {
//                if (parameter.name == SpecialNames.DESTRUCT) {
//                    destructedParams.add(parameter)
//                }
//            }

            for (statement in body.statements) {
                if (statement !is FirProperty) continue

                if (statement.name == SpecialNames.DESTRUCT) {
                    if (currentDestruct != null) {
                        destructs.add(currentDestructCount)
                    }
                    currentDestruct = statement
                    currentDestructCount = 0
                    continue
                }

                if (currentDestruct == null) continue

                val initializer = statement.initializer
                if (initializer !is FirComponentCall) continue

                currentDestructCount++
            }

            if (currentDestruct != null) {
                destructs.add(currentDestructCount)
            }

            config.put(DestructuringFinder, declaration.symbol.packageFqName().child(declaration.symbol.name), destructs)
        }

        return true
    }

    companion object : CompilerConfigurationKey<Map<FqName, List<Int>>>("unused-destructuring-vars")
}

class FirRegistrar(val config: CompilerConfiguration) : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +{ session: FirSession -> DestructuringFinder(session, config) }
    } 
}
