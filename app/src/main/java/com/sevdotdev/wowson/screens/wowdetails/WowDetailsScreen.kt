package com.sevdotdev.wowson.screens.wowdetails

import android.content.res.Configuration
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.sevdotdev.domain.model.WowMetaData
import com.sevdotdev.wowson.LocalToggler
import com.sevdotdev.wowson.ui.common.core.DefaultLoadingScreen
import com.sevdotdev.wowson.ui.common.core.FullscreenExit
import com.sevdotdev.wowson.ui.common.core.OpenFullscreen
import com.sevdotdev.wowson.ui.common.core.VectorIcons
import com.sevdotdev.wowson.ui.common.ext.conditionBased
import com.sevdotdev.wowson.ui.common.ext.countSuffix
import com.sevdotdev.wowson.ui.common.ext.screenPadding
import com.sevdotdev.wowson.ui.model.UiStateContentView

@Composable
fun WowDetailsScreen(
    movieId: String,
    modifier: Modifier = Modifier,
    viewModel: WowDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = movieId) {
        viewModel.getWowData(movieId)
    }

    val uiState by viewModel.wowDataViewState.collectAsState()
    UiStateContentView(
        state = uiState,
        loadingContent = { DefaultLoadingScreen() },
        errorContent = {

        }) {
        WowDetailsContent(viewState = it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WowDetailsContent(
    viewState: WowMetaData,
    modifier: Modifier = Modifier,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val isFullScreen =
            LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

        val toggler = LocalToggler.current
        BackHandler(enabled = isFullScreen) {
            toggler.toggle(Configuration.ORIENTATION_UNDEFINED)
        }
        VideoPlayer(
            uri = viewState.content.videoContent.highDefOptions[0].url,
            fullScreenMode = isFullScreen,
            onToggleFullScreen = {
                if (!isFullScreen) {
                    toggler.toggle(Configuration.ORIENTATION_LANDSCAPE)
                } else {
                    toggler.toggle(Configuration.ORIENTATION_UNDEFINED)
                }

            }
        )

        if (!isFullScreen) {
            Column(
                modifier = modifier
                    .screenPadding()
                    .verticalScroll(
                        rememberScrollState()
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "Total Wows in film: ${viewState.wowStats.totalWowsInMovie}")
                        Text(
                            text = "This is the ${
                                viewState.wowStats.indexOfWow.countSuffix()
                            } wow in ${viewState.movieTitle}"
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun VideoPlayer(
    uri: String,
    fullScreenMode: Boolean = false,
    onToggleFullScreen: () -> Unit,
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))

                setMediaSource(source)
                prepare()
            }
    }

    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    DisposableEffect(
        Box(
            modifier = Modifier
                .conditionBased(condition = fullScreenMode) {
                    if (it) {
                        fillMaxSize()
                    } else {
                        fillMaxWidth()
                            .aspectRatio(1.77f)
                    }
                },
        ) {
            AndroidView(factory = {
                PlayerView(context).apply {
                    hideController()
                    useController = true
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                }
            })
            IconButton(
                onClick = {
                    onToggleFullScreen()
                },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = if (fullScreenMode) VectorIcons.FullscreenExit else VectorIcons.OpenFullscreen,
                    contentDescription = "toggle full screen",
                    tint = Color.White
                )
            }
        }

    ) {
        onDispose { exoPlayer.release() }
    }
}