package com.music.mp3.spotify.mealcount.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.music.mp3.spotify.mealcount.data.local.entity.MealCountMetaDataEntity
import com.music.mp3.spotify.mealcount.data.local.entity.RoomWithCountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaDataDao {

    @Query("SELECT * FROM mete_data where date = :date")
    fun getMetaData (date: String): Flow<MealCountMetaDataEntity>

    @Upsert
    fun insertMetaData(metadata: MealCountMetaDataEntity)

    @Query("DELETE FROM mete_data WHERE date = :date")
    fun deleteMetaDataByDate(date: String)
}