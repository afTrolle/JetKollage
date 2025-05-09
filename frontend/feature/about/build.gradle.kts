plugins {
    alias(libs.plugins.convention.app.feature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.frontend.lib.navigation)

            implementation(libs.jetbrains.androidx.navigation.compose)
        }
    }
}

