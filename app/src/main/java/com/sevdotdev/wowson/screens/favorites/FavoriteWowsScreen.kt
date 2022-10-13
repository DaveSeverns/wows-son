package com.sevdotdev.wowson.screens.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sevdotdev.wowson.R

@Composable
fun FavoritesScreen() {
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.align(Alignment.Center)) {
            Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
            Text(text = stringResource(id = R.string.favorites_screen_msg))
        }
    }
}