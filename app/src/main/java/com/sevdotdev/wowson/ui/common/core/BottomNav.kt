package com.sevdotdev.wowson.ui.common.core

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import com.sevdotdev.wowson.navigation.TopLevelTab
import com.sevdotdev.wowson.ui.theme.WowSonTheme

@Composable
fun BottomNavBar(
    selectedTab: NavKey,
    onTabSelected: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        TopLevelTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = tab.key == selectedTab,
                onClick = { onTabSelected(tab.key) },
                icon = { Icon(imageVector = tab.icon, contentDescription = null) },
                label = { Text(text = stringResource(id = tab.label)) },
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavBarPreview() {
    WowSonTheme {
        BottomNavBar(selectedTab = TopLevelTab.start.key, onTabSelected = {})
    }
}
