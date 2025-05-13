package com.popovanton0.blueprint.app

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum


@Preview(fontScale = 1.0f)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LabPreview() = Lab()

@Preview
@Composable
private fun SamplePreview() = Sample(LoremIpsum(6).values.joinToString(separator = " "))

@Preview(widthDp = 750)
@Preview(widthDp = 750, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ManyButtonsSamplePreview() = ManyButtonsSample()

@Preview(widthDp = 500)
@Preview(widthDp = 500, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavigationBarSamplePreview() = NavigationBarSample()

