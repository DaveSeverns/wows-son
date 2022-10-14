package com.sevdotdev.wowson.navigation


import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.sevdotdev.wowson.R
import com.sevdotdev.wowson.screens.destinations.FavoritesScreenDestination
import com.sevdotdev.wowson.screens.destinations.WowListScreenDestination
import com.sevdotdev.wowson.screens.destinations.WowMoviesScreenDestination
import com.sevdotdev.wowson.ui.common.core.icons.VectorIcons
import com.sevdotdev.wowson.ui.common.core.icons.WowIcon

enum class NavScreen(
    val icon: ImageVector,
    @StringRes val label: Int? = null,
    val navRoute: String,
) {
    WowList(Icons.Filled.Search,
        label = R.string.discover_label,
        navRoute = WowListScreenDestination.route),
    WowMovies(VectorIcons.WowIcon,
        label = R.string.movies_label,
        navRoute = WowMoviesScreenDestination.route),
    Favorites(Icons.Filled.Favorite,
        label = R.string.favorites_label,
        navRoute = FavoritesScreenDestination.route)
}