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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Surface
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.timas.superapp.components.SuperAppBottomNav
import com.timas.superapp.components.SuperAppToolbar
import com.timas.superapp.components.SuperAppSlider
import com.timas.superapp.components.QuickAppsSection
import com.timas.superapp.screens.LoginScreen
import com.timas.superapp.screens.QrScannerScreen
import com.timas.superapp.screens.SplashScreen
import com.timas.superapp.screens.ZekiiScreen
import com.timas.superapp.screens.SesliKitapScreen
import com.timas.superapp.screens.EBookScreen
import com.timas.superapp.screens.OkumaKulubuScreen
import com.timas.superapp.ui.theme.TimasTheme

private enum class AppScreen { HOME, LOGIN, QR_SCANNER, ZEKII, SESLI_KITAP, E_BOOK, OKUMA_KULUBU }

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
                    var showLoginSheet by remember { mutableStateOf(false) }
                    var scannedQrResult by remember { mutableStateOf<String?>(null) }
                    var searchQuery by remember { mutableStateOf("") }
                    var selectedBottomTab by remember { mutableStateOf(0) }
                    val focusManager = LocalFocusManager.current
                    
                    val snackbarHostState = remember { SnackbarHostState() }
                    val coroutineScope = rememberCoroutineScope()

                    if (showLoginSheet) {
                        Dialog(
                            onDismissRequest = { showLoginSheet = false },
                            properties = DialogProperties(usePlatformDefaultWidth = false)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .widthIn(max = 420.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .wrapContentHeight(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color.White
                            ) {
                                LoginScreen(
                                    onBack = { showLoginSheet = false },
                                    onLoginSuccess = { showLoginSheet = false }
                                )
                            }
                        }
                    }

                    scannedQrResult?.let { result ->
                        AlertDialog(
                            onDismissRequest = { scannedQrResult = null },
                            title = { Text("QR Kod Okundu") },
                            text = { Text("Değer: $result") },
                            confirmButton = {
                                TextButton(onClick = { scannedQrResult = null }) {
                                    Text("Tamam")
                                }
                            }
                        )
                    }

                    when (currentScreen) {
                        AppScreen.ZEKII -> {
                            ZekiiScreen(onBack = { currentScreen = AppScreen.HOME })
                        }
 
                        AppScreen.SESLI_KITAP -> {
                            SesliKitapScreen(onBack = { currentScreen = AppScreen.HOME })
                        }
 
                        AppScreen.E_BOOK -> {
                            EBookScreen(onBack = { currentScreen = AppScreen.HOME })
                        }
                        
                        AppScreen.OKUMA_KULUBU -> {
                            OkumaKulubuScreen(onBack = { currentScreen = AppScreen.HOME })
                        }

                        AppScreen.LOGIN -> {
                            // LoginScreen is now a ModalBottomSheet
                            currentScreen = AppScreen.HOME
                        }

                        AppScreen.QR_SCANNER -> {
                            QrScannerScreen(
                                onBack = { currentScreen = AppScreen.HOME },
                                onQrScanned = { result ->
                                    scannedQrResult = result
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
                                snackbarHost = { SnackbarHost(snackbarHostState) },
                                topBar = {
                                    SuperAppToolbar(
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { searchQuery = it },
                                        onProfileClick = { showLoginSheet = true }
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
                                if (selectedBottomTab == 1) {
                                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                                        com.timas.superapp.screens.SearchDiscoverScreen(searchQuery = searchQuery)
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(innerPadding)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        SuperAppSlider()
                                        QuickAppsSection(
                                            onNavigateToZekii = { currentScreen = AppScreen.ZEKII },
                                            onNavigateToSesliKitap = { currentScreen = AppScreen.SESLI_KITAP },
                                            onNavigateToEBook = { currentScreen = AppScreen.E_BOOK },
                                            onNavigateToOkumaKulubu = { currentScreen = AppScreen.OKUMA_KULUBU }
                                        )
    
                                        Spacer(modifier = Modifier.height(16.dp))
                                        com.timas.superapp.components.HomeDashboardSection(
                                            onKatilClick = { eventTitle ->
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("$eventTitle etkinliğine katılım talebiniz alındı.")
                                                }
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        com.timas.superapp.components.KategorilerSection()
                                        Spacer(modifier = Modifier.height(16.dp))
                                        com.timas.superapp.components.GamesAndTvSection()
                                        
                                        Spacer(modifier = Modifier.height(16.dp))
                                        com.timas.superapp.components.KampanyalarSection()
                                        
                                        Spacer(modifier = Modifier.height(32.dp))
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
