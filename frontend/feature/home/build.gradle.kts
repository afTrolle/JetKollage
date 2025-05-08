plugins {
    alias(libs.plugins.convention.app.feature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.frontend.lib.navigation)

            implementation(compose.material3)
            implementation(compose.runtime)

            implementation(libs.jetbrains.androidx.lifecycle.viewmodel.compose)
            implementation(libs.jetbrains.androidx.lifecycle.runtime.compose)
            implementation(libs.jetbrains.androidx.navigation.compose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}

