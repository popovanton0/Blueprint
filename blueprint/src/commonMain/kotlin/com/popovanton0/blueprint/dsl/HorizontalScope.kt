package com.popovanton0.blueprint.dsl

import com.popovanton0.blueprint.ExperimentalBlueprintApi
import com.popovanton0.blueprint.blueprintId
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Bottom
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Top

@BlueprintDslMarker
public class HorizontalScope internal constructor() {
    internal val topGroups = mutableListOf<HorizontalGroup>()
    internal val bottomGroups = mutableListOf<HorizontalGroup>()

    /**
     * Defines a group of dimension lines. They will be drawn at the same *depth level*.
     *
     * To produce a pretty blueprint, try to place in one group only
     * non-overlapping dimension lines. Place the overlapping ones in different groups.
     */
    public fun group(
        position: Position.Vertical = Bottom,
        block: HorizontalGroupScope.() -> Unit
    ) {
        val scope = HorizontalGroupScope()
        scope.block()
        if (scope.dimensions.isEmpty()) return
        val group = HorizontalGroup(scope.dimensions, position)
        when (position) {
            Top -> topGroups += group
            Bottom -> bottomGroups += group
        }
    }
}

@BlueprintDslMarker
public class HorizontalGroupScope internal constructor() : GroupScope() {
    public val String.left: Anchor get() = Anchor(this, alignment = 0f)
    public val String.center: Anchor get() = Anchor(this, alignment = 0.5f)
    public val String.right: Anchor get() = Anchor(this, alignment = 1f)

    /**
     * Defines a dimension line from the [left] of [this] to the [right] of the [other].
     * To define a line from different anchor points, use these
     * extensions: [left], [center], [right].
     *
     * [this] and [other] are ids from [blueprintId] modifier.
     *
     * Length on the resulting dimension line is in [MeasureUnit.Dp]s.
     */
    public infix fun String.lineTo(other: String): Unit = left lineTo other.right

    /**
     * Length on the resulting dimension line is in [MeasureUnit.Sp]s.
     *
     * @see lineTo
     */
    @ExperimentalBlueprintApi
    public infix fun String.spLineTo(other: String): Unit = left spLineTo other.right
}