package com.sevdotdev.wowson.screens.wowlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.sevdotdev.wowson.ui.model.UiStateContentView

@RootNavGraph(start = true)
@Destination
@Composable
fun WowListScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
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
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = wows,
                key = {
                    it.movieTitle + it.wowStats.indexOfWow
                },
            ) { item: WowMetaData ->
                WowListItem(
                    viewState = item,
                    onPlayAudio = {
                        exoPlayer.setMediaItem(MediaItem.fromUri(it))
                    },
                    onDetailsClicked = { id ->
                        navigator.navigate(WowDetailsScreenDestination(
                            movieId = id
                        ))
                    },
                )
            }
        }
    }
}