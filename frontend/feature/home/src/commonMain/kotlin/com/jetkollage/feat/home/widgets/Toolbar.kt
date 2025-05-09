package com.jetkollage.feat.home.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector
import com.jetkollage.ui.widget.scaffold.ToolSuiteScope
import jetkollage.frontend.lib.ui.generated.resources.Res
import jetkollage.frontend.lib.ui.generated.resources.tool_add_image
import jetkollage.frontend.lib.ui.generated.resources.tool_add_overlay
import jetkollage.frontend.lib.ui.generated.resources.tool_layers
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

fun ToolSuiteScope.toolbar(
    onTool: (Tool) -> Unit = {},
) = Tool.entries.forEach {
    item(
        selected = false,
        icon = {
            Icon(
                it.icon,
                contentDescription = stringResource(it.label),
            )
        },
        enabled = it.enabled,
        label = { Text(stringResource(it.label)) },
        onClick = { onTool(it) },
    )
}

enum class Tool(
    val label: StringResource,
    val icon: ImageVector,
    val enabled : Boolean = true,
) {
    AddImage(
        label = Res.string.tool_add_image,
        icon = Icons.Outlined.AddPhotoAlternate
    ),
    AddOverlay(
        label = Res.string.tool_add_overlay,
        icon = Icons.Default.Add
    ),
    Layers(
        label = Res.string.tool_layers,
        icon = Icons.Outlined.Layers,
        enabled = false
    )
}

