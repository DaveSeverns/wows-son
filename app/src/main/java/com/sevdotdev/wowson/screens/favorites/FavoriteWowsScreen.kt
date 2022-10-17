package com.sevdotdev.wowson.screens.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sevdotdev.domain.model.WowStats
import com.sevdotdev.wowson.R
import com.sevdotdev.wowson.navigation.ScreenKeys
import com.sevdotdev.wowson.screens.wowstats.WowStatsScreen

object FavoriteWowsScreen : Screen {

    override val key: ScreenKey
        get() = ScreenKeys.WOW_FAVORITES_KEY
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        FavoritesScreen(
            onScreenClick = {
                navigator.push(WowStatsScreen(stats = WowStats(
                    5,
                    3,
                    1010110L,
                    1010292920L
                )))
            }
        )
    }
}

@Composable
fun FavoritesScreen(
    onScreenClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()
        .clickable { onScreenClick() }) {
        Column(modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(200.dp))
            Text(text = stringResource(id = R.string.favorites_screen_msg))
        }
    }
}