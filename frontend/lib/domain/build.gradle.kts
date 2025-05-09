plugins {
    alias(libs.plugins.convention.app.library)
    alias(libs.plugins.convention.app.compose)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(libs.jetbrains.serialization.json)
        }
    }
}

