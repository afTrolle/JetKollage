plugins {
    alias(libs.plugins.convention.app.feature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.frontend.lib.api)
            implementation(projects.frontend.lib.navigation)
            implementation(projects.frontend.lib.persistance)
            implementation(projects.frontend.lib.ui)
            implementation(projects.frontend.lib.domain)

            implementation(compose.materialIconsExtended)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.components.resources)

            implementation(libs.jetbrains.compose.material3.window.size)
            implementation(libs.jetbrains.compose.material.icons)
            implementation(libs.jetbrains.compose.adaptive.navigation)
            implementation(libs.jetbrains.compose.adaptive.layout)
            implementation(libs.jetbrains.compose.adaptive)

            implementation(libs.jetbrains.androidx.navigation.compose)
            implementation(libs.jetbrains.androidx.lifecycle.runtime.compose)
            implementation(libs.jetbrains.androidx.lifecycle.viewmodel.compose)

            implementation(libs.filekit.dialogs.compose)
            implementation(libs.filekit.coil)
            implementation(libs.filekit.core)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}

