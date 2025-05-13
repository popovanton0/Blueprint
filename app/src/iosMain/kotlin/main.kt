import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.window.ComposeUIViewController
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import platform.UIKit.UIViewController
import com.popovanton0.blueprint.app.Lab

fun MainViewController(): UIViewController = ComposeUIViewController {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Lab()
        }
    }
}