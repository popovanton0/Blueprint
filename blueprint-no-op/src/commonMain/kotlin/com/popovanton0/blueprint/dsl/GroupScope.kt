package com.popovanton0.blueprint.dsl

import com.popovanton0.blueprint.ExperimentalBlueprintApi

public abstract class GroupScope internal constructor() {
    /**
     * No-op implementation.
     */
    public infix fun Anchor.lineTo(other: Anchor): Unit = Unit

    /**
     * No-op implementation.
     */
    @ExperimentalBlueprintApi
    public infix fun Anchor.spLineTo(other: Anchor): Unit = Unit
}