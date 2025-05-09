package com.popovanton0.blueprint.dsl

import com.popovanton0.blueprint.ExperimentalBlueprintApi
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.Start

@BlueprintDslMarker
public class VerticalScope internal constructor() {
    /**
     * No-op implementation.
     */
    public fun group(
        position: Position.Horizontal = Start,
        block: VerticalGroupScope.() -> Unit
    ): Unit = Unit
}

private val DefautlAnchor = Anchor("", 0f)

@BlueprintDslMarker
public class VerticalGroupScope internal constructor() : GroupScope() {
    public val String.top: Anchor get() = DefautlAnchor
    public val String.center: Anchor get() = DefautlAnchor
    public val String.bottom: Anchor get() = DefautlAnchor

    /**
     * No-op implementation.
     */
    public infix fun String.lineTo(other: String): Unit = Unit

    /**
     * No-op implementation.
     */
    @ExperimentalBlueprintApi
    public infix fun String.spLineTo(other: String): Unit = Unit
}