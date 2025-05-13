package com.popovanton0.blueprint.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.popovanton0.blueprint.Blueprint
import com.popovanton0.blueprint.blueprintId
import com.popovanton0.blueprint.dsl.Position
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Lab()
                    return@Surface
                    val case = remember { RecreateLayersTestCase() }
                    case.Content()
                    LaunchedEffect(Unit) {
                        if (false)repeat(1000000) {
                            case.toggleState()
                            delay(25)
                        }
                    }
                }
            }
        }
    }
}

private class RecreateLayersTestCase {

    private val range = 1f..50f
    private var size by mutableFloatStateOf(range.start)
    private var increase = true
    private val step = 0.5f

    fun toggleState() {
        if (size >= range.endInclusive) increase = false
        else if (size <= range.start) increase = true
        if (increase) size += step else size -= step
        println("size = $size")
    }

    @Composable
    fun Content() = Box {
        Blueprint(
            modifier = Modifier.padding(12.dp),
//            fontColor = Color.Blue,
//            fontSize = 20.sp,
            blueprintBuilder = {
                widths {
                    group {
                        "icon" lineTo "icon"
                        "box" lineTo "box"
                    }
                    group { "icon" lineTo "text" }
                    group(Position.Vertical.Top) { "icon".center lineTo "box".center }
                    group(Position.Vertical.Top) { "icon".center lineTo "box".center }
                }
                if (true) heights {
                    group { "icon" lineTo "icon" }
                    group { "icon" lineTo "icon" }
                    group(Position.Horizontal.End) { "box" lineTo "box" }
                    group(Position.Horizontal.End) { "box" lineTo "box" }
                }
            }
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (size < 25f) Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .blueprintId("icon"),
                    painter = rememberVectorPainter(Icons.Default.Home),
                    contentDescription = null
                )
                Box(
                    modifier = Modifier
                        .size(size.dp)
                        .background(Color.Blue)
                        .blueprintId("box"),
                )
            }
        }
    }
}