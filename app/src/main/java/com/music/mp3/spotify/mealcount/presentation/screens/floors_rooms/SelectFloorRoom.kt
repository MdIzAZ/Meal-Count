package com.music.mp3.spotify.mealcount.presentation.screens.floors_rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.unit.dp
import com.music.mp3.spotify.mealcount.presentation.components.CounterButton
import com.music.mp3.spotify.mealcount.presentation.components.CustomTopAppBar
import com.music.mp3.spotify.mealcount.presentation.components.roomRangeSelection
import kotlin.math.max

@Composable
fun SelectFloorAndRooms(
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit ,
    onNextButtonClick: (Map<Int, Pair<Int, Int>>) -> Unit
) {
    Scaffold(
        topBar = { CustomTopAppBar(title = "Select No of Rooms", onNavIconClick = onBackPress) }
    ) { ip ->

        Column(
            modifier = Modifier
                .padding(ip)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {


            var counterValue by remember { mutableStateOf(0) }
            val rooms = remember { mutableStateOf<MutableMap<Int, Pair<Int, Int>>>(mutableMapOf()) }

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "No of Floors",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))

                CounterButton(
                    counterValue = counterValue.toString(),
                    onIncrease = {counterValue++},
                    onDecrease = { counterValue = max(counterValue-1, 0) },
                    onReset = {}
                )
            }

            Spacer(Modifier.height(5.dp))


            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(counterValue) {
                    Text(
                        text = "Floor: ${it + 1}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(5.dp))
                    val roomNumberRange = roomRangeSelection(floor = it + 1)
                    rooms.value[it + 1] = roomNumberRange
                }
            }

            Spacer(Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                ElevatedButton(
                    enabled = counterValue!= 0,
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = {
                        onNextButtonClick(rooms.value)
                    },
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