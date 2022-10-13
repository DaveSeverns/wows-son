package com.sevdotdev.wowson.screens.wowstats

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sevdotdev.domain.model.WowStats
import com.sevdotdev.wowson.ui.common.core.icons.VectorIcons
import com.sevdotdev.wowson.ui.common.core.icons.WowIcon

@Composable
fun WowStatsScreen(
    stats: WowStats,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        items((1..stats.indexOfWow).toList()) { item ->
            Icon(
                imageVector = VectorIcons.WowIcon,
                contentDescription = null,
                tint = if (item == stats.indexOfWow) Color.Black else Color.Yellow,
            )
        }
    }
}