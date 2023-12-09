package com.popovanton0.blueprint

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import com.popovanton0.blueprint.dsl.SizeUnits

/**
 * This modifier marks a UI-composable with an [id] that can be used in blueprintBuilder to define
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
    this then BlueprintMarkerElement(
        id = id,
        sizeUnits = sizeUnits,
        inspectorInfo = {
            name = "blueprintId"
            properties["id"] = id
            properties["sizeUnits"] = sizeUnits
        }
    )
} else {
    this
}

private class BlueprintMarkerElement(
    val id: String,
    val sizeUnits: SizeUnits?,
    val inspectorInfo: InspectorInfo.() -> Unit,
) : ModifierNodeElement<BlueprintMarkerNode>() {
    override fun create(): BlueprintMarkerNode = BlueprintMarkerNode(id, sizeUnits)

    override fun update(node: BlueprintMarkerNode) {
        node.id = id
        node.sizeUnits = sizeUnits
    }

    override fun equals(other: Any?): Boolean {
        val otherModifierElement = other as? BlueprintMarkerElement ?: return false
        return id == otherModifierElement.id && sizeUnits == otherModifierElement.sizeUnits
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (sizeUnits?.hashCode() ?: 0)
        return result
    }

    override fun InspectorInfo.inspectableProperties() {
        inspectorInfo()
    }
}

private class BlueprintMarkerNode(
    var id: String,
    var sizeUnits: SizeUnits?,
) : Modifier.Node(), ModifierLocalModifierNode, GlobalPositionAwareModifierNode {

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        val markers = ModifierLocalBlueprintMarkers.current ?: return
        markers.remove(id)
        if (coordinates.isAttached) markers[id] = BlueprintMarker(coordinates, sizeUnits)
    }
}