package  com.jetkollage.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension


fun Project.configureAndroidLibrary() {
    val projectName = displayName.trim()
        .replace(":", ".")
        .removePrefix("project '")
        .removeSuffix("'")

    val namespace = "com.jetKollage$projectName"

    extensions.configure(CommonExtension::class.java) {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        defaultConfig {
            minSdk = libs.versions.android.minSdk.get().toInt()
        }

        this.namespace = namespace

        compileOptions {
            sourceCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get())
            targetCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get())
        }
    }
}

fun Project.configureAndroidCompose() {
    extensions.configure(CommonExtension::class.java) {
        buildFeatures {
            compose = true
        }
    }
}