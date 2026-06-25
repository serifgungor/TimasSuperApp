package com.timas.superapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import com.timas.superapp.components.*
import com.timas.superapp.screens.*
import com.timas.superapp.ui.theme.TimasTheme

private enum class AppScreen { HOME, LOGIN, ZEKII, SESLI_KITAP, E_BOOK, OKUMA_KULUBU, WEB_VIEW }

@Composable
fun App() {
    TimasTheme {
        var showSplash by remember { mutableStateOf(true) }
        var currentScreen by remember { mutableStateOf(AppScreen.HOME) }

        if (showSplash) {
            SplashScreen(onSplashFinished = { showSplash = false })
        } else {
            var showLoginSheet by remember { mutableStateOf(false) }
            var showQrDialog by remember { mutableStateOf(false) }
            var scannedQrResult by remember { mutableStateOf<String?>(null) }
            var searchQuery by remember { mutableStateOf("") }
            var selectedBottomTab by remember { mutableStateOf(0) }
            val focusManager = LocalFocusManager.current
            
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()
            val homeScrollState = rememberScrollState()
            var webViewUrl by remember { mutableStateOf("") }
            var webViewTitle by remember { mutableStateOf("") }

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

            if (showQrDialog) {
                Dialog(
                    onDismissRequest = { showQrDialog = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Surface(
                        modifier = Modifier
                            .width(320.dp)
                            .height(420.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(2.dp, Color(0xFFF26122), RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.Black
                    ) {
                        QrScannerScreen(
                            onBack = { showQrDialog = false },
                            onQrScanned = { result ->
                                scannedQrResult = result
                                showQrDialog = false
                            }
                        )
                    }
                }
            }

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { focusManager.clearFocus() })
                    },
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    SuperAppBottomNav(
                        selectedIndex = if (currentScreen == AppScreen.HOME) selectedBottomTab else -1,
                        onTabSelected = { 
                            selectedBottomTab = it
                            currentScreen = AppScreen.HOME
                        },
                        onHomeClick = {
                            selectedBottomTab = 0
                            currentScreen = AppScreen.HOME
                        },
                        onSearchClick = {
                            selectedBottomTab = 1
                            currentScreen = AppScreen.HOME
                        },
                        onCartClick = {
                            selectedBottomTab = 2
                            currentScreen = AppScreen.HOME
                        },
                        onMenuClick = {
                            selectedBottomTab = 3
                            currentScreen = AppScreen.HOME
                        },
                        onQrClick = { showQrDialog = true }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
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

                        AppScreen.WEB_VIEW -> {
                            WebViewScreen(
                                title = webViewTitle,
                                url = webViewUrl,
                                onBack = { currentScreen = AppScreen.HOME }
                            )
                        }
 
                        AppScreen.LOGIN -> {
                            currentScreen = AppScreen.HOME
                        }
 
                        AppScreen.HOME -> {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                topBar = {
                                    SuperAppToolbar(
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { searchQuery = it },
                                        onProfileClick = { showLoginSheet = true }
                                    )
                                }
                            ) { homePadding ->
                                if (selectedBottomTab == 1) {
                                    Box(modifier = Modifier.padding(homePadding).fillMaxSize()) {
                                        SearchDiscoverScreen(searchQuery = searchQuery)
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(homePadding)
                                            .verticalScroll(homeScrollState)
                                    ) {
                                        SuperAppSlider()
                                        KategorilerSection()
                                        Spacer(modifier = Modifier.height(16.dp))
                                        HomeDashboardSection(
                                            onKatilClick = { eventTitle ->
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("$eventTitle etkinliğine katılım talebiniz alındı.")
                                                }
                                            },
                                            onNavigateToEBook = { currentScreen = AppScreen.E_BOOK },
                                            onNavigateToSesliKitap = { currentScreen = AppScreen.SESLI_KITAP }
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        QuickAppsSection(
                                            onNavigateToZekii = { currentScreen = AppScreen.ZEKII },
                                            onNavigateToSesliKitap = { currentScreen = AppScreen.SESLI_KITAP },
                                            onNavigateToEBook = { currentScreen = AppScreen.E_BOOK },
                                            onNavigateToOkumaKulubu = { currentScreen = AppScreen.OKUMA_KULUBU }
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        GamesAndTvSection()
                                        
                                        Spacer(modifier = Modifier.height(16.dp))
                                        KampanyalarSection()
                                        
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
