package com.popovanton0.blueprint.dsl

import com.popovanton0.blueprint.ExperimentalBlueprintApi
import com.popovanton0.blueprint.blueprintId
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.End
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.Start

@BlueprintDslMarker
public class VerticalScope internal constructor() {
    internal val leftGroups = mutableListOf<VerticalGroup>()
    internal val rightGroups = mutableListOf<VerticalGroup>()

    /**
     * Defines a group of dimension lines. They will be drawn at the same depth level.
     *
     * To produce a pretty blueprint, try to place in one group only
     * non-overlapping dimension lines. Place the overlapping ones in different groups.
     */
    public fun group(
        position: Position.Horizontal = Start,
        block: VerticalGroupScope.() -> Unit
    ) {
        val scope = VerticalGroupScope()
        scope.block()
        if (scope.dimensions.isEmpty()) return
        val group = VerticalGroup(scope.dimensions, position)
        when (position) {
            Start -> leftGroups += group
            End -> rightGroups += group
        }
    }
}

@BlueprintDslMarker
public class VerticalGroupScope internal constructor() : GroupScope() {
    public val String.top: Anchor get() = Anchor(this, alignment = 0f)
    public val String.center: Anchor get() = Anchor(this, alignment = 0.5f)
    public val String.bottom: Anchor get() = Anchor(this, alignment = 1f)

    /**
     * Defines a dimension line from the [top] of [this] to the [bottom] of the [other].
     * To define a line from different anchor points, use these
     * extensions: [top], [bottom], [center].
     *
     * [this] and [other] are ids from [blueprintId] modifier.
     *
     * Length on the resulting dimension line is in [MeasureUnit.Dp]s.
     */
    public infix fun String.lineTo(other: String): Unit = top lineTo other.bottom

    /**
     * Length on the resulting dimension line is in [MeasureUnit.Sp]s.
     *
     * @see lineTo
     */
    @ExperimentalBlueprintApi
    public infix fun String.spLineTo(other: String): Unit = top spLineTo other.bottom
}