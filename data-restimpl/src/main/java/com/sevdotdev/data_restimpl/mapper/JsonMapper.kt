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

internal fun List<WowJsonItem>.toDomainList() =
    map {
        it.toDomain()
    }

internal fun WowJsonItem.toDomain() = WowMetaData(
    id = this.movie + DELIMITER + this.currentWowInMovie ,
    content = this.video.toDomain(this.audio),
    movieTitle = this.movie,
    characterName = this.character,
    directorName = this.director,
    releaseDate = this.releaseDate,
    movieDuration = this.movieDuration,
    releaseYear = this.year.toString(),
    fullLine = this.fullLine,
    posterUrl = this.poster,
    wowStats = WowStats(
        totalWowsInMovie = this.totalWowsInMovie,
        indexOfWow = this.currentWowInMovie,
        timeOfWow = RunTimeFormatter.getRuntimeAsLong(this.timestamp),
        durationOfFilm = RunTimeFormatter.getRuntimeAsLong(this.movieDuration),
    ),
)

internal const val DELIMITER = "-"

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