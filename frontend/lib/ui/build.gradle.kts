plugins {
    alias(libs.plugins.convention.app.library)
    alias(libs.plugins.convention.app.compose)
}

compose.resources {
    publicResClass = true
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.components.resources)

            implementation(libs.jetbrains.compose.material3.window.size)
            implementation(libs.jetbrains.compose.material.icons)
            implementation(libs.jetbrains.compose.adaptive.navigation)
            implementation(libs.jetbrains.compose.adaptive.layout)
            implementation(libs.jetbrains.compose.adaptive)

            implementation(libs.coil.core)
            api(libs.coil.compose)
            implementation(libs.coil.ktor)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

            implementation(libs.filekit.dialogs)
            implementation(libs.filekit.dialogs.compose)
            implementation(libs.filekit.core)
            implementation(libs.filekit.coil)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
