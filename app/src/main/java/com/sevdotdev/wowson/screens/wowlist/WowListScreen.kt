package com.sevdotdev.wowson.screens.wowlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.sevdotdev.domain.model.WowMetaData
import com.sevdotdev.wowson.screens.wowdetails.WowDetailsScreen
import com.sevdotdev.wowson.ui.common.core.DefaultLoadingScreen
import com.sevdotdev.wowson.ui.common.ext.screenPadding
import com.sevdotdev.wowson.ui.model.UiStateContentView


class WowListScreen : AndroidScreen() {
    override val key: ScreenKey
        get() = "WOW_LIST"
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel<WowListViewModel>()
        WowListScreen(
            onDetailsClicked = {
                navigator.push(item = WowDetailsScreen(movieId = it))
            },
            viewModel = viewModel
        )
    }
}

@Composable
fun WowListScreen(
    onDetailsClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WowListViewModel,
) {
    val context = LocalContext.current
    val exoPlayer = ExoPlayer.Builder(context).build()
    exoPlayer.apply {
        prepare()
        playWhenReady = true
    }
    DisposableEffect(key1 = context, effect = {
        onDispose {
            exoPlayer.release()
        }
    })

    val uiState by viewModel.wowListStateFlow.collectAsState()
    UiStateContentView(
        state = uiState,
        loadingContent = {
            DefaultLoadingScreen()
        },
        errorContent = {}) { wows ->
        WowListContent(viewState = wows, onPlayAudio = {

            exoPlayer.setMediaItem(MediaItem.fromUri(it))
        }, onDetailsClicked = {
            onDetailsClicked(it)
        })
    }
}

@Composable
fun WowListContent(
    modifier: Modifier = Modifier,
    viewState: List<WowMetaData>,
    onPlayAudio: (url: String) -> Unit,
    onDetailsClicked: (id: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.screenPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = viewState,
            key = {
                it.movieTitle + it.wowStats.indexOfWow
            },
        ) { item: WowMetaData ->
            WowListItem(
                viewState = item,
                onPlayAudio = onPlayAudio,
                onDetailsClicked = onDetailsClicked,
            )
        }
    }
}