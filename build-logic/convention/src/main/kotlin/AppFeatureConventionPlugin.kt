import com.jetkollage.convention.alias
import com.jetkollage.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AppFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.convention.app.library)
                alias(libs.plugins.convention.app.compose)
                alias(libs.plugins.convention.app.common)
            }
        }
    }
}



