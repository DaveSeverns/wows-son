package com.sevdotdev.wowson.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sevdotdev.domain.model.WowMetaData
import com.sevdotdev.wowson.ui.components.VideoPlayer

@Composable
fun WowListScreen(
    modifier: Modifier = Modifier.padding(8.dp),
    viewModel: WowListViewModel = hiltViewModel(),
    onWowClicked: (mediaItem: String) -> Unit = {},
) {
    val wowList by viewModel.wowListStateFlow.collectAsState()
    var videoUri by remember {
        mutableStateOf("")
    }

    VideoPlayer(videoUri = videoUri, modifier = Modifier.fillMaxWidth())

    wowList?.let { wows ->
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                wows,
                key = {
                    it.movieTitle + it.wowStats.indexOfWow
                },
            ) { item: WowMetaData ->
                WowItem(viewState = item, onItemClicked = onWowClicked,
                    onWowClicked = {
                        videoUri = it
                    })
            }
        }
    }
}

@Composable
fun WowItem(
    modifier: Modifier = Modifier,
    viewState: WowMetaData,
    onItemClicked: (audio: String) -> Unit,
    onWowClicked: (mediaItem: String) -> Unit = {},

    ) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .aspectRatio(1f)
    ) {
        val painter = ImageRequest.Builder(LocalContext.current)
            .data(viewState.posterUrl)
            .crossfade(2000)
            .build()
        AsyncImage(
            model = painter,
            contentDescription = viewState.movieTitle,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
                .clickable { onWowClicked(
                    viewState.content.videoContent.highDefOptions[0].url
                ) },
        )
        IconButton(
            onClick = { onItemClicked(viewState.content.audioUrl) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 16.dp)
                .clip(CircleShape),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent.copy(
                    alpha = .5f
                ),
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        ) {
            Icon(Icons.Default.PlayArrow, "Play audio clip")
        }
    }
}