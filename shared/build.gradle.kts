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

//        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
//        extraSpecAttributes["exclude_files"] = "['src/commonMain/resources/MR/**']"
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

                // logger
//                implementation(libs.libres)

                // navigation
                implementation(libs.voyager.navigator)

                api(libs.moko.resources)
                api(libs.moko.resources.compose)

//                implementation(libs.napier)
//                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.insetsx)
//                implementation(libs.ktor.core)
//                implementation(libs.kotlinx.serialization.json)
//                implementation(libs.kotlinx.datetime)
//                implementation(libs.koin.core)
//                implementation(libs.kstore)
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
        }
    }
}

android {
    namespace = "com.closedcircuit.closedcircuitapplication"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.closedcircuit.closedcircuitapplication.resources"
    multiplatformResourcesClassName = "SharedRes"
}