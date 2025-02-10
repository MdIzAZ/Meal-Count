package com.music.mp3.spotify.mealcount.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.music.mp3.spotify.mealcount.data.local.entity.RoomWithCountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealCountDao {

    @Query("SELECT * FROM meal_counts where date = :date")
    fun getAllCountsOfADate(date: String): Flow<List<RoomWithCountEntity>>

    @Query("SELECT * FROM meal_counts")
    fun getAllCount():Flow<List<RoomWithCountEntity>>

    @Upsert
    fun insert(roomWithCountEntity: RoomWithCountEntity)

    @Query("DELETE FROM meal_counts WHERE date = :date")
    fun deleteCountsOfADay(date: String)


    //total
    @Query("SELECT SUM(dVeg) FROM meal_counts WHERE date = :date")
    fun getSumOfDVegForDate(date: String): Flow<Int>

    @Query("SELECT SUM(dHalal) FROM meal_counts WHERE date = :date")
    fun getSumOfDHalalForDate(date: String): Flow<Int>

    @Query("SELECT SUM(nVeg) FROM meal_counts WHERE date = :date")
    fun getSumOfNVegForDate(date: String): Flow<Int>

    @Query("SELECT SUM(nHalal) FROM meal_counts WHERE date = :date")
    fun getSumOfNHalalForDate(date: String): Flow<Int>

    @Query("SELECT SUM(dNonVeg) FROM meal_counts WHERE date = :date")
    fun getSumOfDNonVegForDate(date: String): Flow<Int>

    @Query("SELECT SUM(nNonVeg) FROM meal_counts WHERE date = :date")
    fun getSumOfNNonVegForDate(date: String): Flow<Int>


}