package com.music.mp3.spotify.mealcount.presentation.screens.pager

import android.view.SoundEffectConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.music.mp3.spotify.mealcount.presentation.components.CustomAlertDialog
import com.music.mp3.spotify.mealcount.presentation.components.HorizontalPage
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.MealTime
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.Menu
import kotlinx.coroutines.launch

@Composable
fun PagerScreen(
    modifier: Modifier = Modifier,
    morning: String,
    night: String,
    rooms: Map<Int, Pair<Int, Int>>,
    states: PagerScreenState,
    onBackPress: () -> Unit,
    onCounterValueChane: (roomNo: Int, type: COUNTER_TYPE, mealType: MealType, time: MealTime) -> Unit,
    onHalalListChange: (names: List<Halal>, roomNo: Int, time: MealTime, changeType: COUNTER_TYPE) -> Unit,
    onNextBtnClick: () -> Unit
) {

    val view = LocalView.current

    var shouldShowDialog by remember { mutableStateOf(false) }

    BackHandler {
        shouldShowDialog = true
    }

    if (shouldShowDialog) {
        CustomAlertDialog(
            title = "Discard Changes?",
            message = "You have unsaved changes. If you go back now, any new records or changes will not be saved. Do you want to proceed?",
            onDismissRequest = {
                shouldShowDialog = false
            },
            onConfirmBtnClick = {
                shouldShowDialog = false
                onBackPress()
            }
        )
    }



    Scaffold { ip ->
        Column(
            modifier = modifier
                .padding(ip)
                .fillMaxSize()
        ) {

            val roomsAndFloors = rooms.entries.sortedBy { it.key }.map { it.value }
            var floorIndex by remember { mutableIntStateOf(0) }
            var roomIndex by remember { mutableIntStateOf(0) }

            val scope = rememberCoroutineScope()

            val pagerState =
                rememberPagerState(pageCount = { roomsAndFloors[floorIndex].second - roomsAndFloors[floorIndex].first + 1 })


            LaunchedEffect(pagerState.currentPage) {
                roomIndex = pagerState.currentPage
            }


            //floor numbers
            TabRow(
                selectedTabIndex = floorIndex,
                tabs = {
                    repeat(roomsAndFloors.size) {
                        Tab(
                            selected = it == floorIndex,
                            onClick = {
                                floorIndex = it
                                roomIndex = 0
                                scope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                                view.playSoundEffect(SoundEffectConstants.NAVIGATION_UP)
                            },
                            text = {
                                Text(text = "${it + 1}")
                            }
                        )
                    }
                }
            )

            // room numbers
            ScrollableTabRow(
                selectedTabIndex = roomIndex,
                tabs = {
                    val low = roomsAndFloors[floorIndex].first
                    val high = roomsAndFloors[floorIndex].second

                    repeat(high - low + 1) {
                        Tab(
                            selected = it == roomIndex,
                            onClick = {
                                roomIndex = it
                                scope.launch {
                                    pagerState.animateScrollToPage(it)
                                }
                                view.playSoundEffect(SoundEffectConstants.NAVIGATION_UP)
                            },
                            text = {
                                Text(text = "${low + it}")
                            }
                        )
                    }
                }
            )

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                pageContent = {

                    val roomNo = roomsAndFloors[floorIndex].first + it

                    HorizontalPage(
                        isLasPage = (floorIndex + 1 == roomsAndFloors.size) && ((it + 1) == pagerState.pageCount),
                        roomNo = roomNo,
                        morningMealType = Menu.valueOf(morning),
                        nightMealType = Menu.valueOf(night),
                        onNextBtnClick = onNextBtnClick,
                        dayCount = states.morning[roomNo] ?: MealWithCounts(),
                        nightCounts = states.night[roomNo] ?: MealWithCounts(),
                        onCounterValueChane = onCounterValueChane,
                        dayHalals = states.dHalal.filter { h -> h.roomNo == roomNo },
                        nightHalals = states.nHalal.filter { h -> h.roomNo == roomNo },
                        onHalalListChange = onHalalListChange
                    )

                }
            )

        }
    }
}


enum class COUNTER_TYPE {
    INC, DEC
}

