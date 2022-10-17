package com.sevdotdev.wowson

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.sevdotdev.wowson.navigation.NavScreen
import com.sevdotdev.wowson.screens.favorites.FavoriteWowsScreen
import com.sevdotdev.wowson.screens.wowlist.WowListScreen
import com.sevdotdev.wowson.screens.wowmovies.WowMoviesScreen
import com.sevdotdev.wowson.ui.common.core.BottomNavBar
import com.sevdotdev.wowson.ui.theme.WowSonTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toggler = object : OrientationToggler {
            override fun toggle(orientation: Int) {
                rotateToggle(orientation)
            }

        }
        setContent {
            WowSonApp {
                var navScreen by rememberSaveable {
                    mutableStateOf(NavScreen.WowMovies)
                }

                CompositionLocalProvider(LocalToggler provides toggler) {
                    Navigator(
                        screen = WowMoviesScreen
                    ) { navigator ->
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = {
                                BottomNavBar(
                                    selectedRoute  = navScreen.navRoute,
                                    onScreenSelected = { screen ->
                                        navScreen = screen
                                        if (screen == NavScreen.WowMovies) {
                                            navigator.popUntilRoot()
                                        } else {
                                            navigator.push(FavoriteWowsScreen)
                                        }
                                    }
                                )
                            },
                        ) {
                            CurrentScreen()
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
