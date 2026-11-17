package org.example.project.toast

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ToastHost(
    state: ToastHostState = rememberToastHostState(),
    events: Flow<ToastData>? = null,
    content: @Composable () -> Unit
) {
    if (events != null) {
        LaunchedEffect(events) { events.collect { state.showToast(it) } }
    }

    CompositionLocalProvider(LocalToastHostState provides state) {
        Box(Modifier.fillMaxSize()) {
            content()
            ToastOverlay(state)
        }
    }
}

@Composable
private fun ToastOverlay(state: ToastHostState) {
    val toast = state.currentToast
    var lastToast by remember { mutableStateOf<ToastData?>(null) }

    LaunchedEffect(toast) { if (toast != null) lastToast = toast }

    LaunchedEffect(toast?.id) {
        val current = toast ?: return@LaunchedEffect
        if (current.durationMillis > 0) {
            delay(current.durationMillis.milliseconds)
            if (state.currentToast?.id == current.id) state.dismiss()
        }
    }

    val display = toast ?: lastToast ?: return

    Box(
        Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        AnimatedVisibility(
            visible = toast != null,
            enter = display.animation.enter(),
            exit = display.animation.exit(),
            modifier = Modifier.align(display.position.alignment())
        ) {

            ToastCard(display) { state.dismiss() }
        }
    }
}



private fun ToastAnimation.enter(): EnterTransition = when (this) {
    ToastAnimation.SlideFromTop    -> slideInVertically { -it } + fadeIn()
    ToastAnimation.SlideFromBottom -> slideInVertically { it } + fadeIn()
    ToastAnimation.Fade            -> fadeIn()
    ToastAnimation.Scale           -> scaleIn(initialScale = 0.85f) + fadeIn()
}

private fun ToastAnimation.exit(): ExitTransition = when (this) {
    ToastAnimation.SlideFromTop    -> slideOutVertically { -it } + fadeOut()
    ToastAnimation.SlideFromBottom -> slideOutVertically { it } + fadeOut()
    ToastAnimation.Fade            -> fadeOut()
    ToastAnimation.Scale           -> scaleOut(targetScale = 0.85f) + fadeOut()
}

private fun ToastPosition.alignment(): Alignment = when (this) {
    ToastPosition.Top    -> Alignment.TopCenter
    ToastPosition.Center -> Alignment.Center
    ToastPosition.Bottom -> Alignment.BottomCenter
}