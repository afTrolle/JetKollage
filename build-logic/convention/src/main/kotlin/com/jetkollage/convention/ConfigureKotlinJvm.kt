package com.jetkollage.convention

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

private fun KotlinBaseExtension.configureKotlin(libs: LibrariesForLibs) {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())
    sourceSets.all {
        languageSettings {
            optIn("kotlin.uuid.ExperimentalUuidApi")
            optIn("kotlinx.serialization.ExperimentalSerializationApi")
        }
    }
}

fun Project.configureKotlinKmm() {
    extensions.configure<KotlinMultiplatformExtension> {
        configureKotlinKmm(libs)
    }
}

@OptIn(ExperimentalWasmDsl::class)
private fun KotlinMultiplatformExtension.configureKotlinKmm(
    libs: LibrariesForLibs,
) {
    configureKotlin(libs)

    wasmJs {
        browser {

        }
        binaries.executable()
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.target.get()))
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
}