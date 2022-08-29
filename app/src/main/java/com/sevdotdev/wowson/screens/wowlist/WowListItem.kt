package com.sevdotdev.wowson.screens.wowlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sevdotdev.domain.model.VideoContent
import com.sevdotdev.domain.model.WOW_GOD
import com.sevdotdev.domain.model.WowContent
import com.sevdotdev.domain.model.WowMetaData
import com.sevdotdev.domain.model.WowStats
import com.sevdotdev.wowson.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WowListItem(
    modifier: Modifier = Modifier,
    viewState: WowMetaData,
    onPlayAudio: (audio: String) -> Unit,
    expandedState: Boolean = false,
    onDetailsClicked: (id: String) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(expandedState)
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
            },
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        WowImageWithAudio(
            imageUrl = viewState.posterUrl,
            audioUrl = viewState.content.audioUrl,
            onPlayAudio = onPlayAudio
        )
        val density = LocalDensity.current
        AnimatedVisibility(
            visible = expanded,
            enter = slideInVertically {
                // Slide in from 40 dp from the top.
                with(density) { -40.dp.roundToPx() }
            } + expandVertically(
                // Expand from the top.
                expandFrom = Alignment.Top
            ) + fadeIn(
                // Fade in with the initial alpha of 0.3f.
                initialAlpha = 0.3f
            ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            WowDetails(
                fullLine = viewState.fullLine,
                characterName = viewState.characterName,
                filmTitle = viewState.movieTitle,
                wowId = viewState.id,
                releaseDate = viewState.releaseDate,
                onDetailsClicked = onDetailsClicked
            )
        }
    }
}

@Composable
private fun WowImageWithAudio(
    modifier: Modifier = Modifier,
    imageUrl: String,
    audioUrl: String,
    onPlayAudio: (audio: String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp))
            .aspectRatio(1f),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
                .placeholder(R.drawable.icon_wow_son).crossfade(true).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth(),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = .65f))
        ) {
            IconButton(
                modifier = Modifier.size(80.dp),
                onClick = { onPlayAudio(audioUrl) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Rounded.PlayArrow, "Play audio clip")
            }
        }
    }
}

@Composable
private fun WowDetails(
    fullLine: String,
    characterName: String,
    filmTitle: String,
    releaseDate: String,
    wowId: String,
    modifier: Modifier = Modifier,
    onDetailsClicked: (String) -> Unit,
) {
    Row(
        modifier = Modifier.clickable { onDetailsClicked(wowId) }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(3f)
                .align(Alignment.Top)
        ) {
            val nameStyle = MaterialTheme.typography.labelSmall
            Text(
                text = characterName,
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = "\"$fullLine\"",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Thin,
                fontSize = 20.sp
            )
        }
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(2f)
                .align(Alignment.Bottom),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = filmTitle,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Black
            )
            Text(
                text = releaseDate,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview
@Composable
private fun PreviewWowListItem() = Surface {
    WowListItem(
        viewState = WowMetaData(
            id = "id",
            movieTitle = "Cars 2",
            characterName = "Lightning McQueen",
            actor = WOW_GOD,
            directorName = "Ted Lasso",
            releaseDate = "6/3/2012",
            movieDuration = "01:45:22",
            releaseYear = "2012",
            fullLine = "Wow.",
            posterUrl = "",
            content = WowContent(
                audioUrl = "", videoContent = VideoContent(
                    highDefOptions = listOf(), standardDefOptions = listOf()
                )
            ),
            wowStats = WowStats(
                totalWowsInMovie = 0, indexOfWow = 0, timeOfWow = 0, durationOfFilm = 0
            )

        ),
        onPlayAudio = {},
        expandedState = true,
        onDetailsClicked = { _ ->
        }
    )
}