package com.jetkollage.feat.home.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jetkollage.api.model.OverlayItem
import com.jetkollage.feat.home.OverlayCategory
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
            contentPadding = PaddingValues(16.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            categories.forEach { category ->
                item(
                    key = category.id,
                    span = StaggeredGridItemSpan.FullLine,
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.displayMedium,
                    )
                }

                items(category.items, key = {
                    "${category.id}-${it.id}-${it.name}"
                }) { categoryItem ->
                    Column {
                        Text(text = categoryItem.name)
                        // TODO Draw Image

                        /*  AsyncImage(
                              model = it.url,
                              contentDescription = it.name,
                              //imageLoader= ImageLoader.Builder(LocalContext.current)
                          )*/
                    }
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