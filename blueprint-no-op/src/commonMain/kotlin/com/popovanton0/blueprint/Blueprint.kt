package com.popovanton0.blueprint

import androidx.annotation.IntRange
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.popovanton0.blueprint.dsl.Arrow
import com.popovanton0.blueprint.dsl.BlueprintBuilderScope

/**
 * No-op implementation.
 */
public var blueprintEnabled: Boolean = true

/**
 * No-op implementation.
 */
@Composable
public fun Blueprint(
    modifier: Modifier = Modifier,
    lineStroke: BorderStroke = DefaultStroke,
    borderStroke: BorderStroke = DefaultStroke,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontColor: Color = Color.Unspecified,
    arrow: Arrow? = null,
    @IntRange(from = 0) precision: Int = 1,
    densityRounding: Boolean = true,
    applyPadding: Boolean = true,
    enabled: Boolean = true,
    blueprintBuilder: BlueprintBuilderScope.() -> Unit,
    content: @Composable () -> Unit,
): Unit = Unit

private val DefaultStroke = BorderStroke(1.dp, Color.Red)