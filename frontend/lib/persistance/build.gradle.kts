plugins {
    alias(libs.plugins.convention.app.library)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        wasmJsMain.dependencies {
            implementation(libs.multiplatform.settings.no.arg)
        }
        androidMain.dependencies {
            implementation(libs.multiplatform.settings.datastore)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            api(libs.multiplatform.settings)
            api(libs.multiplatform.settings.serialization)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.jetbrains.coroutines)
            implementation(libs.jetbrains.serialization.json)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }
    }
}

