package com.popovanton0.blueprint.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

@Preview
@Composable
private fun LabPreview() = Lab()

@Preview
@Composable
private fun SamplePreview() = Sample("Lorem ipsum dolor sit amet, consectetur adipiscing elit")

@Preview
@Composable
private fun ManyButtonsSamplePreview() = ManyButtonsSample()

@Preview
@Composable
private fun NavigationBarSamplePreview() = NavigationBarSample()
