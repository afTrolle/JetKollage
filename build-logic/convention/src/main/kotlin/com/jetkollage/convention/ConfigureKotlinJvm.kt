package com.jetkollage.convention

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.configureKotlinJvm() {
    extensions.configure<KotlinBaseExtension> {
        configureKotlin(libs)
    }
}

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

private fun KotlinMultiplatformExtension.configureKotlinKmm(
    libs: LibrariesForLibs,
) {
    configureKotlin(libs)

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.target.get()))
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
}