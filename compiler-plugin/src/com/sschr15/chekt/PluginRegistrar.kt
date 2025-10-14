package com.sschr15.chekt

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

class PluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2 = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
//        error("PAIN")

        FirExtensionRegistrarAdapter.registerExtension(FirRegistrar(configuration))

        IrGenerationExtension.registerExtension(object : IrGenerationExtension {
            override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
                moduleFragment.transform(Memoizer(pluginContext), null)
                moduleFragment.transform(OverflowChecker(pluginContext, configuration), null)
                moduleFragment.transform(IrDestructuringFinder(pluginContext, configuration), null)
            }
        })
    }
}
