package com.sevdotdev.wowson.screens.wowmovies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import com.sevdotdev.wowson.R
import com.sevdotdev.wowson.navigation.ScreenKeys
import com.sevdotdev.wowson.screens.favorites.FavoriteWowsScreen


object WowMoviesScreen: Screen {

    override val key: ScreenKey
        get() = ScreenKeys.WOW_MOVIES_KEY

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        WowMoviesScreen(onScreenClick = {
            navigator.push(FavoriteWowsScreen)
        })
    }
}

@Composable
fun WowMoviesScreen(
    onScreenClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()
        .clickable {
            onScreenClick()
        }) {
        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Filled.Settings,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(200.dp))
            Text(text = stringResource(id = R.string.movies_screen_msg))
        }
    }
}