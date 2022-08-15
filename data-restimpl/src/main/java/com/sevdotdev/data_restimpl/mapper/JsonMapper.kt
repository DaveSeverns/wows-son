package com.sevdotdev.data_restimpl.mapper

import com.sevdotdev.data_restimpl.model.Video
import com.sevdotdev.data_restimpl.model.WowJsonItem
import com.sevdotdev.domain.model.Resolution
import com.sevdotdev.domain.model.VideoContent
import com.sevdotdev.domain.model.VideoOption
import com.sevdotdev.domain.model.WowContent
import com.sevdotdev.domain.model.WowMetaData
import com.sevdotdev.domain.model.WowStats
import com.sevdotdev.domain.utils.RunTimeFormatter
import java.time.format.DateTimeFormatter

internal fun List<WowJsonItem>.toDomainList() =
    map { json ->
        WowMetaData(
            content = json.video.toDomain(json.audio),
            movieTitle = json.movie,
            characterName = json.character,
            directorName = json.director,
            releaseDate = json.releaseDate,
            movieDuration = json.movieDuration,
            releaseYear = json.year.toString(),
            fullLine = json.fullLine,
            posterUrl = json.poster,
            wowStats = WowStats(
                totalWowsInMovie = json.totalWowsInMovie,
                indexOfWow = json.currentWowInMovie,
                timeOfWow =  RunTimeFormatter.getRuntimeAsLong(json.timestamp),
                durationOfFilm = RunTimeFormatter.getRuntimeAsLong(json.movieDuration),
            ),

        )
    }

internal fun Video.toDomain(audioUrl: String) = WowContent(
    audioUrl = audioUrl,
    videoContent = VideoContent(
        highDefOptions = listOf(
            VideoOption.HighDef(
                url = this.highDefLarge,
                resolution = Resolution.HD.TEN_80,
            ),
            VideoOption.HighDef(
                url = this.highDefSmall,
                resolution = Resolution.HD.SEVEN_20,
            )
        ),
        standardDefOptions = listOf(
            VideoOption.StandardDef(
                url = this.standardDef,
                resolution = Resolution.SD.FOUR_80,
            ),
            VideoOption.StandardDef(
                url = this.lowDef,
                resolution = Resolution.SD.THREE_60,
            )
        )
    )
)