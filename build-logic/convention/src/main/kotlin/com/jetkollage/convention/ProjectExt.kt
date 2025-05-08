package  com.jetkollage.convention

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.the

val Project.libs: LibrariesForLibs get() = the<LibrariesForLibs>()

fun PluginManager.alias(libs: org.gradle.api.provider.Provider<org.gradle.plugin.use.PluginDependency>) {
    apply(libs.get().pluginId)
}
