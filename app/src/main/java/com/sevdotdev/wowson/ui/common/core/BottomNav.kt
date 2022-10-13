package com.sevdotdev.wowson.ui.common.core

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sevdotdev.wowson.navigation.NavScreen

@Composable
fun BottomNavBar(
    selectedScreen: NavScreen = NavScreen.WowList,
    onScreenSelected: (NavScreen) -> Unit = {},
) {
    BottomAppBar {
        NavScreen.values().forEach {
            NavigationBarItem(selected = it == selectedScreen,
                onClick = { onScreenSelected(it) },
                icon = { Icon(imageVector = it.icon, contentDescription = it.name) },
                label = it.label?.let { resId ->
                    { Text(text = stringResource(id = resId)) }
                }
            )
        }
    }
}