package com.popovanton0.blueprint

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.resources.Density
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.popovanton0.blueprint.dsl.Arrow
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.End
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.Start
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Bottom
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Top
import com.popovanton0.blueprint.dsl.SizeUnits
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
internal class BlueprintScreenshotTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5.copy(
            screenWidth = 1500,
            screenHeight = 1500,
            density = Density.XXXHIGH,
            softButtons = false,
        ),
    )

    @Test
    fun basicTest() = paparazzi.snapshotWrapper {
        Blueprint(blueprintBuilder = {
            widths {
                group {
                    "icon" lineTo "icon"
                    "text" lineTo "text"
                }
                group { "icon" lineTo "text" }
                group(Top) { "icon".center lineTo "text".center }
                group(Top) { "icon".center lineTo "text".center }
            }
            heights {
                group { "icon" lineTo "icon" }
                group { "icon" lineTo "icon" }
                group(End) { "text" lineTo "text" }
                group(End) { "text" lineTo "text" }
            }
        }) {
            TestButton()
        }
    }

    @Test
    fun customFontSizeAndColor() = paparazzi.snapshotWrapper {
        Blueprint(blueprintBuilder = {
            widths {
                group {
                    "icon" lineTo "icon"
                    "text" lineTo "text"
                }
                group { "icon" lineTo "text" }
                group(Top) { "icon".center lineTo "text".center }
                group(Top) { "icon".center lineTo "text".center }
            }
            heights {
                group { "icon" lineTo "icon" }
                group { "icon" lineTo "icon" }
                group(End) { "text" lineTo "text" }
                group(End) { "text" lineTo "text" }
            }
        }, fontSize = 14.sp, fontColor = Color.Blue) {
            TestButton()
        }
    }

    @Test
    fun emptyBlueprint() = paparazzi.snapshotWrapper {
        Blueprint(blueprintBuilder = {}) {
            TestButton()
        }
    }

    @Test
    fun `when blueprint is disabled, it is not shown`() = paparazzi.snapshotWrapper {
        Blueprint(
            enabled = false,
            blueprintBuilder = { widths { group(Top) { "icon" lineTo "icon" } } },
            content = { TestButton() }
        )
    }

    @Test
    fun `when specifying blueprintIds that are not referenced in the composable, no dimensions are shown`() =
        paparazzi.snapshotWrapper {
            Blueprint(blueprintBuilder = { widths { group { "lorem1" lineTo "lorem2" } } }) {
                TestButton()
            }
        }

    @Test
    fun `correct line widths and alignments`() = paparazzi.snapshotWrapper {
        Blueprint(
            lineStroke = BorderStroke(3.dp, Color.Red),
            blueprintBuilder = {
                widths {
                    group { "2".right lineTo "3".left }
                    group { "2".center lineTo "3".center }
                    group { "2".center lineTo "3".left }
                    group { "2".left lineTo "3".center }
                    group { "2".center lineTo "3".right }
                    group { "2" lineTo "3" }

                    group(Top) {
                        "1".left lineTo "1".center
                        "1".right lineTo "3".left
                        "3".center lineTo "3".right
                    }
                    group(Top) { "1".center lineTo "3".left }
                    group(Top) { "1".left lineTo "3".left }
                    group(Top) { "1".center lineTo "3".center }
                    group(Top) { "1" lineTo "3" }
                }
                heights {
                    group {
                        "1".top lineTo "1".center
                        "1".center lineTo "1".bottom
                        "1".bottom lineTo "2".top
                        "2".top lineTo "2".center
                    }
                    group { "1".center lineTo "2".center }
                    group { "1" lineTo "1" }

                    group(End) { "1".bottom lineTo "3".bottom }
                }
            }
        ) {
            TestUI()
        }
    }

    @Suppress("JUnitMalformedDeclaration")
    @Test
    fun `arrow customization`(
        @TestParameter("0", "15", "45", "90") angle: Float,
    ) {
        paparazzi.snapshotWrapper {
            Blueprint(
                lineStroke = BorderStroke(6.dp, Color.Red),
                arrow = Arrow(length = 6.dp * 3, angle = angle, roundCap = true),
                blueprintBuilder = {
                    widths {
                        group(Top) { "1" lineTo "3" }
                        group(Bottom) { "1" lineTo "3" }
                    }
                    heights {
                        group(Start) { "1".bottom lineTo "3".bottom }
                        group(End) { "1".bottom lineTo "3".bottom }
                    }
                },
                content = { TestUI() }
            )
        }
    }

    @Test
    fun `not enough space to draw`() = paparazzi.snapshotWrapper {
        Box(
            modifier = Modifier
                .requiredSize(50.dp)
                .border(1.dp, Color.Magenta)
        ) {
            Blueprint(blueprintBuilder = {
                widths { group(Top) { "1" lineTo "3" } }
                heights { group { "1".bottom lineTo "3".bottom } }
            }, content = { TestUI() })
        }
    }

    @Test
    fun `almost none space to draw`() = paparazzi.snapshotWrapper {
        Box(
            modifier = Modifier
                .requiredSize(10.dp)
                .border(1.dp, Color.Magenta)
        ) {
            Blueprint(blueprintBuilder = {
                widths {
                    group(Top) { "1" lineTo "3" }
                    group { "2" lineTo "3" }
                }
                heights { group { "1".bottom lineTo "3".bottom } }
            }, content = { TestUI() })
        }
    }

    @Test
    fun `fractional dp values rendering`() = paparazzi.snapshotWrapper {
        Blueprint(blueprintBuilder = {
            widths { group { "1" lineTo "1" } }
            heights { group { "1" lineTo "1" } }
        }, precision = 3) {
            Box(
                modifier = Modifier
                    .size(41.5.dp, 29.5.dp)
                    .background(Color.Green)
                    .blueprintId("1")
            )
        }
    }

    @Composable
    private fun LoremText(modifier: Modifier = Modifier, words: Int = 10) =
        Text(
            modifier = modifier, text = LoremIpsum(words).values.joinToString(separator = " ")
        )

    @Test
    fun `padding not applied`() = paparazzi.snapshotWrapper {
        Row(modifier = Modifier.fillMaxWidth()) {
            LoremText(modifier = Modifier.weight(1f), words = 20)
            Column(modifier = Modifier.weight(1f)) {
                LoremText()
                Blueprint(
                    blueprintBuilder = {
                        widths {
                            repeat(2) { group { "icon" lineTo "text" } }
                            repeat(2) { group(Top) { "icon" lineTo "text" } }
                        }
                        heights {
                            repeat(2) { group { "icon" lineTo "icon" } }
                            repeat(2) { group(End) { "icon" lineTo "icon" } }
                        }
                    },
                    applyPadding = false,
                    fontColor = Color.Blue,
                ) {
                    TestButton()
                }
                LoremText()
            }
            LoremText(modifier = Modifier.weight(1f), words = 20)
        }
    }

    @Test
    fun `size labels`() = paparazzi.snapshotWrapper {
        Blueprint(blueprintBuilder = {
            widths { group { "1" lineTo "1" } }
            heights { group { "1" lineTo "1" } }
        }, content = { TestUI(showSizeLabels = true) })
    }

    @Test
    fun `no blueprint if globally disabled`() {
        blueprintEnabled = false
        paparazzi.snapshotWrapper {
            Blueprint(blueprintBuilder = {
                widths { group { "1" lineTo "1" } }
                heights { group { "1" lineTo "1" } }
            }, content = { TestUI() })
        }
        blueprintEnabled = true
    }

    @Test
    fun `when specifying blueprintIds that are then removed from the composition, dimensions are shown and then hidden`() {
        var showGreen by mutableStateOf(true)
        val content = @Composable {
            Blueprint(blueprintBuilder = {
                widths {
                    group(Top) { "green" lineTo "green" }
                    group { "blue" lineTo "blue" }
                }
            }) {
                Column {
                    if (showGreen) Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color.Green)
                            .blueprintId("green")
                    )
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.Blue)
                            .blueprintId("blue")
                    )
                }
            }
        }
        paparazzi.snapshotWrapper(name = "(with green)") { content() }
        showGreen = false
        paparazzi.snapshotWrapper(name = "(without green)") { content() }
    }

    @Test
    fun `reacts to blueprintBuilder update`() {
        var showGreenSize by mutableStateOf(true)
        val content = @Composable {
            Blueprint(blueprintBuilder = {
                widths {
                    group { "2" lineTo "2" }
                    if (showGreenSize) group { "1" lineTo "1" }
                }
            }) {
                TestUI()
            }
        }
        paparazzi.snapshotWrapper(name = "(with green)") { content() }
        showGreenSize = false
        paparazzi.snapshotWrapper(name = "(without green)") { content() }
    }

    private fun Paparazzi.snapshotWrapper(
        name: String? = null,
        composable: @Composable () -> Unit
    ): Unit = snapshot(name) {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                Box(modifier = Modifier.padding(8.dp)) {
                    composable()
                }
            }
        }
    }

    @Composable
    private fun TestUI(showSizeLabels: Boolean = false) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Green)
                    .blueprintId("1", if (showSizeLabels) SizeUnits.Dp else null)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Blue)
                        .blueprintId("2", if (showSizeLabels) SizeUnits.Dp else null)
                )
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Yellow)
                        .blueprintId("3", if (showSizeLabels) SizeUnits.Dp else null)
                )
            }
        }
    }

    @Composable
    private fun TestButton() = Button(onClick = { }) {
        Icon(
            modifier = Modifier
                .size(20.dp)
                .blueprintId("icon"),
            painter = rememberVectorPainter(Icons.Default.Home),
            contentDescription = null
        )
        Text(
            modifier = Modifier.blueprintId("text"),
            text = "Lorem",
        )
    }
}