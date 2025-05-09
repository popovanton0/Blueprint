package com.popovanton0.blueprint

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalProvider
import androidx.compose.ui.modifier.ProvidableModifierLocal
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.debugInspectorInfo

/**
 * Internal copy of [ExperimentalComposeUiApi] [modifierLocalProvider], to not depend on
 * experimental APIs.
 */
internal fun <T> Modifier.blueprintModifierLocalProvider(
    key: ProvidableModifierLocal<T>,
    value: () -> T
): Modifier {
    return this.then(
        object : ModifierLocalProvider<T>, InspectorValueInfo(
            debugInspectorInfo {
                name = "blueprintModifierLocalProvider"
                properties["key"] = key
                properties["value"] = value
            }
        ) {
            override val key: ProvidableModifierLocal<T> = key
            override val value by derivedStateOf(value)
        }
    )
}
