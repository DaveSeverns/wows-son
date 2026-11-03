package com.sevdotdev.wowson.screens.wowstats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sevdotdev.wowson.ui.common.core.icons.VectorIcons
import com.sevdotdev.wowson.ui.common.core.icons.WowIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WowStatsScreen(
    stats: WowStatsArgs,
    modifier: Modifier = Modifier,
    onDismissAction: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissAction,
        sheetState = rememberModalBottomSheetState(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface),
        ) {
            items((1..stats.totalWowsInMovie).toList()) { item ->
                Icon(
                    imageVector = VectorIcons.WowIcon,
                    contentDescription = null,
                    tint = if (item == stats.indexOfWow) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}
