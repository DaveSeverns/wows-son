package com.sevdotdev.wowson

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.sevdotdev.wowson.navigation.TopLevelTab
import com.sevdotdev.wowson.navigation.WowSonNavDisplay
import com.sevdotdev.wowson.navigation.rememberTopLevelBackStack
import com.sevdotdev.wowson.ui.common.core.BottomNavBar
import com.sevdotdev.wowson.ui.theme.WowSonTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toggler =  object : OrientationToggler {
            override fun toggle(orientation: Int) {
                rotateToggle(orientation)
            }

        }
        setContent {
            CompositionLocalProvider(LocalToggler provides toggler) {
                WowSonApp()
            }
        }
    }
}

@Composable
fun WowSonApp() {
    WowSonTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val backStack = rememberTopLevelBackStack(
                startTab = TopLevelTab.start.key,
                tabs = TopLevelTab.keys,
            )
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        selectedTab = backStack.currentTab,
                        onTabSelected = backStack::switchTab,
                    )
                },
            ) { innerPadding ->
                WowSonNavDisplay(
                    backStack = backStack,
                    modifier = Modifier.padding(innerPadding),
                )
            }
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
    lifecycleScope.launch{
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        delay(600)
    }
}