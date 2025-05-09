package com.popovanton0.blueprint.dsl

import androidx.compose.runtime.Immutable

@Immutable
internal data class Dimension internal constructor(
    internal val startAnchor: Anchor,
    internal val endAnchor: Anchor,
    internal val unit: MeasureUnit
)