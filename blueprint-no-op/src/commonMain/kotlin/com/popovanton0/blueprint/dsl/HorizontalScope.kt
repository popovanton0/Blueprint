package com.popovanton0.blueprint.dsl

import com.popovanton0.blueprint.ExperimentalBlueprintApi
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Bottom

@BlueprintDslMarker
public class HorizontalScope internal constructor() {
    /**
     * No-op implementation.
     */
    public fun group(
        position: Position.Vertical = Bottom,
        block: HorizontalGroupScope.() -> Unit
    ): Unit = Unit
}

private val DefautlAnchor = Anchor("", 0f)

@BlueprintDslMarker
public class HorizontalGroupScope internal constructor() : GroupScope() {
    public val String.left: Anchor get() = DefautlAnchor
    public val String.center: Anchor get() = DefautlAnchor
    public val String.right: Anchor get() = DefautlAnchor

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