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
import com.timas.superapp.screens.LoginScreen
import com.timas.superapp.screens.QrScannerScreen
import com.timas.superapp.screens.SplashScreen
import com.timas.superapp.screens.ZekiiScreen
import com.timas.superapp.ui.theme.TimasTheme

private enum class AppScreen { HOME, LOGIN, QR_SCANNER, ZEKII }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimasTheme {
                var showSplash by remember { mutableStateOf(true) }
                var currentScreen by remember { mutableStateOf(AppScreen.HOME) }

                if (showSplash) {
                    SplashScreen(onSplashFinished = { showSplash = false })
                } else {
                    var searchQuery by remember { mutableStateOf("") }
                    var selectedBottomTab by remember { mutableStateOf(0) }
                    val focusManager = LocalFocusManager.current

                    when (currentScreen) {
                        AppScreen.ZEKII -> {
                            ZekiiScreen(onBack = { currentScreen = AppScreen.HOME })
                        }

                        AppScreen.LOGIN -> {
                            LoginScreen(
                                onBack = { currentScreen = AppScreen.HOME },
                                onLoginSuccess = { currentScreen = AppScreen.HOME }
                            )
                        }

                        AppScreen.QR_SCANNER -> {
                            QrScannerScreen(
                                onBack = { currentScreen = AppScreen.HOME },
                                onQrScanned = { result ->
                                    // QR sonucunu burada işleyin
                                    currentScreen = AppScreen.HOME
                                }
                            )
                        }

                        AppScreen.HOME -> {
                            Scaffold(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .pointerInput(Unit) {
                                        detectTapGestures(onTap = { focusManager.clearFocus() })
                                    },
                                topBar = {
                                    SuperAppToolbar(
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { searchQuery = it },
                                        onProfileClick = { currentScreen = AppScreen.LOGIN }
                                    )
                                },
                                bottomBar = {
                                    SuperAppBottomNav(
                                        selectedIndex = selectedBottomTab,
                                        onTabSelected = { selectedBottomTab = it },
                                        onQrClick = { currentScreen = AppScreen.QR_SCANNER }
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
                                    QuickAppsSection(onNavigateToZekii = { currentScreen = AppScreen.ZEKII })

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
}
