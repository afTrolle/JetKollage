plugins {
    `kotlin-dsl`
}

group = "com.jetkollage.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register(libs.plugins.convention.app.common, "AppCommonConventionPlugin")
        register(libs.plugins.convention.app.compose, "AppComposeConventionPlugin")
        register(libs.plugins.convention.app.feature, "AppFeatureConventionPlugin")
        register(libs.plugins.convention.app.library, "AppLibraryConventionPlugin")
    }
}

fun NamedDomainObjectContainer<PluginDeclaration>.register(
    id: Provider<PluginDependency>,
    implementationClass: String,
) {
    register(id.get().pluginId) {
        this.id = name
        this.implementationClass = implementationClass
    }
}
