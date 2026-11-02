package com.sevdotdev.wowson.ui.common.core.icons.previews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sevdotdev.wowson.ui.common.core.icons.FullscreenExit
import com.sevdotdev.wowson.ui.common.core.icons.OpenFullscreen
import com.sevdotdev.wowson.ui.common.core.icons.VectorIcons

@Composable
fun IconContainer(
    vectorIcon: ImageVector,
    size: Dp = 100.dp,
) {
    Box(modifier = Modifier.background(Color.White)) {
        Image(
            imageVector = vectorIcon,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(size),
        )
    }
}

@Preview
@Composable
fun IconsPreview() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
        IconContainer(vectorIcon = VectorIcons.FullscreenExit)
        IconContainer(vectorIcon = VectorIcons.OpenFullscreen)
    }
}