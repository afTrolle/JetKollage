package com.jetkollage.ui.util

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings


@Composable
expect fun rememberCustomFileSaverLauncher(
    dialogSettings: FileKitDialogSettings = FileKitDialogSettings.createDefault(),
    onWrite: suspend () -> ByteArray?,
    onResult: (PlatformFile?) -> Unit,
): CustomSaverLauncher


interface CustomSaverLauncher {
    fun launch(suggestedName: String, extension: String? = null, directory: PlatformFile? = null)
}

expect suspend fun PlatformFile.writeNative(bytes: ByteArray)