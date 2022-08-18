package com.sevdotdev.wowson.ui.components

import android.app.ActionBar.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoUri: String? = null,
) {
    val context = LocalContext.current
    val exoPlayer = remember(videoUri) {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                videoUri?.let {
                    setMediaItem(MediaItem.fromUri(videoUri))
                }
                prepare()
            }
    }

    exoPlayer.playWhenReady = true
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
    AndroidView(factory = {
        PlayerControlView(context).apply {
            player = exoPlayer
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, 300)
        }
    })
    DisposableEffect(
        Unit
    )
    {
        onDispose {
            exoPlayer.release()
        }
    }

}