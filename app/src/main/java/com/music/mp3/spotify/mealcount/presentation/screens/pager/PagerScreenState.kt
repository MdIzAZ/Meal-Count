package com.music.mp3.spotify.mealcount.presentation.screens.pager

import androidx.room.Entity

data class PagerScreenState(
    val dHalal: List<Halal> = mutableListOf(),
    val nHalal: List<Halal> = mutableListOf(),
    val morning: Map<Int, MealWithCounts> = mutableMapOf(),
    val night: Map<Int, MealWithCounts> = mutableMapOf(),
)


data class MealWithCounts(
    val veg: Int = 0,
    val nonVeg: Int = 0,
    val halal: Int = 0,
)

data class Halal(
    val name: String,
    val roomNo: Int
)
