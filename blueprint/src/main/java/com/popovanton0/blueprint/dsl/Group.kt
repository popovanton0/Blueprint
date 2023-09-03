package com.popovanton0.blueprint.dsl

import androidx.compose.runtime.Immutable

@Immutable
internal sealed class Group {
    internal abstract val dimensions: List<Dimension>
    internal abstract val position: Position
}

@Immutable
internal data class HorizontalGroup internal constructor(
    override val dimensions: List<Dimension>,
    override val position: Position.Vertical,
) : Group()

@Immutable
internal data class VerticalGroup internal constructor(
    override val dimensions: List<Dimension>,
    override val position: Position.Horizontal,
) : Group()