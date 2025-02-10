package com.music.mp3.spotify.mealcount.presentation.table_screen

import android.view.SoundEffectConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.music.mp3.spotify.mealcount.data.repo.SumOfMeals
import com.music.mp3.spotify.mealcount.domain.models.WholeDayCount
import com.music.mp3.spotify.mealcount.presentation.components.CustomTopAppBar


//@Preview
@Composable
fun TableScreen(
    modifier: Modifier = Modifier,
    menus: Pair<String, String>,
    sumOfMeals: SumOfMeals,
    isLoading: Boolean,
    meals: List<WholeDayCount>,
    onHomeIconClick: () -> Unit,
    onNavIconClick: () -> Unit,
    onEditIconClick: () -> Unit,
) {

    val view = LocalView.current

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Room-wise",
                onNavIconClick = onNavIconClick,
                actionIcon = {
                    IconButton(
                        onClick = {
                            onHomeIconClick()
                            view.playSoundEffect(SoundEffectConstants.NAVIGATION_UP)
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home"
                            )
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEditIconClick()
                    view.playSoundEffect(SoundEffectConstants.NAVIGATION_UP)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            )
        }
    ) { ip ->

        if (isLoading) Box(
            modifier = Modifier
                .padding(ip)
                .fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        else Box(
            modifier = modifier
                .fillMaxSize()
                .padding(ip)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .height(800.dp)
                    .padding(4.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                    ) {
                        TableCell(text = "", isHeader = true, 2f)
                        TableCell(text = "Morning( ${menus.first} )", isHeader = true, 5f)
                        TableCell(text = "Night( ${menus.second} )", isHeader = true, 5f)
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                    ) {
                        TableCell(text = "Room No", isHeader = true, 6f)
                        TableCell(text = "V", isHeader = true, 5f)
                        TableCell(text = "NV", isHeader = true, 5f)
                        TableCell(text = "H", isHeader = true, 5f)
                        TableCell(text = "V", isHeader = true, 5f)
                        TableCell(text = "NV", isHeader = true, 5f)
                        TableCell(text = "H", isHeader = true, 5f)
                    }
                }

                items(meals) {
                    Row(
                        modifier = Modifier
                    ) {
                        TableCell(text = it.roomNo.toString(), weight = 6f)
                        TableCell(text = it.dVeg, weight = 5f)
                        TableCell(text = it.dNonVeg, weight = 5f)
                        TableCell(text = it.dHalal, weight = 5f)
                        TableCell(text = it.nVeg, weight = 5f)
                        TableCell(text = it.nNonVeg, weight = 5f)
                        TableCell(text = it.nHalal, weight = 5f)
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                    ) {
                        TableCell(text = "", isHeader = true, 6f)
                        TableCell(text = "${sumOfMeals.dVeg}", isHeader = true, 5f)
                        TableCell(text = "${sumOfMeals.dNonVeg}", isHeader = true, 5f)
                        TableCell(text = "${sumOfMeals.dHalal}", isHeader = true, 5f)
                        TableCell(text = "${sumOfMeals.nVeg}", isHeader = true, 5f)
                        TableCell(text = "${sumOfMeals.nNonVeg}", isHeader = true, 5f)
                        TableCell(text = "${sumOfMeals.nHalal}", isHeader = true, 5f)
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                    ) {
                        TableCell(text = "Total", isHeader = true, 2f)
                        TableCell(
                            text = "${sumOfMeals.dVeg + sumOfMeals.dNonVeg + sumOfMeals.dHalal}",
                            isHeader = true,
                            5f
                        )
                        TableCell(
                            text = "${sumOfMeals.nVeg + sumOfMeals.nNonVeg + sumOfMeals.nHalal}",
                            isHeader = true,
                            5f
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(12.dp))
                }


                item {
                    Row(
                        modifier = Modifier
                    ) {
                        TableCell(text = "Room No", isHeader = true, 2f)
                        TableCell(text = "Morning( ${menus.first} )", isHeader = true, 5f)
                        TableCell(text = "Night( ${menus.second} )", isHeader = true, 5f)
                    }
                }

                items(meals) { count ->
//                    Row(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        TableCell(text = "Room No", isHeader = true, weight = 2f)
//                        TableCell(text = "Morning (${count.dHalal})", isHeader = true, weight = 5f)
//                        TableCell(text = "Night (${count.nHalal})", isHeader = true, weight = 5f)
//                    }
                    count.dayHalals.forEach { dayHalal ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TableCell(text = count.roomNo.toString(), isHeader = false, weight = 2f)
                            TableCell(text = dayHalal, isHeader = false, weight = 5f)
                            TableCell(text = "", isHeader = false, weight = 5f)
                        }
                    }
                    count.nightHalals.forEach { nightHalal ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TableCell(text = count.roomNo.toString(), isHeader = false, weight = 2f)
                            TableCell(text = "", isHeader = false, weight = 5f)
                            TableCell(text = nightHalal, isHeader = false, weight = 5f)
                        }
                    }
                }


            }
        }
    }


}


@Composable
fun RowScope.TableCell(text: String, isHeader: Boolean = false, weight: Float) {
    Text(
        text = text,
        modifier = Modifier
            .height(30.dp)
            .weight(weight)
            .border(0.5.dp, Color.Black)
            .background(if (isHeader) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
            .padding(vertical = 2.dp, horizontal = 2.dp),
        fontSize = 12.sp,
        lineHeight = 13.sp,
        textAlign = TextAlign.Center,
        color = if (isHeader) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
    )
}
