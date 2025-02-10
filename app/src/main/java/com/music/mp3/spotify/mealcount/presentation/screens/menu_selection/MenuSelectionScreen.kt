package com.music.mp3.spotify.mealcount.presentation.screens.menu_selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.mp3.spotify.mealcount.presentation.components.CustomTopAppBar
import com.music.mp3.spotify.mealcount.presentation.components.MenuSelectionSection
import kotlinx.serialization.Serializable

@Composable
fun MenuSelectionScreen(
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit ,
    onNextButtonClick: (morning: String, night: String) -> Unit
) {
    Scaffold(
        topBar = { CustomTopAppBar(title = "Select Menu", onNavIconClick = onBackPress) }
    ) { ip ->

        var morningMenuItem by rememberSaveable { mutableStateOf(Menu.VEG.name) }
        var nightMenuItem by rememberSaveable { mutableStateOf(Menu.VEG.name) }

        var currentSelectionTime by rememberSaveable { mutableStateOf(MealTime.DAY) }


        Column(
            modifier = Modifier
                .padding(ip)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.wrapContentSize()) {
                MenuSelectionSection(
                    mealTime = MealTime.DAY,
                    menuItem = Menu.valueOf(morningMenuItem),
                    onMenuIconClick = {
                        currentSelectionTime = MealTime.DAY
                    },
                    onMenuSelect = {
                        morningMenuItem = it
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

                MenuSelectionSection(
                    mealTime = MealTime.NIGHT,
                    menuItem = Menu.valueOf(nightMenuItem),
                    onMenuIconClick = {
                        currentSelectionTime = MealTime.NIGHT
                    },
                    onMenuSelect = {
                        nightMenuItem = it
                    }
                )
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                ElevatedButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = {onNextButtonClick(morningMenuItem, nightMenuItem)},
                    content = {
                        Text(
                            text = "Next",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }

        }

    }
}


@Serializable
enum class Menu {
    VEG, EGG, FISH, MEAT
}

@Serializable
enum class MealTime {
    DAY, NIGHT
}