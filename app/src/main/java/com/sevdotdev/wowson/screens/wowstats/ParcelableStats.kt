package com.sevdotdev.wowson.screens.wowstats

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableStats(
    val totalWowsInMovie: Int,
    val indexOfWow: Int,
    val timeOfWow: Long,
    val durationOfFilm: Long,
): Parcelable