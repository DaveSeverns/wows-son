package com.sevdotdev.wowson.screens.wowdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.sevdotdev.wowson.ui.common.core.DefaultLoadingScreen
import com.sevdotdev.wowson.ui.model.UiState
import com.sevdotdev.wowson.ui.model.UiStateContentView

@Destination
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
    UiStateContentView(state = uiState, loadingContent = { DefaultLoadingScreen() }, errorContent ={

    } ) {
        Text(text = it.toString())
    }
}