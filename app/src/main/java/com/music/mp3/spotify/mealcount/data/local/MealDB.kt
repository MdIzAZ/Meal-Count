package com.music.mp3.spotify.mealcount.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.music.mp3.spotify.mealcount.data.local.converter.Converters
import com.music.mp3.spotify.mealcount.data.local.entity.MealCountMetaDataEntity
import com.music.mp3.spotify.mealcount.data.local.entity.RoomWithCountEntity


@Database(
    entities = [RoomWithCountEntity::class, MealCountMetaDataEntity::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MealDB : RoomDatabase() {
    abstract fun mealCountDao(): MealCountDao
    abstract fun metaDataDao(): MetaDataDao
}