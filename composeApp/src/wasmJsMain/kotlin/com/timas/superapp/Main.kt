package com.timas.superapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(title = "Timaş SuperApp", canvasElementId = "ComposeTarget") {
        App()
    }
}
