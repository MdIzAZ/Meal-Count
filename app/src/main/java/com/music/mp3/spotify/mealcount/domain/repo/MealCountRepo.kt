package com.music.mp3.spotify.mealcount.domain.repo

import com.music.mp3.spotify.mealcount.data.local.entity.MealCountMetaDataEntity
import com.music.mp3.spotify.mealcount.data.local.entity.RoomWithCountEntity
import com.music.mp3.spotify.mealcount.data.repo.SumOfMeals
import kotlinx.coroutines.flow.Flow

interface MealCountRepo {

    fun getAllMealCounts(date: String) : Flow<List<RoomWithCountEntity>>

    fun getAllCount():Flow<List<RoomWithCountEntity>>

    suspend fun insertMealCount(roomWithCountEntity: RoomWithCountEntity)

    suspend fun deleteCountsOfADay(date: String)

    suspend fun insertMetaData(meta: MealCountMetaDataEntity)

    suspend fun deleteMetaData(date: String)

    fun getMetaData(date: String): Flow<MealCountMetaDataEntity>

    fun getSumForEveryTypesOfItem(date: String): Flow<SumOfMeals>

}

