package com.timas.superapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimasDijitalScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Timaş Dijital") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF4F6F8),
                    titleContentColor = Color(0xFF1E293B),
                    navigationIconContentColor = Color(0xFF1E293B)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6F8)) // Açık gri arka plan
                .padding(innerPadding)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Devices,
                contentDescription = "Timaş Dijital İkonu",
                modifier = Modifier.size(80.dp),
                tint = Color(0xFF75A9A9) // Teal/Adaçayı ikon rengi
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Timaş Dijital",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF11233B), // Koyu mavi başlık
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Dijital içerikler yakında burada!\nE-kitaplar, sesli kitaplar ve interaktif materyaller.",
                fontSize = 16.sp,
                color = Color(0xFF6B7280), // Gri açıklama metni
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}
