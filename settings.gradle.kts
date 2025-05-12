enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

rootProject.name = "JetKollage"

// App
include(":app:android")
include(":app:web")
// Shared
include(":frontend:shared")
// Features
include(":frontend:feature:about")
include(":frontend:feature:canvas")
include(":frontend:feature:home")
// Libs
include(":frontend:lib:api")
include(":frontend:lib:common")
include(":frontend:lib:domain")
include(":frontend:lib:navigation")
include(":frontend:lib:persistance")
include(":frontend:lib:ui")
