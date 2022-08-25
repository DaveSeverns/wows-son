package com.sevdotdev.wowson

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.sevdotdev.wowson.screens.wowlist.WowListScreen
import com.sevdotdev.wowson.ui.theme.WowSonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.apply {
            prepare()
            playWhenReady = true
        }
        setContent {
            val context = LocalContext.current
            DisposableEffect(key1 = context, effect = {
                onDispose {
                    exoPlayer.release()
                }
            })
            WowSonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        WowListScreen(
                            onWowClicked = { audio ->
                                val mediaItem = MediaItem.fromUri(audio)
                                exoPlayer.setMediaItem(mediaItem)
                            }
                        )
                    }
                }
            }
        }
    }
}
