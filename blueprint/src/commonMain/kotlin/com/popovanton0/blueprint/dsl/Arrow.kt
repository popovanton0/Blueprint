package com.popovanton0.blueprint.dsl

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isFinite
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.times
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

/**
 * Arrow extending from the dimension lines in four directions
 */
public class Arrow(
    public val length: Dp,
    public val angle: Float = 35f,
    public val roundCap: Boolean = false,
) {
    init {
        require(length.isSpecified && length.isFinite) {
            "Invalid arrow length: must be finite and specified"
        }
        require(angle in 0f..90f) { "Invalid arrow angle: must be in [0..180]" }
    }

    internal fun projectionOnDimensionLine(strokeWidth: Dp): Dp =
        cos(toRadians(angle.toDouble())) * (length + strokeWidth)

    internal fun projectionOnExtendingLine(strokeWidth: Dp): Dp =
        sin(toRadians(angle.toDouble())) * (length + strokeWidth)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Arrow

        if (length != other.length) return false
        if (angle.compareTo(other.angle) != 0) return false
        if (roundCap != other.roundCap) return false

        return true
    }

    override fun hashCode(): Int {
        var result = length.hashCode()
        result = 31 * result + angle.hashCode()
        result = 31 * result + roundCap.hashCode()
        return result
    }
}

private fun toRadians(angle: Double) = angle * PI / 180.0