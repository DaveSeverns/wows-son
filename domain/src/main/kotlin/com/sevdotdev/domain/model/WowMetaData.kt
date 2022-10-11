package com.sevdotdev.domain.model

data class WowMetaData(
    val id: String,
    val movieTitle: String,
    val characterName: String,
    val actor: String = WOW_GOD,
    val directorName: String,
    val releaseDate: String,
    val movieDuration: String,
    val releaseYear: String,
    val fullLine: String,
    val posterUrl: String,
    val content: WowContent,
    val wowStats: WowStats,
)

const val WOW_GOD = "Owen Wilson"