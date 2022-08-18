package com.sevdotdev.wowson

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.sevdotdev.wowson.screens.WowListScreen
import com.sevdotdev.wowson.ui.theme.WowSonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var exoPlayer: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exoPlayer = ExoPlayer.Builder(this).build()
        setContent {
            WowSonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {

                        //Greeting("Android")
                        WowListScreen(
                            onWowClicked = { audio ->
                                val mediaItem = MediaItem.fromUri(audio)
                                exoPlayer.setMediaItem(mediaItem)
                                exoPlayer.apply {
                                    prepare()
                                    playWhenReady = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onStop() {
        exoPlayer.release()
        super.onStop()
    }
}

@Composable
fun Greeting(name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
    ) {
        Image(painter = painterResource(id = R.drawable.icon_wow_son), contentDescription = "Logo")
        Text(text = "$name, you're crazier than a road lizard!")
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    WowSonTheme {
        Greeting("Android")
    }
}