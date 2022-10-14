package com.sevdotdev.wowson.navigation


import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.sevdotdev.domain.model.WowStats
import com.sevdotdev.wowson.R
import com.sevdotdev.wowson.navigation.NavArgKeys.MOVIE_ID
import com.sevdotdev.wowson.ui.common.core.icons.VectorIcons
import com.sevdotdev.wowson.ui.common.core.icons.WowIcon

enum class NavScreen(val icon: ImageVector,  @StringRes val label: Int? = null, val navRoute: String) {
    WowList(Icons.Filled.Search, label = R.string.discover_label, navRoute = HomeScreen),
    WowMovies(VectorIcons.WowIcon, label = R.string.movies_label, navRoute = NavRoutes.WowMovies),
    Favorites(Icons.Filled.Favorite,label = R.string.favorites_label, navRoute = NavRoutes.Favorites)
}

fun getByRoute(route: String?): NavScreen {
    return when (route) {
        NavScreen.WowList.navRoute -> NavScreen.WowList
        NavScreen.WowMovies.navRoute -> NavScreen.WowMovies
        NavScreen.Favorites.navRoute -> NavScreen.Favorites
        else -> NavScreen.WowList
    }
}

val HomeScreen: String
    get() = NavRoutes.WowList

object NavRoutes {
    const val WowList = "WowList.Screen"
    const val WowDetails = "$WowDetailsBasePath{$MOVIE_ID}"
    const val WowStats = "WowStats.Screen"
    const val WowMovies = "WowMovies.Screen"
    const val Favorites = "Favorites.Screen"
}

const val WowDetailsBasePath = "WowDetails.Screen/"

object NavArgKeys {
    const val MOVIE_ID = "movieId"
    const val WOW_STATS = "wowStats"
}