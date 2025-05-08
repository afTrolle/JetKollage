plugins {
    alias(libs.plugins.convention.app.feature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {

            implementation(libs.jetbrains.androidx.navigation.compose)
        }
    }
}

