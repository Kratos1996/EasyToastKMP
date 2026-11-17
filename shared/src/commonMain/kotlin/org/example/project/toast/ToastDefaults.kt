import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import org.example.project.toast.ToastData

object ToastDefaults {
    fun success(message: String, description: String? = null) =
        ToastData(message, description, leading = { Text("✅", fontSize = 18.sp) })

    fun error(message: String, description: String? = null) =
        ToastData(message, description, leading = { Text("⚠️", fontSize = 18.sp) })

    fun info(message: String, description: String? = null) =
        ToastData(message, description, leading = { Text("ℹ️", fontSize = 18.sp) })

    fun warning(message: String, description: String? = null) =
        ToastData(message, description, leading = { Text("⚡", fontSize = 18.sp) })
}