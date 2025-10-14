@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.dokka)
}

kotlin {
    explicitApi()

    androidNativeArm32()
    androidNativeArm64()
    androidNativeX64()
    androidNativeX86()

    iosArm64()
    iosSimulatorArm64()
    iosX64()

    js().nodejs()

    jvm()

    linuxArm64()
    linuxX64()

    macosArm64()
    macosX64()

    mingwX64()

    tvosArm64()
    tvosSimulatorArm64()
    tvosX64()

    wasmJs().nodejs()
    wasmWasi().nodejs()

    watchosArm32()
    watchosArm64()
    watchosDeviceArm64()
    watchosSimulatorArm64()
    watchosX64()

    applyHierarchyTemplate {
        common {
            withCompilations { true }
            group("native") {
                group("posix") { // Greater posix support than Windows offers
                    withLinux()
                    withApple()
                    withAndroidNative()
                }
                withMingw()
            }
            group("web") {
                withJs()
                withWasmJs()
            }
            group("wasm") {
                withWasmJs()
                withWasmWasi()
            }
            withJvm()
        }
    }

    sourceSets.configureEach {
        kotlin.setSrcDirs(kotlin.srcDirs.map { it.absolutePath.replace("userdeps-dummy", "user-dependencies") })
    }
}

dokka {
    moduleName = "Chekt Compilation Dependencies"
    dokkaSourceSets.configureEach {
        documentedVisibilities.set(VisibilityModifier.entries)
        includes.from(rootProject.file("modules.md"))
    }

    pluginsConfiguration.html {
        footerMessage = "Made with love by sschr15"
    }
}
