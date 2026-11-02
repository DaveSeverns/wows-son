package com.sevdotdev.wowson.navigation

import androidx.navigation3.runtime.NavKey
import com.sevdotdev.wowson.screens.wowstats.WowStatsArgs
import kotlinx.serialization.Serializable

/** Top-level tabs. Each one is the root of its own back stack. */
@Serializable
data object WowListKey : NavKey

@Serializable
data object WowMoviesKey : NavKey

@Serializable
data object FavoritesKey : NavKey

/** Detail screens pushed on top of a tab. */
@Serializable
data class WowDetailsKey(val movieId: String) : NavKey

@Serializable
data class WowStatsKey(val stats: WowStatsArgs) : NavKey
