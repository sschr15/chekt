import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publishing)
    `java-gradle-plugin`
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src"))
        resources.setSrcDirs(listOf("resources"))
    }
    test {
        java.setSrcDirs(listOf("test"))
        resources.setSrcDirs(listOf("testResources"))
    }
}

dependencies {
    implementation(kotlin("gradle-plugin-api"))

    testImplementation(kotlin("test-junit5"))
}

buildConfig {
    packageName(project.group.toString())

    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${rootProject.group}\"")

    buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${projects.compilerPlugin.group}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"${projects.compilerPlugin.name}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${projects.compilerPlugin.version}\"")

    buildConfigField(
        type = "String",
        name = "ANNOTATIONS_LIBRARY_COORDINATES",
        expression = "\"${projects.userDependencies.group}:${projects.userDependencies.name}:${projects.userDependencies.version}\""
    )
}

gradlePlugin {
    website = "https://github.com/sschr15/Chekt"
    vcsUrl = "https://github.com/sschr15/Chekt.git"

    plugins {
        create("ChektPlugin") {
            id = rootProject.group.toString()
            displayName = "ChektPlugin"
            description = "Applies the Chekt compiler plugin to Kotlin, providing integer overflow and destructuring checks and a memoization annotation"
            implementationClass = "com.sschr15.chekt.gradle.ChektGradlePlugin"
            tags = listOf(
                "kotlin", "compiler-plugin"
            )
        }
    }
}

dokka {
    moduleName = "Chekt Gradle Plugin"
    dokkaSourceSets.configureEach {
        documentedVisibilities.set(VisibilityModifier.entries)
        includes.from(rootProject.file("modules.md"))
    }

    pluginsConfiguration.html {
        footerMessage = "Made with love by sschr15"
    }
}

tasks {
    val javadocJar by registering(Jar::class) {
        from(dokkaGenerate)
        archiveClassifier = "javadoc"
    }
}
