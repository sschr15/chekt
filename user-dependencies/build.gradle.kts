@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jreleaser.model.Active

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.binary.compatibility.validator)
    alias(libs.plugins.dokka)
    alias(libs.plugins.jreleaser)
    `maven-publish`
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

    withSourcesJar()
}

dokka {
    moduleName = "Chekt User Dependencies"
    dokkaSourceSets.configureEach {
        documentedVisibilities(VisibilityModifier.Public)
        suppressedFiles.from(
            file("src/commonMain/kotlin/com/sschr15/utils/chekt/CollectionOverflowChecks.kt"),
            file("src/commonMain/kotlin/com/sschr15/utils/chekt/IntLongOverflowChecks.kt"),
            file("src/commonMain/kotlin/com/sschr15/utils/chekt/Platform.common.kt"),
        )
        includes.from(rootProject.file("modules.md"))
    }

    pluginsConfiguration.html {
        footerMessage = "Made with love by sschr15"
    }
}

jreleaser {
    signing {
        active = Active.ALWAYS
        armored = true
    }
    deploy {
        maven {
            mavenCentral {
                val sonatype by registering {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}

publishing {
    publications {
        val maven by registering(MavenPublication::class) {
            artifactId = "user-dependencies"

            from(components["kotlin"])

            pom {
                name = "Chekt User Dependencies"
                description = "Compile-only dependencies for Chekt"
                url = "https://github.com/sschr15/chekt"

                licenses {
                    license {
                        name = "MIT"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }

                developers {
                    developer {
                        name = "sschr15"
                        email = "me@sschr15.com"
                        url = "https://github.com/sschr15"
                        timezone = "America/Chicago"
                    }
                }

                scm {
                    connection = this@pom.url.get().replace("https", "scm:git:git")
                    developerConnection = connection.get().replace("git://", "ssh://")
                    url = this@pom.url
                }
            }
        }
    }

    repositories {
        mavenLocal()
    }
}
