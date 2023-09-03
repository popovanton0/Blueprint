package com.popovanton0.blueprint

import androidx.compose.ui.modifier.modifierLocalOf

internal val ModifierLocalBlueprintMarkers =
    modifierLocalOf<MutableMap<String, BlueprintMarker>?> { null }