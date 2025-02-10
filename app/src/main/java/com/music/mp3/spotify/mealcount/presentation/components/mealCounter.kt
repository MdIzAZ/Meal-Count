package com.music.mp3.spotify.mealcount.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.MealTime
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.Menu
import com.music.mp3.spotify.mealcount.presentation.screens.pager.COUNTER_TYPE
import com.music.mp3.spotify.mealcount.presentation.screens.pager.MealType
import com.music.mp3.spotify.mealcount.presentation.screens.pager.MealWithCounts

@Composable
fun mealCounter(
    modifier: Modifier = Modifier,
    menuType: Menu = Menu.MEAT,
    onCounterValueChane: (type: COUNTER_TYPE, mealType: MealType) -> Unit,
    counts: MealWithCounts,
): Triple<Int, Int, Int> {


    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (menuType) {
            Menu.VEG -> {
                Text("V: ")
                CounterButton(
                    counterValue = counts.veg.toString(),
                    onIncrease = {
                        onCounterValueChane(COUNTER_TYPE.INC, MealType.VEG)
                    },
                    onDecrease = { onCounterValueChane(COUNTER_TYPE.DEC, MealType.VEG) },
                    onReset = {}
                )
            }

            Menu.EGG -> {
                Text("V: ")
                CounterButton(counterValue = counts.veg.toString(),
                    onIncrease = { onCounterValueChane(COUNTER_TYPE.INC, MealType.VEG) },
                    onDecrease = { onCounterValueChane(COUNTER_TYPE.DEC, MealType.VEG) },
                    onReset = {}
                )
                Text("NV: ")
                CounterButton(counterValue = counts.nonVeg.toString(),
                    onIncrease = { onCounterValueChane(COUNTER_TYPE.INC, MealType.NON_VEG) },
                    onDecrease = { onCounterValueChane(COUNTER_TYPE.DEC, MealType.NON_VEG) },
                    onReset = {}
                )
            }

            Menu.FISH -> {
                Text("V: ")
                CounterButton(counterValue = counts.veg.toString(),
                    onIncrease = { onCounterValueChane(COUNTER_TYPE.INC, MealType.VEG) },
                    onDecrease = { onCounterValueChane(COUNTER_TYPE.DEC, MealType.VEG) },
                    onReset = {}
                )
                Text("NV: ")
                CounterButton(counterValue = counts.nonVeg.toString(),
                    onIncrease = { onCounterValueChane(COUNTER_TYPE.INC, MealType.NON_VEG) },
                    onDecrease = { onCounterValueChane(COUNTER_TYPE.DEC, MealType.NON_VEG) },
                    onReset = {}
                )
            }

            Menu.MEAT -> {
                Text("V: ")
                CounterButton(counterValue = counts.veg.toString(),
                    onIncrease = { onCounterValueChane(COUNTER_TYPE.INC, MealType.VEG) },
                    onDecrease = { onCounterValueChane(COUNTER_TYPE.DEC, MealType.VEG) },
                    onReset = {}
                )
                Text("NV: ")
                CounterButton(counterValue = counts.nonVeg.toString(),
                    onIncrease = { onCounterValueChane(COUNTER_TYPE.INC, MealType.NON_VEG) },
                    onDecrease = { onCounterValueChane(COUNTER_TYPE.DEC, MealType.NON_VEG) },
                    onReset = {}
                )
                Text("H: ")
                CounterButton(counterValue = counts.halal.toString(),
                    onIncrease = { onCounterValueChane(COUNTER_TYPE.INC, MealType.HALAL) },
                    onDecrease = { onCounterValueChane(COUNTER_TYPE.DEC, MealType.HALAL) },
                    onReset = {}
                )
            }
        }
    }
    return Triple(counts.halal, counts.veg, counts.nonVeg)
}




