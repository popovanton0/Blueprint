package com.popovanton0.blueprint.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Blueprint Demo",
    ) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Lab()
            }
        }
    }
}