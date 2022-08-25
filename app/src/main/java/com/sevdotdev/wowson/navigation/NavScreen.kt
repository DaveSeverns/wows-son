package com.sevdotdev.wowson.navigation

abstract class NavScreen {
    protected abstract val routeId: String
    abstract fun buildPath(): String
}

sealed class NoArgsNavScreen: NavScreen() {
    override fun buildPath(): String = routeId
    object Home: NoArgsNavScreen() {
        override val routeId: String
            get() = Routes.HOME
    }
}

sealed class ArgsNavScreen(val argumentsPath: String): NavScreen() {
    override fun buildPath(): String = routeId + argumentsPath
    object WowDetails: ArgsNavScreen(argumentsPath = "/{$MOVIE_TITLE}/{$WOW_IN_MOVIE}") {
        override val routeId: String
            get() = Routes.WOW_DETAILS
    }


    companion object {
        const val MOVIE_TITLE = "movieTitle"
        const val WOW_IN_MOVIE = "wowInMovie"
    }
}

private object Routes {
    const val HOME = "HOME"
    const val WOW_DETAILS = "WOW_DETAILS"
}