import com.jetkollage.convention.alias
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.jetkollage.convention.configureKotlinKmm
import com.jetkollage.convention.libs

class AppCommonConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.kotlinMultiplatform)
            }
            configureKotlinKmm()
        }
    }
}

