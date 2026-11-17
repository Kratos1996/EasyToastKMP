package org.example.project.toast

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

 enum class ArrowDirection { None, Up, Down }
enum class ToastPosition { Top, Center, Bottom }
enum class ToastAnimation { SlideFromTop, SlideFromBottom, Fade, Scale }

data class ToastAction(
    val label: String,
    val onClick: () -> Unit
)

data class ToastData(
    val message: String,
    val description: String? = null,
    val leading: (@Composable () -> Unit)? = null,     // icon/emoji slot
    val containerColor: Color? = null,   // ← was Color(...)
    val contentColor:   Color? = null,   // ← was Color.White
    val actionColor:    Color? = null,   // ← was Color(...)
    val durationMillis: Long = 3_000L,                  // 0 = sticky
    val position: ToastPosition = ToastPosition.Bottom,
    val animation: ToastAnimation = ToastAnimation.SlideFromBottom,
    val action: ToastAction? = null,
    val id: Long = 0L                                   // internal, used for keying
)

class ToastHostState {
    var currentToast by mutableStateOf<ToastData?>(null)
        private set

    private var counter = 0L

    fun showToast(toast: ToastData) {
        counter++
        currentToast = toast.copy(id = counter)
    }

    fun dismiss() { currentToast = null }
}

@Composable
fun rememberToastHostState(): ToastHostState = remember { ToastHostState() }

val LocalToastHostState = staticCompositionLocalOf<ToastHostState> {
    error("No ToastHostState provided — wrap your UI in ToastHost { ... }")
}