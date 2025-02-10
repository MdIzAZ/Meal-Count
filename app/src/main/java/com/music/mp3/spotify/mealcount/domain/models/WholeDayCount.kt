package com.music.mp3.spotify.mealcount.domain.models


data class WholeDayCount(
    val roomNo: Int,
    val date: String,
    val dVeg: String,
    val nVeg: String,
    val dNonVeg: String,
    val nNonVeg: String,
    val dHalal: String,
    val nHalal: String,
    val dayHalals: List<String>,
    val nightHalals: List<String>
)
