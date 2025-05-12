package com.jetkollage.ui.util

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.write

@Composable
actual fun rememberCustomFileSaverLauncher(
    dialogSettings: FileKitDialogSettings,
    onWrite: suspend () -> ByteArray?,
    onResult: (PlatformFile?) -> Unit,
): CustomSaverLauncher {
    val launcher = rememberFileSaverLauncher(
        dialogSettings = dialogSettings,
        onResult = onResult
    )
    return object : CustomSaverLauncher {
        override fun launch(
            suggestedName: String,
            extension: String?,
            directory: PlatformFile?,
        ) = launcher.launch(
            suggestedName = suggestedName,
            extension = extension,
            directory = directory
        )
    }
}

actual suspend fun PlatformFile.writeNative(bytes: ByteArray) = write(bytes)