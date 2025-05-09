package com.popovanton0.blueprint

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.popovanton0.blueprint.dsl.SizeUnits

/**
 * No-op implementation.
 */
@Stable
public fun Modifier.blueprintId(
    id: String,
    sizeUnits: SizeUnits? = null
): Modifier = this