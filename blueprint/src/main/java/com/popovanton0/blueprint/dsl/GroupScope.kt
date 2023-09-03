package com.popovanton0.blueprint.dsl

import com.popovanton0.blueprint.ExperimentalBlueprintApi

public abstract class GroupScope internal constructor() {
    internal val dimensions = mutableListOf<Dimension>()

    /**
     * Defines a dimension line from the [this] to the [other].
     *
     * [Anchor] is acquired from these extensions on [String]:
     * - [VerticalGroupScope.top],
     * - [VerticalGroupScope.bottom],
     * - [VerticalGroupScope.center].
     * - [HorizontalGroupScope.left],
     * - [HorizontalGroupScope.center],
     * - [HorizontalGroupScope.right].
     *
     * Length on the resulting dimension line is in [MeasureUnit.Dp]s.
     */
    public infix fun Anchor.lineTo(other: Anchor) {
        dimensions.add(Dimension(this, other, MeasureUnit.Dp))
    }

    /**
     * Length on the resulting dimension line is in [MeasureUnit.Sp]s.
     *
     * @see lineTo
     */
    @ExperimentalBlueprintApi
    public infix fun Anchor.spLineTo(other: Anchor) {
        dimensions.add(Dimension(this, other, MeasureUnit.Sp))
    }
}