plugins {
    alias(libs.plugins.convention.app.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.frontend.lib.ui)

            implementation(libs.jetbrains.serialization.json)
            implementation(libs.androidx.annotation)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
        }
    }
}

