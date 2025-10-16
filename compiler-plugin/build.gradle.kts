import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.nmcp)
    alias(libs.plugins.dokka)
    `maven-publish`
    signing

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

val javadocJar by tasks.registering(Jar::class) {
    from(tasks.dokkaGenerate)
    archiveClassifier = "javadoc"
}

val allDocJar by tasks.registering(Jar::class) {
    from(rootProject.tasks.dokkaGenerate)
    archiveClassifier = "javadoc-all"
}

val maven by publishing.publications.creating(MavenPublication::class) {
    artifactId = "compiler-plugin"

    from(components["kotlin"])
    artifact(tasks.kotlinSourcesJar)
    artifact(javadocJar)
    artifact(allDocJar)

    pom {
        name = "Chekt Compiler Plugin"
        description = "Main Chekt artifact - a Kotlin compiler plugin"
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

signing {
    useInMemoryPgpKeys(System.getenv("SIGNING_KEY"), System.getenv("SIGNING_PASSWORD"))
    sign(maven)
}
