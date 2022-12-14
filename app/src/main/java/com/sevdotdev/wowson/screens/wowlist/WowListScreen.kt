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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sevdotdev.domain.model.WowMetaData
import com.sevdotdev.wowson.screens.destinations.WowDetailsScreenDestination
import com.sevdotdev.wowson.ui.common.core.DefaultLoadingScreen
import com.sevdotdev.wowson.ui.common.ext.screenPadding
import com.sevdotdev.wowson.ui.model.UiStateContentView

@RootNavGraph(start = true)
@Destination
@Composable
fun WowListScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: WowListViewModel = hiltViewModel(),
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
            navigator.navigate(WowDetailsScreenDestination(it))
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