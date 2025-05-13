package com.popovanton0.blueprint.dsl

import androidx.compose.runtime.Immutable

@Immutable
internal data class Blueprint internal constructor(
    internal val horizontalTopGroups: List<HorizontalGroup>,
    internal val horizontalBottomGroups: List<HorizontalGroup>,
    internal val verticalLeftGroups: List<VerticalGroup>,
    internal val verticalRightGroups: List<VerticalGroup>,
) {
    internal val groupCollection: List<List<Group>> = listOf(
        horizontalTopGroups,
        horizontalBottomGroups,
        verticalLeftGroups,
        verticalRightGroups,
    )
}