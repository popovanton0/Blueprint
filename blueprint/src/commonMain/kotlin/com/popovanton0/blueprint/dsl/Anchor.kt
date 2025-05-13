package com.popovanton0.blueprint.dsl

import androidx.compose.runtime.Immutable

@Immutable
public class Anchor internal constructor(
    internal val key: String,
    internal val alignment: Float,
) {
    init {
        require(alignment in 0f..1f) { "Alignment must be in [0..1]" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Anchor

        if (key != other.key) return false
        if (alignment.compareTo(other.alignment) != 0) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + alignment.hashCode()
        return result
    }
}