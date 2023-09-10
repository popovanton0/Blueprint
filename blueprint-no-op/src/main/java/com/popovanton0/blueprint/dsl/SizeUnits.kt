package com.popovanton0.blueprint.dsl

import androidx.compose.runtime.Immutable
import com.popovanton0.blueprint.ExperimentalBlueprintApi

@Immutable
public class SizeUnits(
    public val xUnit: MeasureUnit = MeasureUnit.Dp,
    public val yUnit: MeasureUnit = MeasureUnit.Dp,
) {
    public companion object {
        public val Dp: SizeUnits = SizeUnits()

        @ExperimentalBlueprintApi
        public val Sp: SizeUnits = SizeUnits(MeasureUnit.Sp, MeasureUnit.Sp)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SizeUnits

        if (xUnit != other.xUnit) return false
        if (yUnit != other.yUnit) return false

        return true
    }

    override fun hashCode(): Int {
        var result = xUnit.hashCode()
        result = 31 * result + yUnit.hashCode()
        return result
    }
}