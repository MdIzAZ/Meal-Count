package com.music.mp3.spotify.mealcount.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.sign

//@Preview(showSystemUi = true)
@Composable
fun CounterButton(
    counterValue: String,
    modifier: Modifier = Modifier,
    onIncrease:()->Unit,
    onDecrease:()->Unit,
    onReset:()->Unit,
) {

    val thumbOffsetX = remember { Animatable(0f) }
    val thumbOffsetY = remember { Animatable(0f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(200.dp)
            .height(80.dp)
    ) {


        ButtonContainer(
            modifier = Modifier,
            thumbOffsetX = thumbOffsetX.value,
            onValueDecreaseClick = { onDecrease() },
            onValueIncreaseClick = { onIncrease() },
            onValueClearClick = { onReset() },
        )

        DraggableThumbButton(
            value = counterValue.toString(),
            onClick = { onIncrease() },
            modifier = Modifier.align(Alignment.Center),
            onValueDecreaseClick = { onDecrease() },
            onValueIncreaseClick = { onIncrease() },
            thumbOffsetX = thumbOffsetX,
            thumbOffsetY = thumbOffsetY
        )
    }
}

private const val ICON_BUTTON_ALPHA_INITIAL = 0.6f
private const val CONTAINER_BACKGROUND_ALPHA_INITIAL = 0.6f
private const val CONTAINER_OFFSET_FACTOR = 0.1f
private const val DRAG_LIMIT_HORIZONTAL_DP = 60
private const val START_DRAG_THRESHOLD_DP = 2

@Composable
private fun ButtonContainer(
    modifier: Modifier = Modifier,
    clearButtonVisible: Boolean = false,
    thumbOffsetX: Float,
    onValueDecreaseClick: () -> Unit,
    onValueIncreaseClick: () -> Unit,
    onValueClearClick: () -> Unit,
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .offset {
                IntOffset(
                    (thumbOffsetX * CONTAINER_OFFSET_FACTOR).toInt(),
                    0
                )
            }
            .fillMaxSize()
            .clip(RoundedCornerShape(64.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = CONTAINER_BACKGROUND_ALPHA_INITIAL))
            .padding(horizontal = 8.dp)
    ) {
        // decrease button
        IconControlButton(
            icon = Icons.Outlined.Remove,
            contentDescription = "Decrease count",
            onClick = onValueDecreaseClick,
            tintColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                alpha = if (thumbOffsetX < 0) {
                    (thumbOffsetX.absoluteValue / 36).coerceIn(
                        ICON_BUTTON_ALPHA_INITIAL,
                        1f
                    )
                } else {
                    ICON_BUTTON_ALPHA_INITIAL
                }
            )
        )

        // clear button
        if (clearButtonVisible) {
            IconControlButton(
                icon = Icons.Outlined.Clear,
                contentDescription = "Clear count",
                onClick = onValueClearClick,
                tintColor = Color.White.copy(alpha = ICON_BUTTON_ALPHA_INITIAL)
            )
        }

        // increase button
        IconControlButton(
            icon = Icons.Outlined.Add,
            contentDescription = "Increase count",
            onClick = onValueIncreaseClick,
            tintColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                alpha = if (thumbOffsetX > 0) {
                    (thumbOffsetX.absoluteValue / 36).coerceIn(
                        ICON_BUTTON_ALPHA_INITIAL,
                        1f
                    )
                } else {
                    ICON_BUTTON_ALPHA_INITIAL
                }
            )
        )
    }
}

@Composable
private fun IconControlButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tintColor: Color = Color.White,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tintColor,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun DraggableThumbButton(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    thumbOffsetX: Animatable<Float, AnimationVector1D>,
    thumbOffsetY: Animatable<Float, AnimationVector1D>,
    onValueDecreaseClick: () -> Unit,
    onValueIncreaseClick: () -> Unit,
) {

    val scope = rememberCoroutineScope()
    val dragLimitHorizontalPx = DRAG_LIMIT_HORIZONTAL_DP.dp.dpToPx()
    val startDragThreshold = START_DRAG_THRESHOLD_DP.dp.dpToPx()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .offset {
                IntOffset(x = thumbOffsetX.value.toInt(), y = 0)
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    do {
                        val event = awaitPointerEvent()
                        event.changes.forEach { pointerInputChange ->
                            scope.launch {
                                val targetValue =
                                    thumbOffsetX.value + pointerInputChange.positionChange().x
                                val targetValueWithinBounds = targetValue.coerceIn(
                                    -dragLimitHorizontalPx,
                                    dragLimitHorizontalPx
                                )
                                thumbOffsetX.snapTo(targetValueWithinBounds)
                            }
                        }
                    } while (event.changes.any { it.pressed })


                    if (thumbOffsetX.value.absoluteValue >= dragLimitHorizontalPx) {
                        if (thumbOffsetX.value.sign > 0) {
                            onValueIncreaseClick()
                        } else {
                            onValueDecreaseClick()
                        }
                    }

                    scope.launch {
                        if (thumbOffsetX.value != 0f) {
                            thumbOffsetX.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = StiffnessLow
                                )
                            )
                        }
                    }
                }
            }
            .shadow(8.dp, shape = CircleShape)
            .size(64.dp)
            .clip(CircleShape)
            .clickable {
                if (thumbOffsetX.value.absoluteValue <= startDragThreshold) {
                    onClick()
                }
            }
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

