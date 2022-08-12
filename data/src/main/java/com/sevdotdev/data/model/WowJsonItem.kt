package com.sevdotdev.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WowJsonItem(
    @Json(name = "audio")
    val audio: String,
    @Json(name = "character")
    val character: String,
    @Json(name = "current_wow_in_movie")
    val currentWowInMovie: Int,
    @Json(name = "director")
    val director: String,
    @Json(name = "full_line")
    val fullLine: String,
    @Json(name = "movie")
    val movie: String,
    @Json(name = "movie_duration")
    val movieDuration: String,
    @Json(name = "poster")
    val poster: String,
    @Json(name = "release_date")
    val releaseDate: String,
    @Json(name = "timestamp")
    val timestamp: String,
    @Json(name = "total_wows_in_movie")
    val totalWowsInMovie: Int,
    @Json(name = "video")
    val video: Video,
    @Json(name = "year")
    val year: Int
)