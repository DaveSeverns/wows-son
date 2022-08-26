package com.sevdotdev.wowson.screens.wowdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun WowDetailsScreen(
    movieTitle: String,
    wowIndex: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "Title: $movieTitle")
        Text(text = "Index: $wowIndex")
    }

}