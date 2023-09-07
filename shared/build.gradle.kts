@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.cocoapods)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.libres)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.mokoResources)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.ksp)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        name = "shared"
        summary = "Some description for the Shared Module"
        homepage = "home"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
//                implementation(libs.composeImageLoader)

                // navigation
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.tab)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.koin)

                // res
                api(libs.moko.resources)
                api(libs.moko.resources.compose)

                // network
                implementation(libs.ktorfit)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.json)
                implementation(libs.ktor.content.negotiation)
                implementation(libs.ktor.client.logging)

                api(libs.napier)

                implementation(libs.kotlinx.coroutines.core)
//                implementation(libs.ktor.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.koin.core)
                implementation(libs.kstore)
                implementation(libs.kstore.file)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activityCompose)
                implementation(libs.compose.uitooling)
                implementation(libs.napier)
                implementation(libs.koin.compose)
                implementation(libs.kstore.file)
//                implementation(libs.kotlinx.coroutines.android)
//                implementation(libs.ktor.client.okhttp)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.ios)
            }
        }
    }
}

android {
    namespace = "com.closedcircuit.closedcircuitapplication"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    listOf(
        "kspCommonMainMetadata",
        "kspAndroid",
        "kspIosX64",
        "kspIosArm64",
        "kspIosSimulatorArm64"
    ).forEach {
        add(it, libs.kotlinInject.compiler)
        add(it, libs.ktorfit.ksp)
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.closedcircuit.closedcircuitapplication.resources"
    multiplatformResourcesClassName = "SharedRes"
}

tasks.matching { it.name == "kspKotlinIosSimulatorArm64" }.configureEach {
    dependsOn(tasks.getByName("generateMRiosSimulatorArm64Main"))
}

//tasks.matching { it.name == "syncPodComposeResourcesForIos" }.configureEach {
//    dependsOn(tasks.getByName("generateMRiosSimulatorArm64Main"))
//}