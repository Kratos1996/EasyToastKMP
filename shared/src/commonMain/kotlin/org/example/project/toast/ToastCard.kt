package org.example.project.toast

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

private enum class SwipeDirection { Up, Down }


@Composable
internal fun ToastCard(toast: ToastData, onDismiss: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    val containerColor = toast.containerColor ?: cs.inverseSurface
    val contentColor   = toast.contentColor   ?: cs.inverseOnSurface
    val actionColor    = toast.actionColor    ?: cs.inversePrimary

    // ── Swipe direction ──
    val swipeDirection: SwipeDirection? = when (toast.animation) {
        ToastAnimation.SlideFromTop    -> SwipeDirection.Up
        ToastAnimation.SlideFromBottom -> SwipeDirection.Down
        ToastAnimation.Fade            -> null
        ToastAnimation.Scale           -> null
    }

    // ── Arrow direction ──
    val arrowDir: ArrowDirection = when (toast.animation) {
        ToastAnimation.SlideFromTop    -> ArrowDirection.Up
        ToastAnimation.SlideFromBottom -> ArrowDirection.Down
        else                           -> ArrowDirection.None
    }

    val density = LocalDensity.current
    val scope   = rememberCoroutineScope()

    var dragOffset by remember(toast.id) { mutableFloatStateOf(0f) }

    val thresholdPx    = with(density) { 12.dp.toPx() }
    val fadeDistancePx = with(density) { 200.dp.toPx() }
    val dragAlpha      = (1f - abs(dragOffset) / fadeDistancePx).coerceIn(0f, 1f)

    val swipeModifier: Modifier = if (swipeDirection == null) {
        Modifier
    } else {
        Modifier
            .offset { IntOffset(0, dragOffset.roundToInt()) }
            .graphicsLayer { alpha = dragAlpha }
            .pointerInput(toast.id, swipeDirection) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, amount ->
                        change.consume()
                        dragOffset = when (swipeDirection) {
                            SwipeDirection.Up   -> min(dragOffset + amount, 0f)
                            SwipeDirection.Down -> max(dragOffset + amount, 0f)
                        }
                    },
                    onDragEnd = {
                        val passed  = abs(dragOffset) > thresholdPx
                        val correct = when (swipeDirection) {
                            SwipeDirection.Up   -> dragOffset < 0
                            SwipeDirection.Down -> dragOffset > 0
                        }

                        if (passed && correct) {
                            val target = if (dragOffset < 0)
                                dragOffset - size.height.toFloat()
                            else
                                dragOffset + size.height.toFloat()

                            scope.launch {
                                Animatable(dragOffset).animateTo(target, tween(200)) {
                                    dragOffset = value
                                }
                                onDismiss()
                            }
                        } else {
                            scope.launch {
                                Animatable(dragOffset).animateTo(0f, spring()) {
                                    dragOffset = value
                                }
                            }
                        }
                    },
                )
            }
    }

    // ── Card ──
    Surface(
        shape           = RoundedCornerShape(14.dp),
        color           = containerColor,
        contentColor    = contentColor,
        shadowElevation = 8.dp,
        modifier        = Modifier.widthIn(max = 460.dp).then(swipeModifier)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Main content row
            Row(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                toast.leading?.let {
                    Box(Modifier.padding(end = 12.dp)) { it() }
                }

                Column(Modifier.weight(1f, fill = false)) {
                    Text(
                        text = toast.message,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColor,
                    )
                    toast.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = contentColor.copy(alpha = 0.85f),
                        )
                    }
                }

                toast.action?.let { action ->
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = { action.onClick(); onDismiss() }) {
                        Text(
                            action.label,
                            color = actionColor,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            // Arrow Indicator
            if (arrowDir != ArrowDirection.None) {
                Icon(
                    imageVector = if (arrowDir == ArrowDirection.Up)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = "Swipe to dismiss",
                    tint = contentColor.copy(alpha = 0.6f),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .size(24.dp)
                )
            }
        }
    }
}