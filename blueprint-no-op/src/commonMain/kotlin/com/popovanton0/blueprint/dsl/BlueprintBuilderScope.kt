package com.popovanton0.blueprint.dsl

@BlueprintDslMarker
public class BlueprintBuilderScope internal constructor() {
    /**
     * No-op implementation.
     */
    public fun widths(block: HorizontalScope.() -> Unit): Unit = Unit

    /**
     * No-op implementation.
     */
    public fun heights(block: VerticalScope.() -> Unit): Unit = Unit
}