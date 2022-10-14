package com.sevdotdev.domain.model

import java.io.Serializable

data class WowStats(
    val totalWowsInMovie: Int,
    val indexOfWow: Int,
    val timeOfWow: Long,
    val durationOfFilm: Long,
): Serializable
