package com.popovanton0.blueprint.dsl

import com.popovanton0.blueprint.ExperimentalBlueprintApi
import kotlin.jvm.JvmInline

@JvmInline
public value class MeasureUnit private constructor(private val value: Int) {
    override fun toString(): String = when (this) {
        Dp -> "Dp"
        Sp -> "Sp"
        else -> "Invalid"
    }

    public companion object {
        public val Dp: MeasureUnit = MeasureUnit(0)

        /**
         * Currently doesn't work correctly
         */
        @ExperimentalBlueprintApi
        public val Sp: MeasureUnit = MeasureUnit(1)
    }
}