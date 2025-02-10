package com.music.mp3.spotify.mealcount.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.music.mp3.spotify.mealcount.presentation.screens.pager.MealWithCounts


@Entity(tableName = "meal_counts", primaryKeys = ["roomNo", "date"])
data class RoomWithCountEntity(
    val roomNo: Int,
    val date: String,
    val dVeg: String,
    val nVeg: String,
    val dNonVeg: String,
    val nNonVeg: String,
    val dHalal: String,
    val nHalal: String,
    val dayHalalList: List<String>,
    val nightHalalList: List<String>
)
