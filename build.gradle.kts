@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.cocoapods) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.mokoResources) apply false
    alias(libs.plugins.buildConfig) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.ktorfit) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

subprojects {
    // Configure KtorFit
    plugins.withType<de.jensklingenberg.ktorfit.gradle.KtorfitGradleSubPlugin> {
        configure<de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration> {
            version = libs.versions.ktorfit.asProvider().get()
            logging = project.hasProperty("debugApp")
        }
    }
}


apply(from = "gradle/config/android-library.gradle")
apply(from = "gradle/config/java-library.gradle")
apply(from = "gradle/config/multiplatform-library.gradle")