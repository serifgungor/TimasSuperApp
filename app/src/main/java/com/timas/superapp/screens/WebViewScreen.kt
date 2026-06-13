package com.timas.superapp.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    title: String,
    url: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading = remember { mutableStateOf(true) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    androidx.activity.compose.BackHandler(enabled = webViewInstance?.canGoBack() == true) {
        webViewInstance?.goBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (webViewInstance?.canGoBack() == true) {
                            webViewInstance?.goBack()
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color(0xFF1E293B),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8FAFC)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    @android.annotation.SuppressLint("SetJavaScriptEnabled")
                    val webView = WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadsImagesAutomatically = true
                        settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        
                        settings.useWideViewPort = true
                        settings.loadWithOverviewMode = true
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false

                        // Disable dark mode (force light theme) on the WebView
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            settings.isAlgorithmicDarkeningAllowed = false
                        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                            settings.forceDark = android.webkit.WebSettings.FORCE_DARK_OFF
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading.value = false
                                // Only inject landscape/small screen overflow CSS fix for Timaş Portal
                                if (url?.contains("portal.timas.com.tr") == true) {
                                    view?.evaluateJavascript(
                                        """
                                        (function() {
                                            var style = document.createElement('style');
                                            style.type = 'text/css';
                                            style.innerHTML = 'html { height: 100% !important; overflow: hidden !important; } body { height: auto !important; min-height: 100% !important; overflow: auto !important; }';
                                            document.head.appendChild(style);
                                        })()
                                        """.trimIndent(),
                                        null
                                    )
                                }
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: android.webkit.WebResourceRequest?,
                                error: android.webkit.WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                if (request?.isForMainFrame == true) {
                                    isLoading.value = false
                                    val errorHtml = """
                                        <!DOCTYPE html>
                                        <html>
                                        <head>
                                            <meta name="viewport" content="width=device-width, initial-scale=1">
                                            <style>
                                                body { background-color: #F8FAFC; color: #1E293B; font-family: sans-serif; display: flex; flex-direction: column; justify-content: center; align-items: center; height: 100vh; margin: 0; text-align: center; padding: 20px; box-sizing: border-box; }
                                                h2 { margin-bottom: 8px; color: #0F172A; }
                                                p { color: #64748B; margin-top: 0; font-size: 14px; }
                                                svg { width: 64px; height: 64px; fill: #94A3B8; margin-bottom: 16px; }
                                            </style>
                                        </head>
                                        <body>
                                            <svg viewBox="0 0 24 24"><path d="M11 15h2v2h-2zm0-8h2v6zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z"/></svg>
                                            <h2>Bağlantı Hatası</h2>
                                            <p>İçerik yüklenirken bir sorun oluştu.<br/>Lütfen internet bağlantınızı kontrol edip tekrar deneyin.</p>
                                        </body>
                                        </html>
                                    """.trimIndent()
                                    view?.loadDataWithBaseURL(null, errorHtml, "text/html", "utf-8", null)
                                }
                            }
                        }
                        webChromeClient = android.webkit.WebChromeClient()
                        loadUrl(url)
                    }
                    webViewInstance = webView
                    webView
                }
            )

            if (isLoading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFF26122))
                }
            }
        }
    }
}
