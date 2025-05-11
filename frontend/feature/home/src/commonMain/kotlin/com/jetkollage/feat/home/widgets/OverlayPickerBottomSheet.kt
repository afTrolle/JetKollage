package com.jetkollage.feat.home.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jetkollage.domain.overlay.OverlayCategory
import com.jetkollage.domain.overlay.OverlayItem
import jetkollage.frontend.lib.ui.generated.resources.Res
import jetkollage.frontend.lib.ui.generated.resources.bottom_sheet_overlay_exit_desc
import jetkollage.frontend.lib.ui.generated.resources.bottom_sheet_overlay_headline
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlayPickerBottomSheet(
    categories: List<OverlayCategory>,
    onOverlaySelected: (OverlayItem) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    val sheetState: SheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        OverlayHeadLine(onDismissRequest = onDismissRequest)
        HorizontalDivider()

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(110.dp),
            contentPadding = PaddingValues(32.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            categories.forEach { category ->
                item(
                    key = category.name,
                    span = StaggeredGridItemSpan.FullLine,
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.displayMedium,
                    )
                }

                items(category.items, key = { it.id }) { categoryItem ->
                    val placeholder = rememberVectorPainter(Icons.Default.Image)
                    AsyncImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = Color.White, bounded = true),
                                role = Role.Image
                            ) {
                                onOverlaySelected(categoryItem)
                            }
                            .padding(8.dp)
                            .fillMaxWidth()
                            .heightIn(max = 160.dp),
                        placeholder = placeholder,
                        model = categoryItem.url,
                        contentDescription = categoryItem.name,
                    )
                }
            }
        }
    }
}

@Composable
private fun OverlayHeadLine(
    title: StringResource = Res.string.bottom_sheet_overlay_headline,
    dismissContentDescription: StringResource = Res.string.bottom_sheet_overlay_exit_desc,
    onDismissRequest: () -> Unit = {},
) = Box(
    modifier = Modifier
        .padding(bottom = 16.dp)
        .padding(horizontal = 8.dp)
        .fillMaxWidth(),
    contentAlignment = Alignment.Center
) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.headlineMedium,
    )
    IconButton(
        modifier = Modifier.align(Alignment.CenterEnd),
        onClick = onDismissRequest,
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(dismissContentDescription),
        )
    }
}