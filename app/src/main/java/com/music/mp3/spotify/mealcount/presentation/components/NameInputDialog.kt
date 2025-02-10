package com.music.mp3.spotify.mealcount.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun NameInputDialog(
    modifier: Modifier = Modifier,
    count: Int,
    onConfirm: (List<String>) -> Unit
) {

    var openAlertDialog by remember { mutableStateOf(false) }
    val names = remember { mutableStateListOf<String>().apply { addAll(List(count) { "" }) } }

    LaunchedEffect(count) {
        if (count > names.size) {
            repeat(count - names.size) {
                names.add("")
            }
        } else if (count < names.size) {
            repeat(names.size - count) {
                names.removeAt(names.size - 1)
            }
        }
    }

    Log.d("izaz", "${names.size}")


    AlertDialog(
        title = {
            Text(text = "Enter Names")
        },
        onDismissRequest = {
            openAlertDialog = false
        },

        text = {
            LazyColumn {
                itemsIndexed(names) {idx, item ->
                    TextField(
                        value = item,
                        onValueChange = { names[idx] = it },
                        placeholder = { Text(text = "Name: ${idx + 1}") }
                    )
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(names)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openAlertDialog = false
                }
            ) {
                Text("Cancel")
            }
        }
    )

}