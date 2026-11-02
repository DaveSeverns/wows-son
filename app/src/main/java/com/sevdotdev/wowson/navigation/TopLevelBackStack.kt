package com.sevdotdev.wowson.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

/**
 * Navigation state for an app with bottom-nav tabs, where each tab keeps its own back stack.
 *
 * The start tab always sits underneath the current tab, so pressing back from the root of
 * another tab returns to the start tab instead of closing the app.
 */
class TopLevelBackStack(
    val startTab: NavKey,
    val stacks: Map<NavKey, NavBackStack<NavKey>>,
    private val currentTabState: MutableState<NavKey>,
) {
    init {
        require(startTab in stacks) { "startTab must be one of the tabs" }
    }

    val currentTab: NavKey get() = currentTabState.value

    /** Tabs whose stacks are on screen, bottom to top. */
    val tabsInUse: List<NavKey>
        get() = if (currentTab == startTab) listOf(startTab) else listOf(startTab, currentTab)

    /** All keys currently shown, bottom to top. */
    val visibleStack: List<NavKey>
        get() = tabsInUse.flatMap { stacks.getValue(it) }

    /** Selecting the current tab again pops it back to its root. */
    fun switchTab(tab: NavKey) {
        val stack = requireNotNull(stacks[tab]) { "$tab is not a top level tab" }
        if (tab == currentTab) {
            while (stack.size > 1) stack.removeAt(stack.lastIndex)
        } else {
            currentTabState.value = tab
        }
    }

    fun push(key: NavKey) {
        stacks.getValue(currentTab).add(key)
    }

    /** Returns false when there is nothing left to pop (the system should handle back). */
    fun pop(): Boolean {
        val stack = stacks.getValue(currentTab)
        return when {
            stack.size > 1 -> {
                stack.removeAt(stack.lastIndex)
                true
            }
            currentTab != startTab -> {
                currentTabState.value = startTab
                true
            }
            else -> false
        }
    }
}

/** Remembers a [TopLevelBackStack] whose stacks and current tab survive process death. */
@Composable
fun rememberTopLevelBackStack(startTab: NavKey, tabs: List<NavKey>): TopLevelBackStack {
    val stacks = tabs.associateWith { tab -> key(tab) { rememberNavBackStack(tab) } }
    val currentTab = rememberSaveable(
        saver = Saver(
            save = { tabs.indexOf(it.value) },
            restore = { mutableStateOf(tabs[it]) },
        ),
    ) { mutableStateOf(startTab) }
    return TopLevelBackStack(startTab, stacks, currentTab)
}
