package com.jetkollage.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.download
import kotlinx.coroutines.launch

@Composable
actual fun rememberCustomFileSaverLauncher(
    dialogSettings: FileKitDialogSettings,
    onWrite: suspend () -> ByteArray?,
    onResult: (PlatformFile?) -> Unit,
): CustomSaverLauncher {
    val scope = rememberCoroutineScope()

    return object : CustomSaverLauncher {

        override fun launch(
            suggestedName: String,
            extension: String?,
            directory: PlatformFile?,
        ) {
            scope.launch {
                onWrite()?.let {
                    FileKit.download(
                        bytes = it,
                        fileName = "jetkollage_file.png"
                    )
                }
            }
        }
    }
}

actual suspend fun PlatformFile.writeNative(bytes: ByteArray) : Unit = error("Not supported")