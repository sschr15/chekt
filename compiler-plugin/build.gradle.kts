import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.publish.on.central)
    alias(libs.plugins.dokka)
    `maven-publish`
    
    kotlin("plugin.power-assert") version "2.2.20"
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src"))
        resources.setSrcDirs(listOf("resources"))
    }
}
dependencies {
    compileOnly(kotlin("compiler"))
}

powerAssert { 
    functions = listOf("kotlin.assert", "kotlin.require", "kotlin.check")
    includedSourceSets = listOf("main")
}

buildConfig {
    useKotlinOutput {
        internalVisibility = true
    }

    packageName(group.toString())
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${rootProject.group}\"")
}

kotlin {
    compilerOptions {
        optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
        optIn.add("org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI")
    }
}

dokka {
    moduleName = "Chekt Compiler Plugin"
    dokkaSourceSets.configureEach {
        documentedVisibilities.set(VisibilityModifier.entries)
        includes.from(rootProject.file("modules.md"))
    }

    pluginsConfiguration.html {
        footerMessage = "Made with love by sschr15"
    }
}

val allDocJar by tasks.registering(Jar::class) {
    from(rootProject.tasks.dokkaGenerate)
    archiveClassifier = "javadoc-all"
}

publishOnCentral {
    repoOwner = "sschr15"
    projectDescription = "Main Chekt artifact - a Kotlin compiler plugin"
    licenseName = "MIT"
    licenseUrl = "https://opensource.org/license/MIT"
}

publishing {
    publications {
        withType<MavenPublication> {
            artifact(allDocJar)

            pom {
                developers {
                    developer {
                        name = "sschr15"
                        email = "me@sschr15.com"
                        url = "https://github.com/sschr15"
                        timezone = "America/Chicago"
                    }
                }
            }
        }
    }
}
