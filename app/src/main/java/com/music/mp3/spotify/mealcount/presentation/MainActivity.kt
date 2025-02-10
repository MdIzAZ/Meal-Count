package com.music.mp3.spotify.mealcount.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.music.mp3.spotify.mealcount.domain.models.MealCountCardDetails
import com.music.mp3.spotify.mealcount.presentation.navigation.NavGraph
import com.music.mp3.spotify.mealcount.presentation.screens.floors_rooms.SharedViewModel
import com.music.mp3.spotify.mealcount.presentation.screens.pager.PagerScreen
import com.music.mp3.spotify.mealcount.presentation.screens.pager.PagerViewModel
import com.music.mp3.spotify.mealcount.presentation.table_screen.TableViewModel
import com.music.mp3.spotify.mealcount.presentation.theme.MealCountTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MealCountTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { ip ->

                    val navController = rememberNavController()
                    val sharedViewModel = hiltViewModel<SharedViewModel>()
                    val tableViewModel = hiltViewModel<TableViewModel>()
                    val pagerViewModel = hiltViewModel<PagerViewModel>()

                    NavGraph(
                        navController = navController,
                        sharedViewModel = sharedViewModel,
                        pagerViewModel = pagerViewModel
                    )
                }
            }
        }
    }
}

