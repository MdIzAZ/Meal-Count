package com.music.mp3.spotify.mealcount.presentation.components

import android.view.SoundEffectConstants
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView

@Composable
fun CustomAlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmBtnClick: () -> Unit
) {

    val view = LocalView.current

    AlertDialog(
        icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete") },
        title = { Text(text = title) },
        text = {
            Text(text = message)
        },
        onDismissRequest = {
            onDismissRequest()
            view.playSoundEffect(SoundEffectConstants.CLICK)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmBtnClick()
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                },
                colors = ButtonDefaults.textButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                },
                colors = ButtonDefaults.textButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text("Dismiss")
            }
        }
    )
}