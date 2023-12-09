@file:OptIn(ExperimentalBlueprintApi::class, ExperimentalBlueprintApi::class)

package com.popovanton0.blueprint.app

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.popovanton0.blueprint.Blueprint
import com.popovanton0.blueprint.ExperimentalBlueprintApi
import com.popovanton0.blueprint.app.ui.theme.Pipette
import com.popovanton0.blueprint.blueprintId
import com.popovanton0.blueprint.dsl.Arrow
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.End
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.Start
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Bottom
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Top
import kotlinx.coroutines.delay

@Preview(fontScale = 1.0f)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun Lab() = Preview {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
//        PetriDish()
        ButtonDemo2()
    }
}

@Composable
internal fun Preview(content: @Composable () -> Unit) =
    MaterialTheme(if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        Surface(content = content)
    }

@Preview
@Composable
fun Sample() = Preview {
    LazyRow {
        items(4) {
            Button(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
                Text(text = LoremIpsum(6).values.joinToString(separator = " "))
            }
        }
    }
}

@Preview(widthDp = 750)
@Preview(widthDp = 750, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ManyButtonsSample() = Preview {
    data class State(
        val icon: Boolean,
        val label: Boolean,
        val description: Boolean,
        val outline: Boolean = false,
        val tonal: Boolean = false,
        val empty: Boolean = false,
    )

    val buttons = remember {
        val initial = listOf(
            State(icon = false, label = false, description = true),
            State(icon = false, label = true, description = false),
            State(icon = false, label = true, description = true),
            State(icon = true, label = false, description = false),
            State(icon = true, label = false, description = true),
            State(icon = true, label = true, description = false),
            State(icon = true, label = true, description = true),
            State(empty = true, icon = false, label = false, description = false),
        )
        buildList {
            addAll(initial)
            initial.forEach { add(it.copy(outline = true)) }
            initial.forEach { add(it.copy(tonal = true)) }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4), contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(buttons) { item ->
            val content: @Composable RowScope.() -> Unit = {
                Box {
                    with(item) {
                        if (icon && label && description && outline) {
                            Text(
                                text = "?",
                                modifier = Modifier.align(BiasAlignment(-0.45f, 0f)),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        if (icon && label && !description && outline) {
                            Text(
                                text = "?",
                                modifier = Modifier.align(BiasAlignment(-0.15f, 0f)),
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.icon) Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null
                        )
                        Column {
                            if (item.empty) Text(text = "Empty")
                            if (item.label) Text(text = "Title")
                            if (item.description) Text(
                                text = "Description",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            val modifier = remember {
                Modifier
                    .padding(12.dp)
            }

            if (item.outline) OutlinedButton({ }, modifier, content = content)
            else if (item.tonal) FilledTonalButton({ }, modifier, content = content)
            else Button({ }, modifier, content = content)
        }
    }
}

@Preview(widthDp = 500)
@Preview(widthDp = 500, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NavigationBarSample() = Preview {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = remember { listOf("Songs", "Artists", "Playlists", "Settings") }

    val content = @Composable {
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    modifier = Modifier.blueprintId("item$index"),
                    icon = {
                        Icon(
                            modifier = Modifier.blueprintId("item${index}Icon"),
                            imageVector = when (index) {
                                0 -> Icons.Filled.MusicNote
                                1 -> Icons.Filled.People
                                2 -> Icons.Filled.PlaylistPlay
                                3 -> Icons.Filled.Settings
                                else -> error("Unreachable")
                            }, contentDescription = item
                        )
                    },
                    label = {
                        Text(
                            modifier = Modifier.blueprintId("item${index}Text"),
                            text = item
                        )
                    },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    }
    Column(modifier = Modifier.padding(70.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        content()
        Divider()
        Blueprint(
            applyPadding = false,
            precision = 0,
            blueprintBuilder = {
                widths {
                    group {
                        "item0".right lineTo "item1".left
                        "item0" lineTo "item0"
                        "item2" lineTo "item3"
                    }
                }
                heights {
                    group { "item0Icon" lineTo "item0Text" }
                    group { "item0" lineTo "item0" }
                    group(End) { "item3Icon".bottom lineTo "item3Text".top }
                }
            }
        ) {
            content()
        }
    }
}

@Composable
fun NavigationBarSample2() = Preview {
    Blueprint(
        applyPadding = false,
        precision = 0,
        blueprintBuilder = {
            widths {
                group {
                    "item0".right lineTo "item1".left
                    "item0" lineTo "item0"
                    "item2" lineTo "item3"
                }
            }
            heights {
                group { "item0Icon" lineTo "item0Text" }
                group { "item0" lineTo "item0" }
                group(End) { "item3Icon".bottom lineTo "item3Text".top }
            }
        }
    ) {
        val items = remember { listOf("Songs", "Artists", "Playlists", "Settings") }
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    modifier = Modifier.blueprintId("item$index"),
                    icon = {
                        Icon(
                            modifier = Modifier.blueprintId("item${index}Icon"),
                            imageVector = Icons.Filled.People,
                            contentDescription = item,
                        )
                    },
                    label = {
                        Text(
                            modifier = Modifier.blueprintId("item${index}Text"),
                            text = item
                        )
                    },
                    selected = index == 0,
                    onClick = { /*TODO*/ }
                )
            }
        }
    }
}

@Composable
private fun ButtonDemo2() = Box {
    val textSize = remember { Animatable(16f) }
    var strokeWidth by remember { mutableStateOf(1.dp) }
    LaunchedEffect(Unit) {
        while (true) {
            textSize.animateTo(30f, animationSpec = tween(3000, easing = LinearEasing))
            textSize.animateTo(15f, animationSpec = tween(3000, easing = LinearEasing))
//            strokeWidth = Random.nextDouble(1.0, 4.0).dp
        }
    }
    if (false) Text(
        text = "sdfsdf",
//        fontSize = textSize.value.sp
    )
    val lineWidth = 1.5.dp
    Blueprint(
        modifier = Modifier
//            .size(100.dp)
//            .border(1.dp, Color.Red)
            .padding(12.dp),
        lineStroke = BorderStroke(lineWidth, Color.Red),
//        borderStroke = BorderStroke(1.dp, Color.Transparent),
//        arrow = Arrow(length = lineWidth*3, angle = 35f, roundCap = false),
//        fontSize = 4.sp,
        precision = 1,
        densityRounding = true,
        applyPadding = true,
        blueprintBuilder = {
//            return@Blueprint
            widths {
                group { "1".right lineTo "2".left }
                group { "1" lineTo "2" }
                group { "icon2" lineTo "icon2" }
                group {
                    "icon" lineTo "icon"
                    "text" lineTo "text"
                }
                group { "icon" lineTo "text" }
//                group(Top) { "icon".center lineTo "text".right }
                group(Top) {
                    "smol" lineTo "smol"
                    "icon".left lineTo "text".left
                }
                group(Top) {
                    "smol2" lineTo "smol2"
                    "icon".left lineTo "text".left
                }
                group(Top) {
                    "smol2" lineTo "smol3"
                }
            }
//            return@Blueprint
            if (true) heights {
                group { "icon2" lineTo "icon2" }
//                group { "icon".top lineTo "icon2".bottom }
                group { "icon" lineTo "icon" }
                group(End) {
                    "text" spLineTo "text"
                    "text2" spLineTo "text2"
                }
                group(End) { "text" spLineTo "text2" }
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (true) Button(modifier = Modifier, onClick = { }) {
                if (textSize.value < 22f) Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .blueprintId("icon", null)
                        .padding(4.dp)
                        .blueprintId("icon2", null),
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
                Column {
                    if (false)Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .requiredSize(8.dp)
                                .background(Color.Green)
                                .blueprintId("smol")
                        )
                        Box(
                            modifier = Modifier
                                .requiredSize(14.dp)
                                .background(Color.Green)
                                .blueprintId("smol2")
                        )
                        Box(
                            modifier = Modifier
                                .requiredSize(14.dp)
                                .background(Color.Green)
                                .blueprintId("smol3")
                        )

                    }
                    Text(
                        modifier = Modifier.blueprintId("text"),
                        text = "Lorem ".repeat(1),
                        fontSize = textSize.value.sp,
                        style = LocalTextStyle.current.copy(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                    Text(
                        modifier = Modifier.blueprintId("text2"),
                        text = "Lorem ".repeat(2),
                        fontSize = 13.sp,
                        style = LocalTextStyle.current.copy(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }
            if (false) Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.Blue)
                        .blueprintId("1", null)
                )
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.Blue)
                        .blueprintId("2", null)
                )
            }
        }
    }
}

@Composable
private fun PetriDish() {
    BorderStroke::class
    Arrow::class
    Blueprint(
        modifier = Modifier
            .padding(20.dp)
            .border(Dp.Hairline, Color.Magenta),
        blueprintBuilder = {
            widths {
                group(position = Bottom) { "333" lineTo "444" }
                group(position = Top) { "111" lineTo "111" }
            }
            heights {
                group { "444" lineTo "444" }
                group(position = Start) { "lorem" lineTo "lorem" }
            }
        }
    ) {
        Pipette()
        Divider(Modifier.padding(8.dp))
        Row {
            var key by remember { mutableStateOf("111") }
            LaunchedEffect(Unit) {
                delay(1000)
                key = "1111"
            }
            Text(
                modifier = Modifier.blueprintId(key),
                text = "111"
            )
            if (false) Text(
                modifier = Modifier.blueprintId("222"),
                text = "222"
            )
            Text(
                modifier = Modifier.blueprintId("333"),
                text = "333"
            )
            Text(
                modifier = Modifier.blueprintId("444"),
                text = "444"
            )
        }
        Text(
            modifier = Modifier.blueprintId("lorem"),
            text = "lorem"
        )
        if (false)
            Column {
                repeat(3) { i ->
                    Text(
                        modifier = Modifier.blueprintId("44$i"),
                        text = "44$i"
                    )
                }
                Divider(Modifier.padding(8.dp))
                if (false)
                    Row {
                        Text(
                            modifier = Modifier.blueprintId("111"),
                            text = "111"
                        )
                        Text(
                            modifier = Modifier.blueprintId("222"),
                            text = "222"
                        )
                        Text(
                            modifier = Modifier.blueprintId("333"),
                            text = "333"
                        )
                    }
            }
    }
}