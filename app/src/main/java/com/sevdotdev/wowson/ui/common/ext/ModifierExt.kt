package com.sevdotdev.wowson.ui.common.ext

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.screenPadding() = Modifier
    .fillMaxSize()
    .padding(vertical = 8.dp, horizontal = 16.dp)

fun Modifier.modifyIf(predicate: Boolean, modifyWith: Modifier.() -> Modifier): Modifier =
    if (predicate) {
        this.modifyWith()
    } else {
        this
    }

fun Modifier.modifyIf(predicate: () -> Boolean, modifyWith: Modifier.() -> Modifier): Modifier =
    if (predicate()) {
        this.modifyWith()
    } else {
        this
    }