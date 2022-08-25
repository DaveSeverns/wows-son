package com.sevdotdev.wowson.screens.wowlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.sevdotdev.domain.model.WowMetaData
import com.sevdotdev.wowson.ui.model.UiStateContentView

@Destination(start = true)
@Composable
fun WowListScreen(
    modifier: Modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
    viewModel: WowListViewModel = hiltViewModel(),
    onWowClicked: (mediaItem: String) -> Unit = {},
) {
    val uiState by viewModel.wowListStateFlow.collectAsState()
    UiStateContentView(
        state = uiState,
        loadingContent = {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
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
                WowListItem(viewState = item, onPlayAudio = onWowClicked)
            }
        }
    }
}