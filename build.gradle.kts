plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.binary.compatibility.validator) apply false
    alias(libs.plugins.jreleaser) apply false
    alias(libs.plugins.dokka)
}

allprojects {
    group = "com.sschr15.chekt"
    version = rootProject.libs.versions.project.get()
}

dependencies {
    dokka(projects.compilerPlugin)
    dokka(projects.gradlePlugin)
    dokka(projects.userDependencies)
    dokka(projects.userdepsDummy)
}
