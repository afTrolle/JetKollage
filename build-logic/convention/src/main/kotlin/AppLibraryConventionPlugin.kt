import com.jetkollage.convention.alias
import com.jetkollage.convention.configureAndroidLibrary
import com.jetkollage.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AppLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.androidLibrary)
                alias(libs.plugins.convention.app.common)
            }

            configureAndroidLibrary()
        }
    }
}


