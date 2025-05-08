plugins {
    alias(libs.plugins.convention.app.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {

            implementation(libs.jetbrains.serialization.json)
            implementation(libs.jetbrains.androidx.navigation.compose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
        }
    }
}

