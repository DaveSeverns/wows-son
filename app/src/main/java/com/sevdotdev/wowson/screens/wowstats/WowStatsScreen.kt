package com.sevdotdev.wowson.screens.wowstats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.stack.rememberStateStack
import com.sevdotdev.domain.model.WowStats
import com.sevdotdev.wowson.ui.common.core.icons.VectorIcons
import com.sevdotdev.wowson.ui.common.core.icons.WowIcon

data class WowStatsScreen(val stats: WowStats): AndroidScreen() {
    @Composable
    override fun Content() {
        WowStatsScreenContent(stats = stats)
    }

}

@Composable
fun WowStatsScreenContent(
    stats: WowStats,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
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