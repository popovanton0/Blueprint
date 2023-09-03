package com.popovanton0.blueprint.dsl

import androidx.compose.runtime.Immutable

@Immutable
public sealed interface Position {

    @JvmInline
    public value class Horizontal private constructor(private val value: Int) : Position {
        override fun toString(): String = when (this) {
            Start -> "Start"
            End -> "End"
            else -> "Invalid"
        }

        internal fun toRtl(isRtl: Boolean) = when (this) {
            Start -> if (isRtl) End else Start
            End -> if (isRtl) Start else End
            else -> error("Invalid")
        }

        public companion object {
            public val Start: Horizontal = Horizontal(0)
            public val End: Horizontal = Horizontal(1)
        }
    }

    @JvmInline
    public value class Vertical private constructor(private val value: Int) : Position {
        override fun toString(): String = when (this) {
            Top -> "Top"
            Bottom -> "Bottom"
            else -> "Invalid"
        }

        public companion object {
            public val Top: Vertical = Vertical(0)
            public val Bottom: Vertical = Vertical(1)
        }
    }
}
