package com.music.mp3.spotify.mealcount.presentation.table_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.mp3.spotify.mealcount.data.local.entity.RoomWithCountEntity
import com.music.mp3.spotify.mealcount.data.repo.SumOfMeals
import com.music.mp3.spotify.mealcount.domain.mappers.toListOfWholeDayCount
import com.music.mp3.spotify.mealcount.domain.models.WholeDayCount
import com.music.mp3.spotify.mealcount.domain.repo.MealCountRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TableViewModel @Inject constructor(
    private val repo: MealCountRepo
) : ViewModel() {

    private val _state = MutableStateFlow<List<WholeDayCount>>(emptyList())
    val state = _state.asStateFlow()

    private val _menus = MutableStateFlow(Pair("", ""))
    val menus = _menus.asStateFlow()

    private val _sumOfCounts = MutableStateFlow(SumOfMeals())
    val sumOfCounts = _sumOfCounts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun getAllCounts(date: String) {
        viewModelScope.launch {
            try {
                _isLoading.emit(true)
                repo.getAllMealCounts(date).collectLatest { list ->
                    val newList = list.sortedBy { it.roomNo }
                    _state.emit(newList.toListOfWholeDayCount())
                    _isLoading.emit(false)
                }

            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
                _isLoading.emit(false)
            }
        }
    }

    fun getMenuOfDate(date: String) {
        viewModelScope.launch {
            try {
                repo.getMetaData(date).collectLatest {
                    _menus.emit(Pair(it.dayMenu, it.nightMenu))
                }
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }

    fun getSumOfMeals(date: String) {
        viewModelScope.launch {
            try {
                repo.getSumForEveryTypesOfItem(date).collectLatest {
                    _sumOfCounts.emit(it)
                }
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }
}