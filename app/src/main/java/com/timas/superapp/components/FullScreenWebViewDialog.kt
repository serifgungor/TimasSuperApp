package com.timas.superapp.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun FullScreenWebViewDialog(
    title: String,
    url: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        )
    ) {
        val showWebView = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
        
        androidx.compose.runtime.LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(400) // Dialog animasyonunun bitmesini bekle, böylece WebView ana thread'i kilitlediğinde titreme (gidip gelme) olmaz.
            showWebView.value = true
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .systemBarsPadding()
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Kapat",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onDismiss() },
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
            
            // Divider
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE2E8F0)))

            // WebView
            if (showWebView.value) {
                AndroidView(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    factory = { context ->
                        @android.annotation.SuppressLint("SetJavaScriptEnabled")
                        val webView = WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.loadsImagesAutomatically = true
                            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            
                            // Ekran sığdırma ve ölçeklendirme ayarları
                            settings.useWideViewPort = true
                            settings.loadWithOverviewMode = true
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false

                            webViewClient = WebViewClient()
                            webChromeClient = android.webkit.WebChromeClient()
                            loadUrl(url)
                        }
                        webView
                    },
                    update = { webView ->
                        // Optional update logic
                    }
                )
            } else {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.CircularProgressIndicator(color = Color.Gray)
                }
            }
        }
    }
}
