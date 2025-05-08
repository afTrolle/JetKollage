import com.jetkollage.convention.alias
import com.jetkollage.convention.configureAndroidCompose
import com.jetkollage.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AppComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.jetbrainsCompose)
                alias(libs.plugins.compose.compiler)
            }
            configureAndroidCompose()
        }
    }
}