package com.music.mp3.spotify.mealcount.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.Menu

@Entity(tableName = "mete_data")
data class MealCountMetaDataEntity(
    @PrimaryKey(autoGenerate = false)
    val date: String,
    val dayMenu: String,
    val nightMenu: String,
    val roomsWithFloors: Map<Int, Pair<Int, Int>>
)
