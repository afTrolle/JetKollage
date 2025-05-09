import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeUIViewController
import com.jetkollage.shared.App
import com.jetkollage.shared.initKoin
import platform.UIKit.UIViewController


@OptIn(ExperimentalComposeUiApi::class)
fun ComposeApp(): UIViewController = ComposeUIViewController(
    configure = {
        enableBackGesture = true
        parallelRendering = true
    },
    content = { App() }
)

fun initKoin() = initKoin()
