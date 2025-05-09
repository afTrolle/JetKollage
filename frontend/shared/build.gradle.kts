import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.convention.app.compose)
}

android {
    namespace = "com.jetkollage"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get())
    }
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.target.get()))
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
            binaryOption("bundleId", "com.jetkollage")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.frontend.feature.about)
            implementation(projects.frontend.feature.canvas)
            implementation(projects.frontend.feature.home)
            implementation(projects.frontend.lib.api)
            implementation(projects.frontend.lib.navigation)
            implementation(projects.frontend.lib.ui)
            implementation(projects.frontend.lib.persistance)

            implementation(compose.material3)
            implementation(compose.runtime)

            implementation(libs.jetbrains.androidx.navigation.compose)
            implementation(libs.jetbrains.androidx.lifecycle.runtime.compose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)

            implementation(libs.jetbrains.serialization.json)
        }
    }
}

