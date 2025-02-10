package com.music.mp3.spotify.mealcount.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.mp3.spotify.mealcount.R

//@Preview(showSystemUi = true)
@Composable
fun MealSummaryCard(
    modifier: Modifier = Modifier,
    countedBy: Int = 207,
    countedAt: String = "22/10/2025",
    onCardClick: () -> Unit,
    onDelete: (String) -> Unit
) {
    Box {
        ElevatedCard(
            modifier = Modifier
                .padding(4.dp)
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(0.dp, 20.dp, 0.dp, 20.dp)
                )
                .size(190.dp, 200.dp)
                .clickable { onCardClick() },
            elevation = CardDefaults.elevatedCardElevation(90.dp),
            shape = RoundedCornerShape(0.dp, 20.dp, 0.dp, 20.dp),
            content = {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.7f),
                    contentScale = ContentScale.FillBounds,
                    painter = painterResource(R.drawable.food),
                    contentDescription = null
                )
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                Text(
//                    text = "Counted By: $countedBy",
//                    style = MaterialTheme.typography.bodyLarge,
//                    fontWeight = FontWeight.SemiBold
//                )

                    Text(
                        text = "Date: \n$countedAt",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors()
                            .copy(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        onClick = { onDelete(countedAt) },
                        shape = RoundedCornerShape(0.dp, 16.dp, 0.dp, 16.dp),
                        content = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                contentDescription = null
                            )
                        }
                    )


                }
            }
        )
    }

}