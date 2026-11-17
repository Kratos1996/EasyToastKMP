package org.example.project.toast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DemoScreen() {
    val toast = LocalToastHostState.current
    Surface(
        modifier = Modifier.fillMaxSize(),

        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {


    Column(
        Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button({ toast.showToast(ToastData("Hello from KMP toast!")) }) {
            Text("Default toast")
        }

        Button({ toast.showToast(ToastDefaults.success("Saved successfully")) }) {
            Text("Success")
        }

        Button({ toast.showToast(ToastDefaults.error("Something went wrong")) }) {
            Text("Error")
        }

        Button({
            toast.showToast(
                ToastData(
                    message = "New message",
                    description = "Tap UNDO to revert",
                    leading = { Text("💬", fontSize = 18.sp) },
                    action = ToastAction("UNDO") { },
                    position = ToastPosition.Top,
                    animation = ToastAnimation.SlideFromTop
                )
            )
        }) { Text("Top + Action button") }



        Button({
            toast.showToast(
                ToastData(
                    message = "Fading away…",
                    animation = ToastAnimation.Fade,
                    position = ToastPosition.Bottom
                )
            )
        }) { Text("Fade") }

        Button({
            toast.showToast(
                ToastData(
                    message = "Sticky toast",
                    description = "Tap dismiss",
                    durationMillis = 0,
                    action = ToastAction("Dismiss") { }
                )
            )
        }) { Text("Sticky (no auto‑dismiss)") }
    }
}
}