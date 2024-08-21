import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.cocoapods)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.mokoResources)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.googleServices)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    android()
    project.extensions.findByType(KotlinMultiplatformExtension::class.java)?.apply {
        targets
            .filterIsInstance<KotlinNativeTarget>()
            .flatMap { it.binaries }
            .forEach { compilationUnit -> compilationUnit.linkerOpts("-lsqlite3") }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            export(libs.rinku)
            baseName = "shared"
        }
    }

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

                // navigation
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.tab)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.koin)

                // firebase
                implementation(libs.firebase.analytics)
                implementation(libs.firebase.auth)
                implementation(libs.firebase.firestore)
                implementation(libs.firebase.messaging)
                implementation(libs.firebase.storage)

                // res
                api(libs.moko.resources)
                api(libs.moko.resources.compose)

                // network
                implementation(libs.ktorfit)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.content.negotiation)
                implementation(libs.ktor.client.logging)

                api(libs.napier)

                // deeplink
                api(libs.rinku)
                implementation(libs.rinku.compose.ext)

                // kotlin ext
                implementation(libs.kotlinx.collections)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)

                // koin
                implementation(libs.koin.core)

                // kstore
                implementation(libs.kstore)
                implementation(libs.kstore.file)

                // sqlDelight
                implementation(libs.sqldelight.coroutines)
                implementation(libs.sqldelight.primitiveAdapters)

                // image loading
                implementation(libs.landscapist.coil)

                // webview
                api(libs.webview.compose)
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
                implementation(libs.sqldelight.androidDriver)
                implementation(libs.ktor.client.okhttp)
//                implementation(libs.kotlinx.coroutines.android)
            }
        }

        val iosX64Main by getting {
            resources.srcDirs("build/generated/moko/iosX64Main/src")
        }

        val iosArm64Main by getting {
            resources.srcDirs("build/generated/moko/iosArm64Main/src")
        }

        val iosSimulatorArm64Main by getting {
            resources.srcDirs("build/generated/moko/iosSimulatorArm64Main/src")
        }

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.ios)
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqldelight.nativeDriver)
            }
        }
    }

    task("testClasses")
}

android {
    sourceSets {
        getByName("main").java.srcDirs("build/generated/moko/androidMain/src")
    }

    namespace = "com.closedcircuit.closedcircuitapplication"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    with(libs.ktorfit.ksp) {
        add("kspCommonMainMetadata", this)
        add("kspAndroid", this)
        add("kspAndroidTest", this)
        add("kspIosX64", this)
        add("kspIosX64Test", this)
        add("kspIosArm64", this)
        add("kspIosArm64Test", this)
        add("kspIosSimulatorArm64", this)
        add("kspIosSimulatorArm64Test", this)
    }
//    listOf(
//        "kspCommonMainMetadata",
//        "kspAndroid",
//        "kspIosX64",
//        "kspIosArm64",
//        "kspIosSimulatorArm64"
//    ).forEach {
//        add(it, libs.kotlinInject.compiler)
//        add(it, libs.ktorfit.ksp)
//    }
}

sqldelight {
    databases {
        create("TheClosedCircuitDatabase") {
            packageName.set("com.closedcircuit.closedcircuitapplication.database")
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.closedcircuit.closedcircuitapplication.resources"
    multiplatformResourcesClassName = "SharedRes"
}

tasks.matching { it.name == "kspKotlinIosSimulatorArm64" }.configureEach {
    dependsOn(tasks.getByName("generateMRiosSimulatorArm64Main"))
}

tasks.matching { it.name == "kspKotlinIosArm64" }.configureEach {
    dependsOn(tasks.getByName("generateMRiosArm64Main"))
}