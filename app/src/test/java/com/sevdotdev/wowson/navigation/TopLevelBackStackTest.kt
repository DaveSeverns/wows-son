package com.sevdotdev.wowson.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TopLevelBackStackTest {

    private fun newBackStack(): TopLevelBackStack {
        val tabs = listOf<NavKey>(WowListKey, WowMoviesKey, FavoritesKey)
        return TopLevelBackStack(
            startTab = WowListKey,
            stacks = tabs.associateWith { NavBackStack(it) },
            currentTabState = mutableStateOf(WowListKey),
        )
    }

    @Test
    fun `starts on the start tab root`() {
        val nav = newBackStack()
        assertEquals(WowListKey, nav.currentTab)
        assertEquals(listOf(WowListKey), nav.visibleStack)
    }

    @Test
    fun `push adds to the current tab`() {
        val nav = newBackStack()
        nav.push(WowDetailsKey("1"))
        assertEquals(listOf(WowListKey, WowDetailsKey("1")), nav.visibleStack)
    }

    @Test
    fun `other tabs show on top of the start tab`() {
        val nav = newBackStack()
        nav.push(WowDetailsKey("1"))
        nav.switchTab(FavoritesKey)
        assertEquals(listOf(WowListKey, WowDetailsKey("1"), FavoritesKey), nav.visibleStack)
    }

    @Test
    fun `switching tabs keeps each tab's stack`() {
        val nav = newBackStack()
        nav.push(WowDetailsKey("1"))
        nav.switchTab(WowMoviesKey)
        nav.push(WowDetailsKey("2"))
        nav.switchTab(WowListKey)
        assertEquals(listOf(WowListKey, WowDetailsKey("1")), nav.visibleStack)
        nav.switchTab(WowMoviesKey)
        assertEquals(WowDetailsKey("2"), nav.visibleStack.last())
    }

    @Test
    fun `reselecting the current tab pops to its root`() {
        val nav = newBackStack()
        nav.push(WowDetailsKey("1"))
        nav.push(WowDetailsKey("2"))
        nav.switchTab(WowListKey)
        assertEquals(listOf(WowListKey), nav.visibleStack)
    }

    @Test
    fun `back from another tab root returns to the start tab`() {
        val nav = newBackStack()
        nav.switchTab(FavoritesKey)
        assertTrue(nav.pop())
        assertEquals(WowListKey, nav.currentTab)
    }

    @Test
    fun `back pops within the current tab first`() {
        val nav = newBackStack()
        nav.switchTab(WowMoviesKey)
        nav.push(WowDetailsKey("1"))
        assertTrue(nav.pop())
        assertEquals(WowMoviesKey, nav.currentTab)
        assertEquals(WowMoviesKey, nav.visibleStack.last())
    }

    @Test
    fun `back at the start tab root is not handled`() {
        assertFalse(newBackStack().pop())
    }
}
