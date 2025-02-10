package com.music.mp3.spotify.mealcount.presentation.navigation

import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.Menu
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object HomeScreen : Routes()

    @Serializable
    data object MenuSelectionScreen : Routes()

    @Serializable
    data class FloorAndRoomSelectionScreen(val morning: String, val night: String) : Routes()

    @Serializable
    data class PagerScreen(
        val date: String,
        val morning: String,
        val night: String
    ) : Routes()

    @Serializable
    data class TableScreen(val date: String): Routes()
}