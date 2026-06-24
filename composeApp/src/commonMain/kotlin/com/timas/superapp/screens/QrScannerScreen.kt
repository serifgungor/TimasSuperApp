package com.timas.superapp.screens

import androidx.compose.ui.text.TextStyle

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QrScannerScreen(
    onBack: () -> Unit,
    onQrScanned: (String) -> Unit = {}
) {
    var customQrValue by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var isScanning by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "laser_transition")
    val laserPositionY by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_y"
    )

    val sampleQrs = listOf(
        "Malamander" to "https://cdn.timas.com.tr/urun/malamander-bez-cilt-somizli-9786050838848.jpg",
        "Gargantis" to "https://cdn.timas.com.tr/urun/gargantis-9786050838039.jpg",
        "Nar Ağacı" to "https://cdn.timas.com.tr/urun/nar-agaci-bez-ciltli-9786050832914.jpg",
        "Mutluluğun İnşası" to "https://cdn.timas.com.tr/urun/mutlulugun-insasi-9786050849745.jpg"
    )

    fun startScan(value: String) {
        if (isScanning) return
        coroutineScope.launch {
            isScanning = true
            delay(1200) // Simulate scanning processing time
            isScanning = false
            onQrScanned(value)
            onBack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E293B)) // Premium dark slate background
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Üst bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "QR Kodu Tarat (Web Simülatör)",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // 2. Kamera Viewport Kutusu (Animasyonlu Lazer)
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, Color(0xFFF26122), RoundedCornerShape(24.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (isScanning) {
                    CircularProgressIndicator(color = Color(0xFFF26122))
                } else {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "Scan",
                        tint = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(72.dp)
                    )
                }

                // Hareketli Lazer Animasyonu
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithContent {
                            drawContent()
                            val y = size.height * laserPositionY
                            drawLine(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFFF26122),
                                        Color(0xFFF26122),
                                        Color.Transparent
                                    )
                                ),
                                start = Offset(10f, y),
                                end = Offset(size.width - 10f, y),
                                strokeWidth = 3.dp.toPx()
                            )
                        }
                )
            }

            Text(
                text = "Taramayı simüle etmek için aşağıdaki örneklerden birini seçin ya da özel bir metin girin.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // 3. Simülasyon Seçenekleri
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Hazır Kitap Kodları:",
                    color = Color(0xFFFF9F43),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                sampleQrs.forEach { (name, value) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { startScan(value) },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2E3D52)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text(text = "Tarat", color = Color(0xFFFF9F43), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)

            // 4. Özel Değer Girişi
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Özel QR Değeri Girin:",
                    color = Color(0xFFFF9F43),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = customQrValue,
                    onValueChange = { customQrValue = it },
                    placeholder = { Text("QR metni veya URL girin", color = Color.White.copy(0.4f), fontSize = 12.sp) },
                    textStyle = TextStyle(color = Color.White, fontSize = 13.sp),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF26122),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedContainerColor = Color(0xFF2E3D52),
                        unfocusedContainerColor = Color(0xFF2E3D52)
                    ),
                    singleLine = true
                )

                Button(
                    onClick = { if (customQrValue.isNotBlank()) startScan(customQrValue) },
                    enabled = customQrValue.isNotBlank() && !isScanning,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF26122)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Değeri Çöz ve Gönder", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
