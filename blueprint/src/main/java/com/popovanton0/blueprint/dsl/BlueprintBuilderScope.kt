package com.popovanton0.blueprint.dsl

@BlueprintDslMarker
public class BlueprintBuilderScope internal constructor() {
    private val horizontalTopGroups = mutableListOf<HorizontalGroup>()
    private val horizontalBottomGroups = mutableListOf<HorizontalGroup>()
    private val verticalLeftGroups = mutableListOf<VerticalGroup>()
    private val verticalRightGroups = mutableListOf<VerticalGroup>()

    /**
     * Groups in [block] are measuring a horizontal dimension — width.
     *
     * Lines in these groups will be placed at the [Position.Vertical.Top] or
     * [Position.Vertical.Bottom] of the composable.
     *
     * The placing order of groups is the following:
     * *first called -> placed closer to the composable*.
     *
     * In other words: going out form the center, like in an onion.
     */
    public fun widths(block: HorizontalScope.() -> Unit) {
        val scope = HorizontalScope()
        scope.block()
        horizontalTopGroups.clear()
        horizontalBottomGroups.clear()
        horizontalTopGroups.addAll(scope.topGroups)
        horizontalBottomGroups.addAll(scope.bottomGroups)
    }

    /**
     * Groups in [block] are measuring a vertical dimension — height.
     *
     * Lines in these groups will be placed at the [Position.Horizontal.Start] or
     * [Position.Horizontal.End] of the composable.
     *
     * The placing order of groups is the following:
     * *first called -> placed closer to the composable*.
     *
     * In other words: going out form the center, like in an onion.
     */
    public fun heights(block: VerticalScope.() -> Unit) {
        val scope = VerticalScope()
        scope.block()
        verticalLeftGroups.clear()
        verticalRightGroups.clear()
        verticalLeftGroups.addAll(scope.leftGroups)
        verticalRightGroups.addAll(scope.rightGroups)
    }

    internal fun toBlueprint(): Blueprint = Blueprint(
        horizontalTopGroups.toList(),
        horizontalBottomGroups.toList(),
        verticalLeftGroups.toList(),
        verticalRightGroups.toList(),
    )
}