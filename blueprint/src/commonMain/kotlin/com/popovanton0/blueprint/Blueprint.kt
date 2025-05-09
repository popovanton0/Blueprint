package com.popovanton0.blueprint

import androidx.annotation.IntRange
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawTransform
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isFinite
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachReversed
import androidx.compose.ui.util.lerp
import androidx.compose.ui.util.trace
import com.popovanton0.blueprint.dsl.Anchor
import com.popovanton0.blueprint.dsl.Arrow
import com.popovanton0.blueprint.dsl.Blueprint
import com.popovanton0.blueprint.dsl.BlueprintBuilderScope
import com.popovanton0.blueprint.dsl.Group
import com.popovanton0.blueprint.dsl.HorizontalGroup
import com.popovanton0.blueprint.dsl.MeasureUnit
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.End
import com.popovanton0.blueprint.dsl.Position.Horizontal.Companion.Start
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Bottom
import com.popovanton0.blueprint.dsl.Position.Vertical.Companion.Top
import com.popovanton0.blueprint.dsl.SizeUnits
import com.popovanton0.blueprint.dsl.VerticalGroup
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Global flag that is used to disable blueprint logic for all [blueprintId] modifiers and all
 * [Blueprint] invocations.
 *
 * Must be called before the first composition initialization (e.g. in the App's `onCreate`).
 *
 * Useful to reduce memory footprint and increase the performance of the production app.
 *
 * Sample:
 * ```
 * class MyApp: Application() {
 *      override fun onCreate() {
 *          super.onCreate()
 *          blueprintEnabled = BuildConfig.DEBUG
 *      }
 * }
 * ```
 */
public var blueprintEnabled: Boolean = true

/**
 * Draws a *blueprint* over [content], if any children have [blueprintId] modifiers on any
 * nesting level.
 *
 * [blueprintBuilder] example:
 * ```
 * widths {
 *     group {
 *         "icon" lineTo "icon"
 *         "text" lineTo "text"
 *     }
 *     group { "icon" lineTo "text" }
 *     group(Top) { "icon".center lineTo "text".center }
 *     group(Top) { "icon".center lineTo "text".center }
 * }
 *
 * heights {
 *     group { "icon" lineTo "icon" }
 *     group { "icon" lineTo "icon" }
 *     group(End) { "text" lineTo "text" }
 *     group(End) { "text" lineTo "text" }
 * }
 * ```
 *
 * [LayoutDirection.Rtl] is not currently supported.
 *
 * @param modifier will be applied to the [Blueprint] wrapper. Applied only if [enabled].
 * @param lineStroke of the dimension lines
 * @param borderStroke that is drawn around children with [blueprintId] modifier applied.
 * To disable it, specify [Color.Transparent]
 * @param fontSize text size of the dimension labels (size numbers)
 * @param fontColor text color of the dimension labels (size numbers).
 * By default, it adapts to the dark theme.
 * @param arrow if non-null, small arrows will be drawn at the ends of each dimension line
 * @param precision number of digits after a decimal point to display in the dimension label,
 * if it has non-zero fractional part. It is useful if you are trying to measure fractional
 * dimension values.
 *
 * Note that dimensional values can be displayed incorrectly, with wrong decimal part. The same
 * behavior is observed in the Android Studio's *Layout Inspector*.
 * This is possibly due to rounding.
 *
 * @param densityRounding to reduce the effect of the aforementioned quirk, set
 * [densityRounding] == true. Then, [Density.density] will be rounded to the nearest .5 value,
 * like 2, 2.5, 3 or 3.5.
 * @param applyPadding if true, padding is applied to [content] such that all dimension lines fill
 * fit and will not overlap with the surrounding.
 * @param enabled if false, the blueprint is not drawn at all.
 * Analogous to just calling [content] directly.
 * [blueprintEnabled] takes precedence over this value.
 * @param blueprintBuilder a DSL-based builder that defines the blueprint structure.
 * Samples are below.
 * @param content on which the blueprint will be drawn. It can contain LayoutNodes with
 * [blueprintId] modifier applied.
 *
 * @see blueprintEnabled
 */
@Composable
public fun Blueprint(
    modifier: Modifier = Modifier,
    lineStroke: BorderStroke = BorderStroke(1.5.dp, Color.Red),
    borderStroke: BorderStroke = BorderStroke(
        width = lineStroke.width,
        color = ((lineStroke.brush as? SolidColor)?.value ?: Color.Red).copy(alpha = 0.4f)
    ),
    fontSize: TextUnit = LabelTextStyle.fontSize,
    fontColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    arrow: Arrow? = Arrow(length = lineStroke.width * 3),
    @IntRange(from = 0) precision: Int = 1,
    densityRounding: Boolean = true,
    applyPadding: Boolean = true,
    enabled: Boolean = true,
    blueprintBuilder: BlueprintBuilderScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    require(lineStroke.width.isSpecified && lineStroke.width.isFinite) {
        "Invalid lineStroke width: must be finite and specified"
    }
    require(borderStroke.width.isSpecified && borderStroke.width.isFinite) {
        "Invalid borderStroke width: must be finite and specified"
    }
    require(precision in 0..Int.MAX_VALUE) { "precision must be in [0..Int.MAX_VALUE]" }
    if (!enabled || !blueprintEnabled) {
        content()
        return
    }

    val density = LocalDensity.current.density
    CompositionLocalProvider(
        LocalDensity provides Density(
            density = if (densityRounding) (density * 2).roundToInt() / 2f else density,
            fontScale = LocalDensity.current.fontScale
        )
    ) {
        val isDarkTheme = isSystemInDarkTheme()
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        val latestBlueprintBuilder by rememberUpdatedState(blueprintBuilder)
        val blueprint = remember(latestBlueprintBuilder) {
            BlueprintBuilderScope().apply(latestBlueprintBuilder).toBlueprint()
        }
        val textMeasurer = rememberTextMeasurer()
        val textStyle = remember(fontSize, fontColor) {
            LabelTextStyle.copy(fontSize = fontSize, lineHeight = fontSize, color = fontColor)
        }
        val groupSpace = rememberGroupSpace(
            precision, lineStroke.width, textStyle, textMeasurer, arrow
        )
        val markers = remember { mutableStateMapOf<String, BlueprintMarker>() }
        var rootCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

        Box(
            modifier = modifier
                .run { if (applyPadding) padding(blueprint, groupSpace) else this }
                .blueprintModifierLocalProvider(ModifierLocalBlueprintMarkers) { markers }
                .onGloballyPositioned { rootCoordinates = it }
                .drawWithContent {
                    drawContent()
                    val actualRootCoordinates = rootCoordinates
                    if (actualRootCoordinates != null) trace("drawBlueprint") {
                        val params = Params(
                            this, lineStroke, borderStroke, textStyle, arrow, precision, blueprint,
                            isDarkTheme, isRtl, textMeasurer, groupSpace, markers,
                            actualRootCoordinates,
                        )
                        drawBlueprint(params)
                    }
                }
        ) {
            content()
        }
    }
}

private class Params(
    density: Density,
    lineStroke: BorderStroke,
    val borderStroke: BorderStroke,
    val textStyle: TextStyle,
    val arrow: Arrow?,
    val precision: Int,
    val blueprint: Blueprint,
    val isDarkTheme: Boolean,
    val isRtl: Boolean,
    val textMeasurer: TextMeasurer,
    val groupSpace: GroupSpace,
    val markers: SnapshotStateMap<String, BlueprintMarker>,
    val rootCoordinates: LayoutCoordinates,
) {
    val lineStroke = if (DEBUG) lineStroke.copy(brush = randomBrush()) else lineStroke
    val lineWidthPx = with(density) { lineStroke.width.toPx() }
}

@Composable
private fun rememberGroupSpace(
    precision: Int,
    lineWidth: Dp,
    textStyle: TextStyle,
    textMeasurer: TextMeasurer,
    arrow: Arrow?,
): GroupSpace = with(LocalDensity.current) {
    remember(lineWidth, textStyle, textMeasurer, arrow) {
        val textLayoutResult = textMeasurer.measure(
            text = "99.${"9".repeat(precision)}sp",
            style = textStyle,
            maxLines = 1,
        )
        val arrowPadding = arrow?.projectionOnExtendingLine(lineWidth)
            ?.coerceAtLeast(MinimumTextArrowPadding)
            ?: MinimumTextArrowPadding
        GroupSpace(
            horizontal = lineWidth + arrowPadding * 2 + textLayoutResult.size.height.toDp(),
            vertical = lineWidth + arrowPadding * 2 + textLayoutResult.size.width.toDp(),
        )
    }
}

/** Represents a space needed to draw one group */
@Immutable
private data class GroupSpace(val horizontal: Dp, val vertical: Dp)

@Stable
private fun Modifier.padding(blueprint: Blueprint, groupSpace: GroupSpace) = padding(
    top = blueprint.horizontalTopGroups.size * groupSpace.horizontal,
    bottom = blueprint.horizontalBottomGroups.size * groupSpace.horizontal,
    start = blueprint.verticalLeftGroups.size * groupSpace.vertical,
    end = blueprint.verticalRightGroups.size * groupSpace.vertical,
)

private fun DrawScope.drawBlueprint(params: Params) = with(params) {
    if (DEBUG) drawRect(Color.Green, alpha = 0.2f)

    blueprint.groupCollection.fastForEach groupCollections@{ groups ->
        val groups = groups.fastFilterNot {
            it.dimensions.fastAll { dimension ->
                val startTarget = markers.getTarget(dimension.startAnchor.key)
                val endTarget = markers.getTarget(dimension.endAnchor.key)

                startTarget == null || endTarget == null
            }
        }
        var groupIndex = groups.lastIndex
        groups.fastForEachReversed groups@{ group ->
            group.dimensions.fastForEach dimensions@{ dimension ->

                val startTarget = markers.getTarget(dimension.startAnchor.key)
                val endTarget = markers.getTarget(dimension.endAnchor.key)

                if (startTarget == null || endTarget == null) return@dimensions

                val firstExtendingLine = extendingLine(
                    params, group, groupIndex, isStartTarget = true, startTarget,
                    dimension.startAnchor
                )

                val secondExtendingLine = extendingLine(
                    params, group, groupIndex, isStartTarget = false, endTarget, dimension.endAnchor
                )

                drawLine(
                    brush = lineStroke.brush,
                    strokeWidth = lineWidthPx,
                    start = firstExtendingLine.startOffset,
                    end = firstExtendingLine.endOffset,
                )
                drawLine(
                    brush = lineStroke.brush,
                    strokeWidth = lineWidthPx,
                    start = secondExtendingLine.startOffset,
                    end = secondExtendingLine.endOffset,
                )

                val label = dimensionLabel(
                    lineWidthPx, group, precision, dimension.unit,
                    firstExtendingLine.endOffsetNoLineWidthCorrection,
                    secondExtendingLine.endOffsetNoLineWidthCorrection
                )

                val isNotEnoughSpaceForLabel = drawLabel(
                    params, group, label = label,
                    arrowPadding = arrow?.projectionOnExtendingLine(strokeWidth = 0.dp)
                        ?.coerceAtLeast(MinimumTextArrowPadding)
                        ?.toPx()
                        ?: MinimumTextArrowPadding.toPx(),
                    firstExtendingLine.endOffset,
                    secondExtendingLine.endOffset,
                )
                drawDimensionLine(
                    lineStroke.brush, lineWidthPx, arrow.takeUnless { isNotEnoughSpaceForLabel },
                    firstExtendingLine.endOffset,
                    secondExtendingLine.endOffset
                )
            }
            groupIndex--
        }
    }

    markers.forEach { (key, marker) ->
        if (!marker.coordinates.isAttached) {
            markers.remove(key)
            return@forEach
        }
        val boundingBox = rootCoordinates.localBoundingBoxOf(marker.coordinates)
        drawBorder(borderStroke, boundingBox)
        if (marker.sizeUnits != null) drawSizeLabel(
            precision, isDarkTheme, textStyle, textMeasurer, marker.sizeUnits, boundingBox
        )
    }
}

private fun DrawScope.drawDimensionLine(
    brush: Brush,
    strokeWidth: Float,
    arrow: Arrow?,
    start: Offset,
    end: Offset,
) {
    drawLine(
        brush = brush,
        strokeWidth = strokeWidth,
        start = start,
        end = end,
        cap = StrokeCap.Square,
    )

    if (arrow == null || arrow.length == 0.dp) return
    val arrowCap = if (arrow.roundCap) StrokeCap.Round else StrokeCap.Butt

    val arrowsWillOverlap = (end - start).getDistance() <
            arrow.projectionOnDimensionLine(strokeWidth.toDp()).toPx() * 2

    if (arrowsWillOverlap) return
    floatArrayOf(-arrow.angle, arrow.angle).forEach { angle ->
        val startVector = end - start
        withTransform({
            val vector = startVector withLength strokeWidth / 2
            if (vector.isUnspecified) return
            translate(vector)
            rotate(angle, pivot = start)
            translate(start)
        }) {
            if (DEBUG) drawCircle(Color.Green, radius = 1.1f, center = Offset.Zero)
            drawLine(
                brush = brush, strokeWidth = strokeWidth, cap = arrowCap,
                start = Offset.Zero,
                end = startVector withLength arrow.length.toPx(),
            )
        }

        val endVector = start - end
        withTransform({
            val vector = endVector withLength strokeWidth / 2
            translate(vector)
            rotate(angle, pivot = end)
            translate(end)
        }) {
            if (DEBUG) drawCircle(Color.Green, radius = 1.1f, center = Offset.Zero)
            drawLine(
                brush = brush, strokeWidth = strokeWidth, cap = arrowCap,
                start = Offset.Zero,
                end = endVector withLength arrow.length.toPx(),
            )
        }
    }
}

/**
 * @return new vector with the same origin and direction, but [newLength]
 */
private infix fun Offset.withLength(newLength: Float) = this * (newLength / getDistance())

private fun DrawTransform.translate(offset: Offset) = translate(offset.x, offset.y)

private fun DrawScope.drawBorder(
    borderStroke: BorderStroke,
    boundingBox: Rect
) {
    val strokeWidth = borderStroke.width.toPx()
    drawRect(
        brush = if (DEBUG) randomBrush() else borderStroke.brush,
        topLeft = boundingBox.topLeft + (Offset(strokeWidth, strokeWidth) / 2f),
        size = boundingBox.size.copy(
            width = boundingBox.size.width - strokeWidth,
            height = boundingBox.size.height - strokeWidth,
        ),
        style = Stroke(strokeWidth, cap = StrokeCap.Butt),
    )
}

private fun DrawScope.drawSizeLabel(
    precision: Int,
    isDarkTheme: Boolean,
    textStyle: TextStyle,
    textMeasurer: TextMeasurer,
    sizeUnits: SizeUnits,
    boundingBox: Rect
) {
    val label = with(boundingBox.size) {
        val density = this@drawSizeLabel
        val width = width.pxToDimensionLabel(density, precision, sizeUnits.xUnit)
        val height = height.pxToDimensionLabel(density, precision, sizeUnits.yUnit)
        if (sizeUnits.xUnit == MeasureUnit.Sp) "$width x $height"
        else "${width}x$height"
    }
    val textLayoutResult = textMeasurer.measure(
        text = label,
        style = textStyle,
        overflow = TextOverflow.Visible,
        maxLines = 1,
    )
    val textOffset = with(textLayoutResult.size.toSize()) { Offset(width / 2, height) }

    val topLeft = boundingBox.bottomCenter - textOffset

    drawRoundRect(
        color = if (isDarkTheme) Color.Black else Color.White,
        topLeft = topLeft,
        size = textLayoutResult.size.toSize(),
        cornerRadius = CornerRadius(2.dp.toPx()),
    )
    drawText(textLayoutResult, topLeft = topLeft)
}

/**
 * @return null if target is detached or was not found
 */
private fun MutableMap<String, BlueprintMarker>.getTarget(key: String): LayoutCoordinates? {
    val target = get(key) ?: return null
    if (!target.coordinates.isAttached) {
        remove(key)
        return null
    }
    return target.coordinates
}

private fun Density.extendingLine(
    params: Params,
    group: Group,
    groupIndex: Int,
    isStartTarget: Boolean,
    target: LayoutCoordinates,
    anchor: Anchor,
): Line = with(params) {
    val canvasSize = rootCoordinates.size.toSize()
    val extendingLineLengthOutOfCanvas = when (group) {
        is HorizontalGroup -> groupSpace.horizontal.toPx() * (groupIndex + 1)
        is VerticalGroup -> groupSpace.vertical.toPx() * (groupIndex + 1)
    }
    val lineWidthCorrection = lineWidthPx / 2 * lerp(1, -1, anchor.alignment)

    val targetBoundingBox = rootCoordinates.localBoundingBoxOf(target)
    // todo rtl
    val startOffset =
        calculateStartOffset(targetBoundingBox, group, anchor, lineWidthCorrection)
    val endOffset =
        calculateEndOffset(group, startOffset, canvasSize, extendingLineLengthOutOfCanvas)
    val startOffsetNoLineWidthCorrection =
        calculateStartOffset(
            targetBoundingBox, group, anchor,
            lineWidthCorrection = lineWidthPx / 2 * if (isStartTarget) 1 else -1
        )
    val endOffsetNoLineWidthCorrection =
        calculateEndOffset(
            group, startOffsetNoLineWidthCorrection, canvasSize, extendingLineLengthOutOfCanvas
        )
    return Line(
        startOffset, endOffset, startOffsetNoLineWidthCorrection, endOffsetNoLineWidthCorrection,
    )
}

private fun Params.calculateStartOffset(
    targetBoundingBox: Rect,
    group: Group,
    anchor: Anchor,
    lineWidthCorrection: Float
) = targetBoundingBox.run {
    when (group) {
        is HorizontalGroup -> Offset(
            x = left + width * anchor.alignment + lineWidthCorrection,
            y = if (group.position == Bottom) top + height else top,
        )

        is VerticalGroup -> Offset(
            x = if (group.position.toRtl(isRtl) == Start) left else right,
            y = top + height * anchor.alignment + lineWidthCorrection,
        )
    }
}

private fun Params.calculateEndOffset(
    group: Group,
    startOffset: Offset,
    canvasSize: Size,
    extendingLineLengthOutOfCanvas: Float
) = when (group) {
    is HorizontalGroup -> startOffset.copy(
        y = if (group.position == Bottom) canvasSize.height + extendingLineLengthOutOfCanvas
        else -extendingLineLengthOutOfCanvas
    )

    is VerticalGroup -> startOffset.copy(
        x = if (group.position.toRtl(isRtl) == End) {
            canvasSize.width + extendingLineLengthOutOfCanvas
        } else {
            -extendingLineLengthOutOfCanvas
        }
    )
}

/**
 * @return true if there was not enough space to draw the label, so the background was drawn under
 * the label
 */
private fun DrawScope.drawLabel(
    params: Params,
    group: Group,
    label: String,
    arrowPadding: Float,
    start: Offset,
    end: Offset,
): Boolean = with(params) {
    val halfLineWidth = lineWidthPx / 2f

    val topLeft: Offset = when (group) {
        is HorizontalGroup -> when (group.position) {
            Bottom -> start - Offset(0f, groupSpace.horizontal.toPx())
            Top -> start + Offset(0f, arrowPadding)
            else -> error("Unreachable")
        }

        is VerticalGroup -> when (group.position.toRtl(isRtl)) {
            Start -> start + Offset(arrowPadding, 0f)
            End -> start - Offset(groupSpace.vertical.toPx(), 0f)
            else -> error("Unreachable")
        }
    } + Offset(halfLineWidth, halfLineWidth)

    val size: Offset = when (group) {
        is HorizontalGroup -> when (group.position) {
            Bottom -> end - Offset(0f, arrowPadding)
            Top -> end + Offset(0f, groupSpace.horizontal.toPx())
            else -> error("Unreachable")
        }

        is VerticalGroup -> when (group.position.toRtl(isRtl)) {
            Start -> end + Offset(groupSpace.vertical.toPx(), 0f)
            End -> end - Offset(arrowPadding, 0f)
            else -> error("Unreachable")
        }
    } - Offset(halfLineWidth, halfLineWidth)


    val maxLabelSize = (size - topLeft).run { Size(x, y) }

    if (DEBUG) {
        drawTextAreaMarker(topLeft)
        drawTextAreaMarker(size)
        drawTextArea(topLeft, maxLabelSize)
    }

    val textLayoutResult = textMeasurer.measure(
        text = label,
        style = textStyle,
        overflow = TextOverflow.Visible,
        maxLines = 1,
    )

    val textSize = textLayoutResult.size.toSize()
    val isNotEnoughSpace = textSize.run {
        width > maxLabelSize.width || height > maxLabelSize.height
    }

    val textTopLeft = topLeft + when (group) {
        is HorizontalGroup -> when (group.position) {
            Bottom -> Offset(
                x = (maxLabelSize.width - textSize.width) / 2,
                y = maxLabelSize.height - textSize.height,
            )

            Top -> Offset(
                x = (maxLabelSize.width - textSize.width) / 2,
                y = 0f,
            )

            else -> error("Unreachable")
        }

        is VerticalGroup -> when (group.position) {
            Start -> Offset(
                x = 0f,
                y = (maxLabelSize.height - textSize.height) / 2,
            )

            End -> Offset(
                x = maxLabelSize.width - textSize.width,
                y = (maxLabelSize.height - textSize.height) / 2,
            )

            else -> error("Unreachable")
        }
    }

    if (isNotEnoughSpace) drawRoundRect(
        color = textStyle.color,
        topLeft = textTopLeft,
        size = textSize,
        cornerRadius = CornerRadius(
            TextOnSmallDimensionCornerRadius.toPx(),
            TextOnSmallDimensionCornerRadius.toPx()
        ),
        colorFilter = invertColorFilter
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = textTopLeft
    )
    return@with isNotEnoughSpace
}

private fun DrawScope.drawTextAreaMarker(center: Offset) =
    drawCircle(Color.Black, radius = 4f, center = center)

private fun DrawScope.drawTextArea(topLeft: Offset, maxLabelSize: Size) = drawRect(
    color = Color.Blue,
    topLeft = topLeft,
    size = maxLabelSize,
    alpha = 0.4f,
)

private fun Density.dimensionLabel(
    lineWidth: Float,
    group: Group,
    precision: Int,
    unit: MeasureUnit,
    start: Offset,
    end: Offset
): String {
    val dimensionValue = end - start + Offset(lineWidth, lineWidth)
    val label = when (group) {
        is HorizontalGroup -> dimensionValue.x
        is VerticalGroup -> dimensionValue.y
    }.pxToDimensionLabel(this, precision, unit)
    return label
}

private fun Float.pxToDimensionLabel(
    density: Density,
    precision: Int,
    unit: MeasureUnit
): String {
    val value = this
    return with(density) {
        when (unit) {
            MeasureUnit.Dp -> value.toDp().value.format(precision)
            MeasureUnit.Sp -> value.toSp().value.format(precision) + "sp"
            else -> error("Unreachable")
        }
    }
}

/**
 * If [this] is a whole number, returns it as [Int.toString]
 * If [this] has non-zero fractional part, rounds it up to [precision] digits after the decimal
 * place and returns as [Float.toString]
 */
private fun Float.format(precision: Int): String =
    if (this % 1 == 0f) this.toInt().toString()
    else {
        val precisionDecimals = 10.0.pow(precision).toInt()
        val beforeDecimals = this.roundToInt()
        val afterDecimals = ((this * precisionDecimals).toInt() % precisionDecimals)
        "${beforeDecimals}.${afterDecimals}"
    }

private fun randomBrush() = SolidColor(Color(Random(0).nextLong()).copy(alpha = 0.6f))

/**
 * This does not allocate an iterator like [Iterable.filterNot].
 */
private inline fun <T> List<T>.fastFilterNot(predicate: (T) -> Boolean): List<T> {
    val destination = ArrayList<T>(size)
    for (element in this) if (!predicate(element)) destination.add(element)
    return destination
}

@Immutable
private data class Line(
    val startOffset: Offset,
    val endOffset: Offset,
    val startOffsetNoLineWidthCorrection: Offset,
    val endOffsetNoLineWidthCorrection: Offset,
)

// from https://developer.android.com/jetpack/compose/graphics/images/customize#invert_colors_of_an_image_composable
private val invertColorFilter = ColorFilter.colorMatrix(
    ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,
            0f, -1f, 0f, 0f, 255f,
            0f, 0f, -1f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f
        )
    )
)

/**
 * If true, debugging drawings are rendered. They help to develop the blueprint library
 */
private const val DEBUG = false
private val MinimumTextArrowPadding = 2.dp
private val TextOnSmallDimensionCornerRadius = 1.dp

private val LabelTextStyle = TextStyle.Default.copy(
    fontSize = 8.sp,
    color = Color.Black,
    textAlign = TextAlign.Center,
    lineHeight = 8.sp,
    //platformStyle = PlatformTextStyle(includeFontPadding = false),
    //platformStyle = PlatformTextStyle(spanStyle = null, paragraphStyle = null),
    lineHeightStyle = LineHeightStyle(
        LineHeightStyle.Alignment.Center,
        LineHeightStyle.Trim.None
    ),
    background = if (DEBUG) Color.Red else Color.Unspecified,
)