package com.music.mp3.spotify.mealcount.domain.mappers

import com.music.mp3.spotify.mealcount.data.local.entity.RoomWithCountEntity
import com.music.mp3.spotify.mealcount.domain.models.WholeDayCount
import com.music.mp3.spotify.mealcount.presentation.screens.pager.Halal
import com.music.mp3.spotify.mealcount.presentation.screens.pager.MealWithCounts


fun RoomWithCountEntity.toWholeDayCount(): WholeDayCount {
    return WholeDayCount(
        roomNo = roomNo,
        date = date,
        dVeg = dVeg,
        nVeg = nVeg,
        dNonVeg = dNonVeg,
        nNonVeg = nNonVeg,
        dHalal = dHalal,
        nHalal = nHalal,
        dayHalals = dayHalalList,
        nightHalals = nightHalalList
    )
}

fun WholeDayCount.toRoomWithCountEntity(): RoomWithCountEntity {
    return RoomWithCountEntity(
        roomNo = roomNo,
        date = date,
        dVeg = dVeg,
        nVeg = nVeg,
        dNonVeg = dNonVeg,
        nNonVeg = nNonVeg,
        dHalal = dHalal,
        nHalal = nHalal,
        dayHalalList = dayHalals,
        nightHalalList = nightHalals
    )
}


fun List<RoomWithCountEntity>.toListOfWholeDayCount(): List<WholeDayCount> {
    return this.map {
        it.toWholeDayCount()
    }
}


fun getCombineMapOfWholeDayCount(
    date: String,
    morning: Map<Int, MealWithCounts>,
    night: Map<Int, MealWithCounts>,
    dayHalalList: List<Halal>,
    nightHalalList: List<Halal>
): Map<Int, WholeDayCount> {

    val combinedMap = mutableMapOf<Int, WholeDayCount>()

    for ((room, mealWithCounts) in morning) {

        val dVeg = mealWithCounts.veg.toString()
        val dNonVeg = mealWithCounts.nonVeg.toString()
        val dHalal = mealWithCounts.halal.toString()

        combinedMap[room] = WholeDayCount(
            date = date,
            roomNo = room,
            dVeg = dVeg,
            dNonVeg = dNonVeg,
            dHalal = dHalal,
            nVeg = "",
            nNonVeg = "",
            nHalal = "",
            dayHalals = dayHalalList.filter { it.roomNo == room }.map { it.name },
            nightHalals = nightHalalList.filter { it.roomNo == room }.map { it.name }
        )
    }


    for ((roomNo, mealWithCounts) in night) {
        val nVeg = mealWithCounts.veg.toString()
        val nNonVeg = mealWithCounts.nonVeg.toString()
        val nHalal = mealWithCounts.halal.toString()

        if (combinedMap.containsKey(roomNo)) {
            val wholeDayCount = combinedMap[roomNo]!!
            combinedMap[roomNo] = wholeDayCount.copy(
                nVeg = nVeg,
                nNonVeg = nNonVeg,
                nHalal = nHalal
            )
        } else {
            combinedMap[roomNo] = WholeDayCount(
                date = date,
                roomNo = roomNo,
                nVeg = nVeg,
                nNonVeg = nNonVeg,
                nHalal = nHalal,
                dVeg = "",
                dNonVeg = "",
                dHalal = "",
                dayHalals = dayHalalList.filter { it.roomNo == roomNo }.map { it.name },
                nightHalals = nightHalalList.filter { it.roomNo == roomNo }.map { it.name }
            )
        }
    }

    return combinedMap
}