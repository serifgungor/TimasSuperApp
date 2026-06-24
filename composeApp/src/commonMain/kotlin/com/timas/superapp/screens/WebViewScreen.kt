package com.timas.superapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timas.superapp.openUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    title: String,
    url: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Automatically attempt to open the link in a new tab when the screen is shown
    LaunchedEffect(url) {
        if (url.isNotEmpty()) {
            openUrl(url)
        }
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
                    IconButton(onClick = onBack) {
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF1F5F9))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFFFF0EB), shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = Color(0xFFF26122),
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sayfa Yeni Sekmede Açılıyor",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"$title\" portalına yönlendiriliyorsunuz. Tarayıcınız yeni sekmeyi engellediyse aşağıdaki butona tıklayabilirsiniz.",
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { openUrl(url) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF26122)),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sayfayı Manuel Aç",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onBack) {
                Text(text = "Geri Dön", color = Color(0xFF64748B), fontSize = 13.sp)
            }
        }
    }
}
