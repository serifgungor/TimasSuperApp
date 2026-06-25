package com.timas.superapp.components

import androidx.compose.runtime.Composable

@Composable
expect fun FullScreenWebViewDialog(
    title: String,
    url: String,
    onDismiss: () -> Unit
)
