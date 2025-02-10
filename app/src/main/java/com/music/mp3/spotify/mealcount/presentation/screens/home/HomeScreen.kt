package com.music.mp3.spotify.mealcount.presentation.screens.home

import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.music.mp3.spotify.mealcount.R
import com.music.mp3.spotify.mealcount.domain.models.WholeDayCount
import com.music.mp3.spotify.mealcount.presentation.components.CustomAlertDialog
import com.music.mp3.spotify.mealcount.presentation.components.CustomTopAppBar
import com.music.mp3.spotify.mealcount.presentation.components.MealSummaryCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    previousCounts: Map<String, List<WholeDayCount>>,
    onCardClick: (String) -> Unit,
    onBackPress: () -> Unit,
    onAddNew: () -> Unit,
    onDelete: (String) -> Unit
) {

    val view = LocalView.current

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Meal Count",
                onNavIconClick = onBackPress
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onAddNew()
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            )
        }

    ) { ip ->


        var shouldShowDialog by rememberSaveable { mutableStateOf(false) }
        var dateToBeDeleted by rememberSaveable { mutableStateOf("") }

        if (shouldShowDialog) {
            CustomAlertDialog(
                title = "Delete Meal Counts?",
                message = "Are you sure you want to delete this item? This action cannot be undone.",
                onDismissRequest = {
                    dateToBeDeleted = ""
                    shouldShowDialog = false
                },
                onConfirmBtnClick = {
                    onDelete(dateToBeDeleted)
                    shouldShowDialog = false
                }
            )
        }



        /*if (shouldShowDialog) AlertDialog(
            icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete") },
            title = { Text(text = "Delete Meal Counts?") },
            text = {
                Text(text = "Are you sure you want to delete this item? This action cannot be undone.")
            },
            onDismissRequest = {
                dateToBeDeleted = ""
                shouldShowDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(dateToBeDeleted)
                        shouldShowDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dateToBeDeleted = ""
                        shouldShowDialog = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )*/


        val oldCounts = previousCounts.keys.toList()

        if (previousCounts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(ip),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = null
                )
                Text("No Records found,\n Click + to add new")
            }
        } else

            Column(
                modifier = Modifier
                    .padding(ip)
                    .padding(5.dp)
                    .fillMaxSize()
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    content = {
                        items(oldCounts) {
                            MealSummaryCard(
                                countedBy = 2022,
                                countedAt = it,
                                onCardClick = { onCardClick(it) },
                                onDelete = { d ->
                                    dateToBeDeleted = d
                                    shouldShowDialog = true
                                }
                            )
                        }
                    }
                )

            }
    }

}

