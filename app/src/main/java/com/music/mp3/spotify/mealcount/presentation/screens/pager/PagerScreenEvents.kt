package com.music.mp3.spotify.mealcount.presentation.screens.pager

import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.MealTime

sealed class PagerScreenEvents {

    data class AddMealCounts(val date: String) : PagerScreenEvents()

    data class Increase(
        val roomNo: Int,
        val menuTime: MealTime,
        val mealType: MealType
    ) : PagerScreenEvents()

    data class Decrease(
        val roomNo: Int,
        val menuTime: MealTime,
        val mealType: MealType
    ) : PagerScreenEvents()

    data class HalalListChange(
        val names: List<Halal>,
        val roomNo: Int,
        val menuTime: MealTime,
        val changeTYPE: COUNTER_TYPE
    ):PagerScreenEvents()
}

enum class MealType {
    HALAL, VEG, NON_VEG
}