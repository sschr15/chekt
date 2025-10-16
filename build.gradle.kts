plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.binary.compatibility.validator) apply false
    alias(libs.plugins.nmcp) apply false
    alias(libs.plugins.nmcp.aggregation)
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

    nmcpAggregation(projects.compilerPlugin)
    nmcpAggregation(projects.userDependencies)
}

nmcpAggregation {
    centralPortal { 
        username = System.getenv("SONATYPE_USERNAME")
        password = System.getenv("SONATYPE_PASSWORD")
    }
}
