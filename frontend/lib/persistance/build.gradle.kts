plugins {
    alias(libs.plugins.convention.app.library)
}

kotlin {
    sourceSets {

        androidMain.dependencies {
            implementation(libs.multiplatform.settings.datastore)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.serialization)
            implementation(libs.multiplatform.settings.coroutines)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }
    }
}

