package com.music.mp3.spotify.mealcount.presentation.components

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.MealTime
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.Menu
import com.music.mp3.spotify.mealcount.presentation.screens.pager.COUNTER_TYPE
import com.music.mp3.spotify.mealcount.presentation.screens.pager.Halal
import com.music.mp3.spotify.mealcount.presentation.screens.pager.MealType
import com.music.mp3.spotify.mealcount.presentation.screens.pager.MealWithCounts
import kotlin.text.Typography.tm

//@Preview(showSystemUi = true)
@Composable
fun HorizontalPage(
    modifier: Modifier = Modifier,
    isLasPage: Boolean = false,
    roomNo: Int = 107,
    morningMealType: Menu = Menu.VEG,
    nightMealType: Menu = Menu.MEAT,
    onCounterValueChane: (roomNo: Int, type: COUNTER_TYPE, mealType: MealType, time: MealTime) -> Unit = { _, _, _, _ -> },
    dayCount: MealWithCounts = MealWithCounts(),
    nightCounts: MealWithCounts = MealWithCounts(),
    dayHalals: List<Halal>,
    nightHalals: List<Halal>,
    onHalalListChange: (names: List<Halal>, roomNo: Int, time: MealTime, changeType: COUNTER_TYPE) -> Unit,
    onNextBtnClick: () -> Unit = {}
) {

    var morning by remember { mutableStateOf(MealWithCounts()) }
    var night by remember { mutableStateOf(MealWithCounts()) }

//    var dayHalals by remember { mutableStateOf<List<String>>(emptyList()) }
//    var nightHalals by remember { mutableStateOf<List<String>>(emptyList()) }


    val currentDialogIsFor by remember {
        mutableStateOf(
            if (morning.halal > dayHalals.size) {
                MealTime.DAY
            } else {
                MealTime.NIGHT
            },
        )
    }

    Scaffold(
        floatingActionButton = {
            if (isLasPage) FloatingActionButton(
                onClick = { onNextBtnClick() },
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null
                    )
                }
            )
        }
    ) { ip ->


        if (morning.halal > dayHalals.size) {
            NameInputDialog(
                count = morning.halal - dayHalals.size,
                onConfirm = { list ->
                    val hl = list.map { Halal(name = it, roomNo = roomNo) }
                    onHalalListChange(hl, roomNo, MealTime.DAY, COUNTER_TYPE.INC)
//                    dayHalals += it
                }
            )
        }

        if (night.halal > nightHalals.size) {
            NameInputDialog(
                count = night.halal - nightHalals.size,
                onConfirm = { list ->
                    val hl = list.map { Halal(name = it, roomNo = roomNo) }
                    onHalalListChange(hl, roomNo, MealTime.NIGHT, COUNTER_TYPE.INC)
//                    nightHalals += it
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(ip)
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Text(text = roomNo.toString())

            Text(
                text = "Morning",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row {
                val tm = mealCounter(
                    modifier = Modifier.weight(1f),
                    menuType = morningMealType,
                    counts = dayCount,
                    onCounterValueChane = { type, mealType ->
                        onCounterValueChane(roomNo, type, mealType, MealTime.DAY)
                    }
                )
                morning = MealWithCounts(tm.second, tm.third, tm.first)


                LazyColumn {
                    items(dayHalals) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(it.name)
                            IconButton(
                                onClick = {
//                                    dayHalals -= it
                                    onHalalListChange(
                                        listOf(it),
                                        roomNo,
                                        MealTime.DAY,
                                        COUNTER_TYPE.DEC
                                    )
                                    onCounterValueChane(
                                        roomNo,
                                        COUNTER_TYPE.DEC,
                                        MealType.HALAL,
                                        MealTime.DAY
                                    )
                                },
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            )

                        }
                    }
                }
            }

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            Text(
                text = "Night",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row {
                val tn = mealCounter(
                    modifier = Modifier.weight(1f),
                    menuType = nightMealType,
                    counts = nightCounts,
                    onCounterValueChane = { type, mealType ->
                        onCounterValueChane(roomNo, type, mealType, MealTime.NIGHT)
                    }
                )
                night = MealWithCounts(tn.second, tn.third, tn.first)
                Log.d("izaz", "${night.halal}")


                LazyColumn {
                    items(nightHalals) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(it.name)
                            IconButton(
                                onClick = {
//                                    nightHalals -= it
                                    onHalalListChange(
                                        listOf(it),
                                        roomNo,
                                        MealTime.NIGHT,
                                        COUNTER_TYPE.DEC
                                    )
                                    onCounterValueChane(
                                        roomNo,
                                        COUNTER_TYPE.DEC,
                                        MealType.HALAL,
                                        MealTime.NIGHT
                                    )
                                },
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            )

                        }
                    }
                }
            }
        }
    }

}

