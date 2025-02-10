package com.music.mp3.spotify.mealcount.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.MealTime
import com.music.mp3.spotify.mealcount.presentation.screens.menu_selection.Menu

@Preview(showSystemUi = true)
@Composable
fun MenuSelectionSection(
    modifier: Modifier = Modifier,
    mealTime: MealTime = MealTime.NIGHT,
    menuItem: Menu? = Menu.FISH,
    onMenuIconClick: () -> Unit = {},
    onMenuSelect: (String) -> Unit = {}
) {

    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var boxRightEdge by remember { mutableStateOf(0f) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                val leftEdge = coordinates.positionInWindow().x
                val width = coordinates.size.width
                boxRightEdge = leftEdge + width
            }
    ) {

        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = when (mealTime) {
                    MealTime.DAY -> "Morning: $menuItem"
                    MealTime.NIGHT -> "Night: $menuItem"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    isExpanded = true
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                }
            )
        }
        DropdownMenu(
            modifier = modifier,
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            offset = DpOffset.Zero.copy(
                x = boxRightEdge.dp
            )
        ) {

            Menu.entries.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = it.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Normal
                        )
                    },
                    onClick = {
                        onMenuSelect(it.name)
                        isExpanded = false
                    }
                )

            }
        }
    }


}
