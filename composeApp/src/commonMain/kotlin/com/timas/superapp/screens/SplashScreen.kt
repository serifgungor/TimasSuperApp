package com.timas.superapp.screens

import timas.composeapp.generated.resources.Res
import timas.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // 5 saniye bekleyip callback tetikliyoruz
    LaunchedEffect(key1 = true) {
        delay(5000)
        onSplashFinished()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "Super App Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}
