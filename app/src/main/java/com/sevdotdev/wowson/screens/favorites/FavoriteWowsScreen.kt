package com.sevdotdev.wowson.screens.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sevdotdev.wowson.R

@Composable
fun FavoritesScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(200.dp))
            Text(text = stringResource(id = R.string.favorites_screen_msg))
        }
    }
}