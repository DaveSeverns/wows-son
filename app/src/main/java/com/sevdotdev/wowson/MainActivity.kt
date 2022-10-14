package com.sevdotdev.wowson

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.sevdotdev.wowson.navigation.HomeScreen
import com.sevdotdev.wowson.navigation.NavArgKeys
import com.sevdotdev.wowson.navigation.NavRoutes
import com.sevdotdev.wowson.navigation.WowDetailsBasePath
import com.sevdotdev.wowson.screens.favorites.FavoritesScreen
import com.sevdotdev.wowson.screens.wowdetails.WowDetailsScreen
import com.sevdotdev.wowson.screens.wowlist.WowListScreen
import com.sevdotdev.wowson.screens.wowmovies.WowMoviesScreen
import com.sevdotdev.wowson.screens.wowstats.ParcelableStats
import com.sevdotdev.wowson.screens.wowstats.WowStatsScreen
import com.sevdotdev.wowson.ui.common.core.BottomNavBar
import com.sevdotdev.wowson.ui.theme.WowSonTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toggler = object : OrientationToggler {
            override fun toggle(orientation: Int) {
                rotateToggle(orientation)
            }

        }
        setContent {
            WowSonApp {

                val navController = rememberNavController()

                val currentNavEntry by navController.currentBackStackEntryAsState()

                LaunchedEffect(key1 = navController) {
                    launch {
                        navController.currentBackStackEntryFlow.onEach {
                            Log.d("Current Back Stack entry", it.destination.route ?: "No route")
                            Log.d("Previous Back Stack entry",
                                navController.previousBackStackEntry?.destination?.route
                                    ?: "No previous route")
                        }.collect()
                    }
                }

                CompositionLocalProvider(LocalToggler provides toggler) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            BottomNavBar(
                                selectedRoute = currentNavEntry?.destination?.route,
                                onScreenSelected = {
                                    navController.navigate(it.navRoute) {
                                        launchSingleTop = true
                                        popUpTo(navController.previousBackStackEntry?.destination?.id
                                            ?: navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                    }
                                }
                            )
                        },
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = HomeScreen,
                        ) {
                            composable(route = HomeScreen) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    WowListScreen(
                                        onDetailsClicked = {
                                            navController.navigate(route = WowDetailsBasePath + it)
                                        }
                                    )
                                }
                            }
                            navigation("home", "other") {

                            }
                            composable(
                                route = NavRoutes.WowDetails,
                                arguments = listOf(navArgument(NavArgKeys.MOVIE_ID) { NavType.StringType })
                            ) {
                                val movieId = it.arguments?.getString(NavArgKeys.MOVIE_ID)
                                movieId?.let {
                                    WowDetailsScreen(movieId = it,
                                    onViewStatsClicked = { stats ->
                                        navController.currentBackStackEntry?.arguments?.putParcelable(
                                            NavArgKeys.WOW_STATS,
                                            stats)
                                        navController.navigate(NavRoutes.WowStats)
                                    })
                                }

                            }

                            composable(route = NavRoutes.WowStats) {
                                val model =
                                    navController.previousBackStackEntry?.arguments?.getParcelable<ParcelableStats>(
                                        NavArgKeys.WOW_STATS)
                                model?.let {
                                    WowStatsScreen(stats = it)
                                }
                            }

                            composable(route = NavRoutes.WowMovies) {
                                WowMoviesScreen()
                            }

                            composable(route = NavRoutes.Favorites) {
                                FavoritesScreen()
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun WowSonApp(
    appContent: @Composable () -> Unit,
) {
    WowSonTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            appContent()
        }
    }
}

val LocalToggler = compositionLocalOf<OrientationToggler> {
    object : OrientationToggler {
        override fun toggle(orientation: Int) {
            //NOOP
        }

    }
}

interface OrientationToggler {
    fun toggle(orientation: Int)
}

fun ComponentActivity.rotateToggle(orientation: Int) {
    requestedOrientation = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    } else {
        toPortraitUnspecified()
        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}

fun ComponentActivity.toPortraitUnspecified() {
    lifecycleScope.launch {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        delay(600)
    }
}