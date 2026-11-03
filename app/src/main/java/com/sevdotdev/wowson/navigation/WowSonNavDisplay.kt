package com.sevdotdev.wowson.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sevdotdev.wowson.screens.favorites.FavoritesScreen
import com.sevdotdev.wowson.screens.wowdetails.WowDetailsScreen
import com.sevdotdev.wowson.screens.wowlist.WowListScreen
import com.sevdotdev.wowson.screens.wowmovies.WowMoviesScreen
import com.sevdotdev.wowson.screens.wowstats.WowStatsScreen

@Composable
fun WowSonNavDisplay(
    backStack: TopLevelBackStack,
    modifier: Modifier = Modifier,
) {
    val entryProvider = entryProvider<NavKey> {
        entry<WowListKey> {
            WowListScreen(onDetailsClicked = { backStack.push(WowDetailsKey(it)) })
        }
        entry<WowDetailsKey> { key ->
            WowDetailsScreen(movieId = key.movieId)
        }
        entry<WowStatsKey> { key ->
            WowStatsScreen(stats = key.stats)
        }
        entry<WowMoviesKey> { WowMoviesScreen() }
        entry<FavoritesKey> { FavoritesScreen() }
    }

    // Decorate each tab's stack separately. Entries in a hidden tab stay in their own stack,
    // so their saved UI state and ViewModels survive tab switches. They are only cleared
    // when popped.
    val entriesByTab = backStack.stacks.mapValues { (tab, stack) ->
        key(tab) {
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider,
            )
        }
    }

    NavDisplay(
        entries = backStack.tabsInUse.flatMap { entriesByTab.getValue(it) },
        onBack = { backStack.pop() },
        modifier = modifier,
    )
}
