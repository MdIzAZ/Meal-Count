package com.music.mp3.spotify.mealcount.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun roomRangeSelection(modifier: Modifier = Modifier, floor: Int): Pair<Int, Int> {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(60.dp))
            .background(MaterialTheme.colorScheme.tertiary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var low by remember { mutableStateOf(floor * 100 + 1) }
        var high by remember { mutableStateOf(floor * 100 + 11) }

        ControlledArrowSection(
            icon = Icons.Default.ArrowDropUp,
            lowType = ArrowType.LOW_INC,
            highType = ArrowType.HIGH_INC,
            onValueChange = {
                when (it) {
                    ArrowType.LOW_INC -> {
                        low++
                    }

                    ArrowType.HIGH_INC -> {
                        high++
                    }

                    else -> {}
                }
            }
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .width(500.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CountHolderBox(count = low, onValueChange = { low = it })
            CountHolderBox(count = high, onValueChange = { high = it })

        }

        ControlledArrowSection(
            icon = Icons.Default.ArrowDropDown,
            lowType = ArrowType.LOW_DEC,
            highType = ArrowType.HIGH_DEC,
            onValueChange = {
                when (it) {
                    ArrowType.LOW_DEC -> {
                        low--
                    }

                    ArrowType.HIGH_DEC -> {
                        high--
                    }

                    else -> {}
                }
            }
        )

        if (low< high)return Pair(low, high)

    }

    return Pair(0, 0)
}


@Composable
fun CountHolderBox(modifier: Modifier = Modifier, count: Int, onValueChange: (Int) -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(horizontal = 12.dp)
            .shadow(8.dp, shape = CircleShape)
            .size(64.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiaryContainer),
    ) {
        Text(
            text = count.toString(),
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )

//        TextField(
//            modifier = Modifier.clip(CircleShape).fillMaxSize(),
//            value = "$count",
//            singleLine = true,
//            onValueChange = { onValueChange(if (it =="") 0 else it.toInt()) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        )
    }
}

@Composable
fun ControlledArrowSection(
    modifier: Modifier = Modifier,
    lowType: ArrowType,
    highType: ArrowType,
    icon: ImageVector,
    onValueChange: (ArrowType) -> Unit
) {

    Row(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .width(500.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            modifier = modifier.size(80.dp),
            onClick = { onValueChange(lowType) },
            content = {
                Icon(
                    modifier = modifier.fillMaxSize(),
                    imageVector = icon,
                    contentDescription = null
                )
            }
        )

        IconButton(
            modifier = modifier.size(80.dp),
            onClick = { onValueChange(highType) },
            content = {
                Icon(
                    modifier = modifier.fillMaxSize(),
                    imageVector = icon,
                    contentDescription = null
                )
            }
        )


    }


}

enum class ArrowType {
    LOW_INC, HIGH_INC, LOW_DEC, HIGH_DEC
}




