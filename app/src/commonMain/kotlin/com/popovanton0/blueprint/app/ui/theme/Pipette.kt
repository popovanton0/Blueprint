package com.popovanton0.blueprint.app.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp


@Composable
internal fun Pipette() {
    var markers by remember {
        mutableStateOf<MutableMap<String, LayoutCoordinates>?>(null)
    }
    Text(
        modifier = Modifier
            .border(2.dp, color = Color.Yellow)
            .padding(3.dp)
            .modifierLocalConsumer {
                //markers = ModifierLocalBlueprintMarkers.current
            },
        text = markers?.entries?.joinToString(
            prefix = "Pipette content:\n",
            separator = "\n",
            transform = { it.key }
        ).toString()
    )
}