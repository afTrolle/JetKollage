package com.jetkollage.ui.widget.scaffold

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
 * Adaptive Toolbar inspired from NavigationSuiteScaffold
 */
@Composable
fun JetKollageToolsOverlay(
    modifier: Modifier = Modifier,
    navigationSuiteItems: ToolSuiteScope.() -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    content: @Composable () -> Unit = {},
) {
    val isCompact = windowWidthSizeClass == WindowWidthSizeClass.Compact
    val defaultItemColors = NavigationSuiteDefaults.itemColors()
    val latestContent = rememberUpdatedState(navigationSuiteItems)
    val scope by remember {
        derivedStateOf {
            ToolsCollector().also { latestContent.value.invoke(it) }.tools
        }
    }

    Box(Modifier.fillMaxSize()) {
        content()
        when (isCompact) {
            true -> ToolBar(
                modifier = modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter),
                containerColor = containerColor,
                contentColor = contentColor
            ) {
                scope.forEach {
                    NavigationBarItem(
                        modifier = it.modifier,
                        selected = it.selected,
                        onClick = it.onClick,
                        icon = it.icon,
                        enabled = it.enabled,
                        label = it.label,
                        alwaysShowLabel = it.alwaysShowLabel,
                        colors = it.colors?.navigationBarItemColors
                            ?: defaultItemColors.navigationBarItemColors,
                        interactionSource = it.interactionSource
                    )
                }
            }

            false -> Column(
                Modifier.fillMaxHeight().align(Alignment.CenterStart),
                verticalArrangement = Arrangement.Center,
            ) {
                ToolRail(
                    modifier = modifier
                        .windowInsetsPadding(
                            WindowInsets.safeGestures
                                .only(WindowInsetsSides.Horizontal)
                        )
                    ,
                    containerColor = containerColor,
                    contentColor = contentColor,
                ) {
                    scope.forEach {
                        NavigationRailItem(
                            modifier = it.modifier,
                            selected = it.selected,
                            onClick = it.onClick,
                            icon = it.icon,
                            enabled = it.enabled,
                            label = it.label,
                            alwaysShowLabel = it.alwaysShowLabel,
                            colors = it.colors?.navigationRailItemColors
                                ?: defaultItemColors.navigationRailItemColors,
                            interactionSource = it.interactionSource
                        )
                    }
                }
            }
        }
    }
}

sealed interface ToolSuiteScope {

    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        icon: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = false,
        label: @Composable() (() -> Unit)? = null,
        alwaysShowLabel: Boolean = true,
        badge: @Composable() (() -> Unit)? = null,
        colors: NavigationSuiteItemColors? = null,
        interactionSource: MutableInteractionSource? = null
    )
}

@Immutable
private data class Tool(
    val selected: Boolean,
    val onClick: () -> Unit,
    val icon: @Composable () -> Unit,
    val modifier: Modifier = Modifier,
    val enabled: Boolean = false,
    val label: @Composable() (() -> Unit)? = null,
    val alwaysShowLabel: Boolean = true,
    val colors: NavigationSuiteItemColors? = null,
    val interactionSource: MutableInteractionSource? = null
)

private class ToolsCollector() : ToolSuiteScope {

    val tools = mutableListOf<Tool>()

    override fun item(
        selected: Boolean,
        onClick: () -> Unit,
        icon: @Composable (() -> Unit),
        modifier: Modifier,
        enabled: Boolean,
        label: @Composable (() -> Unit)?,
        alwaysShowLabel: Boolean,
        badge: @Composable (() -> Unit)?,
        colors: NavigationSuiteItemColors?,
        interactionSource: MutableInteractionSource?
    ) {
        val tool = Tool(
            selected,
            onClick,
            icon,
            modifier,
            enabled,
            label,
            alwaysShowLabel,
            colors,
            interactionSource,
        )
        tools.add(tool)
    }

}

@Composable
private fun ToolBar(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    content: @Composable RowScope.() -> Unit
) = Surface(
    modifier = modifier,
    color = containerColor,
    contentColor = contentColor,
    tonalElevation = tonalElevation,
    shape = shape,
) {
    Row(
        modifier =
            Modifier
                .defaultMinSize(minHeight = 64.dp)
                .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
private fun ToolRail(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    containerColor: Color = NavigationRailDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable ColumnScope.() -> Unit
) = Surface(
    modifier = modifier,
    color = containerColor,
    shape = shape,
    contentColor = contentColor,
) {
    Column(
        Modifier
            .widthIn(min = 80.0.dp, max = 200.dp)
            .padding(vertical = 16.dp)
            .padding(vertical = navigationRailVerticalPadding, horizontal = 2.dp)
            .selectableGroup(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(navigationRailVerticalPadding)
    ) {
        content()
    }
}

private val navigationRailVerticalPadding: Dp = 8.dp
