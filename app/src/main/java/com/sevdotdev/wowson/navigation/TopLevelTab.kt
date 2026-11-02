package com.sevdotdev.wowson.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.sevdotdev.wowson.R
import com.sevdotdev.wowson.ui.common.core.icons.VectorIcons
import com.sevdotdev.wowson.ui.common.core.icons.WowIcon

/** Bottom-nav tabs, in display order. */
enum class TopLevelTab(
    val key: NavKey,
    val icon: ImageVector,
    @param:StringRes val label: Int,
) {
    Discover(WowListKey, Icons.Filled.Search, R.string.discover_label),
    Movies(WowMoviesKey, VectorIcons.WowIcon, R.string.movies_label),
    Favorites(FavoritesKey, Icons.Filled.Favorite, R.string.favorites_label);

    companion object {
        val start = Discover
        val keys: List<NavKey> = entries.map { it.key }
    }
}
