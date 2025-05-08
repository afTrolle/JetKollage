plugins {
    alias(libs.plugins.convention.app.library)
    alias(libs.plugins.convention.app.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.components.resources)
        }
    }
}
