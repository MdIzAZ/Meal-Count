package com.music.mp3.spotify.mealcount.presentation.screens.floors_rooms

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.mp3.spotify.mealcount.data.local.entity.MealCountMetaDataEntity
import com.music.mp3.spotify.mealcount.domain.repo.MealCountRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: MealCountRepo
) : ViewModel() {

    private val _rooms = MutableStateFlow<Map<Int, Pair<Int, Int>>>(emptyMap())
    val rooms = _rooms.asStateFlow()


    fun addRoomsAndFloors(roomsAndFloors: Map<Int, Pair<Int, Int>>){
        viewModelScope.launch {
            try {
                _rooms.value = roomsAndFloors
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }

    fun saveMetaDataToDb(
        date: String,
        dayMenu: String,
        nightMenu: String
    ) {

        val metaData = MealCountMetaDataEntity(
            date = date,
            dayMenu = dayMenu,
            nightMenu = nightMenu,
            roomsWithFloors = _rooms.value
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertMetaData(metaData)
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }


    fun loadRoomAndFloorsFromDb(date: String, callback: (String, String)->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getMetaData(date).collectLatest {
                    _rooms.emit(it.roomsWithFloors)
                    callback(it.dayMenu, it.nightMenu)
                }
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }






}