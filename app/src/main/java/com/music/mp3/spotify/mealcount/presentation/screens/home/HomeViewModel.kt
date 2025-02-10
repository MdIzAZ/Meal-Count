package com.music.mp3.spotify.mealcount.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class HomeViewModel @Inject constructor(
    private val repo: MealCountRepo
) : ViewModel() {

    private val _previousRecords = MutableStateFlow<List<WholeDayCount>>(emptyList())
    val previousRecords = _previousRecords.asStateFlow()


    fun getAllCounts() {
        viewModelScope.launch {
            try {
                repo.getAllCount().collectLatest {
                    _previousRecords.emit(it.toListOfWholeDayCount())
                }
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }
    }

    fun deleteCountsOfADay(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteCountsOfADay(date)
            } catch (e: Exception) {
                Log.d("izaz", e.message ?: "Unknown error")
            }
        }

    }



}
