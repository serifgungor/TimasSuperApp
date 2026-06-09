package com.timas.superapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.timas.superapp.components.SuperAppBottomNav
import com.timas.superapp.components.SuperAppToolbar
import com.timas.superapp.components.SuperAppSlider
import com.timas.superapp.components.QuickAppsSection
import com.timas.superapp.screens.QrScannerScreen
import com.timas.superapp.screens.SplashScreen
import com.timas.superapp.ui.theme.TimasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimasTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen(onSplashFinished = { showSplash = false })
                } else {
                    var searchQuery by remember { mutableStateOf("") }
                    var selectedBottomTab by remember { mutableStateOf(0) }
                    var showQrScanner by remember { mutableStateOf(false) }   // ← YENİ
                    val focusManager = LocalFocusManager.current

                    // QR Ekranı açıksa onu göster
                    if (showQrScanner) {
                        QrScannerScreen(
                            onBack = { showQrScanner = false },
                            onQrScanned = { result ->
                                // QR sonucunu burada işleyin
                                // Örn: Toast, navigasyon, API çağrısı vs.
                                showQrScanner = false
                            }
                        )
                    } else {
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures(onTap = { focusManager.clearFocus() })
                                },
                            topBar = {
                                SuperAppToolbar(
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { searchQuery = it }
                                )
                            },
                            bottomBar = {
                                SuperAppBottomNav(
                                    selectedIndex = selectedBottomTab,
                                    onTabSelected = { selectedBottomTab = it },
                                    onQrClick = { showQrScanner = true }   // ← YENİ
                                )
                            }
                        ) { innerPadding ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                SuperAppSlider()
                                QuickAppsSection()

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Super App'e Hoş Geldiniz")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
