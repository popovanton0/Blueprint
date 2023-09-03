package com.popovanton0.blueprint

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.OnGloballyPositionedModifier
import androidx.compose.ui.modifier.ModifierLocalConsumer
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import com.popovanton0.blueprint.dsl.SizeUnits

/**
 * This modifier marks a UI composable with an [id] that can be used in blueprintBuilder to define
 * dimension lines between this composable and others.
 *
 * [id] must be **unique** for all children of the [Blueprint] composable.
 *
 * @param id *unique* identifier for this composable. Use it in
 * blueprintBuilder to define dimension lines.
 * @param sizeUnits if non-null, size label is drawn over the composable.
 *
 * @see blueprintEnabled
 */
@Stable
public fun Modifier.blueprintId(
    id: String,
    sizeUnits: SizeUnits? = null
): Modifier = if (blueprintEnabled) {
    inspectable(
        inspectorInfo = debugInspectorInfo {
            name = "blueprintId"
            properties["id"] = id
            properties["sizeUnits"] = sizeUnits
        }
    ) {
        BlueprintMarkerModifier(id, sizeUnits)
    }
} else {
    this
}

private class BlueprintMarkerModifier(
    private val id: String,
    private val sizeUnits: SizeUnits?,
) : ModifierLocalConsumer, OnGloballyPositionedModifier {

    private var markers: MutableMap<String, BlueprintMarker>? = null

    override fun onModifierLocalsUpdated(scope: ModifierLocalReadScope) = with(scope) {
        markers = ModifierLocalBlueprintMarkers.current
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        val markers = markers ?: return
        if (coordinates.isAttached) {
            markers.remove(id)
            markers[id] = BlueprintMarker(coordinates, sizeUnits)
        } else {
            markers.remove(id)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BlueprintMarkerModifier) return false
        if (id != other.id) return false
        return sizeUnits == other.sizeUnits
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (sizeUnits?.hashCode() ?: 0)
        return result
    }
}