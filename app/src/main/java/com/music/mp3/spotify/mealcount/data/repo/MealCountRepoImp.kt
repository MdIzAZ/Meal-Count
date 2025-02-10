package com.music.mp3.spotify.mealcount.data.repo

import com.music.mp3.spotify.mealcount.data.local.MealDB
import com.music.mp3.spotify.mealcount.data.local.entity.MealCountMetaDataEntity
import com.music.mp3.spotify.mealcount.data.local.entity.RoomWithCountEntity
import com.music.mp3.spotify.mealcount.domain.repo.MealCountRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class MealCountRepoImp(
    private val mealDB: MealDB
) : MealCountRepo {

    private val mealCountDao = mealDB.mealCountDao()
    private val metaDataDao = mealDB.metaDataDao()

    override fun getAllMealCounts(date: String): Flow<List<RoomWithCountEntity>> {
        return mealCountDao.getAllCountsOfADate(date)
    }

    override fun getAllCount(): Flow<List<RoomWithCountEntity>> {
        return mealCountDao.getAllCount()
    }

    override suspend fun insertMealCount(roomWithCountEntity: RoomWithCountEntity) {
        mealCountDao.insert(roomWithCountEntity)
    }

    override suspend fun deleteCountsOfADay(date: String) {
        mealCountDao.deleteCountsOfADay(date)
    }

    override suspend fun insertMetaData(meta: MealCountMetaDataEntity) {
        metaDataDao.insertMetaData(meta)
    }

    override suspend fun deleteMetaData(date: String) {
        metaDataDao.deleteMetaDataByDate(date)
    }

    override fun getMetaData(date: String): Flow<MealCountMetaDataEntity> {
        return metaDataDao.getMetaData(date)
    }

    override fun getSumForEveryTypesOfItem(date: String): Flow<SumOfMeals> = flow {

        val dVeg = mealCountDao.getSumOfDVegForDate(date).firstOrNull() ?: 0
        val nVeg = mealCountDao.getSumOfNVegForDate(date).firstOrNull() ?: 0
        val dNonVeg = mealCountDao.getSumOfDNonVegForDate(date).firstOrNull() ?: 0
        val nNonVeg = mealCountDao.getSumOfNNonVegForDate(date).firstOrNull() ?: 0
        val dHalal = mealCountDao.getSumOfDHalalForDate(date).firstOrNull() ?: 0
        val nHalal = mealCountDao.getSumOfNHalalForDate(date).firstOrNull() ?: 0

        emit(SumOfMeals(dVeg, nVeg, dNonVeg, nNonVeg, dHalal, nHalal))
    }

}


data class SumOfMeals(
    val dVeg:Int = 0,
    val nVeg:Int = 0,
    val dNonVeg:Int = 0,
    val nNonVeg:Int = 0,
    val dHalal:Int = 0,
    val nHalal:Int = 0,
)