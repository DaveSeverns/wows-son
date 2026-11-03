package com.sevdotdev.wowson.screens.wowstats

import com.sevdotdev.domain.model.WowStats
import kotlinx.serialization.Serializable

/**
 * Serializable copy of [WowStats] so it can live in a Nav3 back stack key.
 * Kept separate so the domain module doesn't need kotlinx.serialization.
 */
@Serializable
data class WowStatsArgs(
    val totalWowsInMovie: Int,
    val indexOfWow: Int,
    val timeOfWow: Long,
    val durationOfFilm: Long,
)

fun WowStats.toArgs(): WowStatsArgs = WowStatsArgs(
    totalWowsInMovie = totalWowsInMovie,
    indexOfWow = indexOfWow,
    timeOfWow = timeOfWow,
    durationOfFilm = durationOfFilm,
)
