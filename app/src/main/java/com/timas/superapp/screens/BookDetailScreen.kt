package com.timas.superapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timas.superapp.Book

private val CreamBg = Color(0xFFF9F6F0)
private val TextDark = Color(0xFF2C2C2C)
private val TextGray = Color(0xFF5A5A5A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    book: Book,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = book.title.uppercase(),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD35400)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBg)
            )
        },
        containerColor = CreamBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Yazar
            Text(
                text = book.author,
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                color = TextDark
            )
            
            val configuration = androidx.compose.ui.platform.LocalConfiguration.current
            val isTablet = configuration.screenWidthDp >= 600

            // Kapak ve Önsöz / Yazar Görseli
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Kitap Kapağı
                Card(
                    modifier = if (isTablet) Modifier.width(240.dp) else Modifier.weight(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .aspectRatio(0.65f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(book.color)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = book.title.uppercase(),
                            fontFamily = FontFamily.Serif,
                            color = Color.White,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(32.dp))
                
                // Sağ taraf: Önsöz ve Yazar resmi
                Column(
                    modifier = if (isTablet) Modifier.weight(1f) else Modifier.weight(1.2f)
                ) {
                    Text(
                        text = "Önsöz",
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore.",
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        color = TextDark,
                        lineHeight = 20.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Kitap Özeti
            Text(
                text = "Kitap Özeti",
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed do eiusmod tempor incididunt ut labore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp,
                color = TextDark,
                lineHeight = 22.sp,
                textAlign = TextAlign.Justify
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Benzer Kitaplar
            Text(
                text = "Benzer Kitaplar",
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            val similarBooks = listOf(
                Book("Kürk Mantolu", "Sabahattin Ali", Color(0xFF6B2D45)),
                Book("Şeker Portakalı", "Vasconcelos", Color(0xFF2C3E50)),
                Book("Simyacı", "Paulo Coelho", Color(0xFF4A6D53)),
                Book("Suç ve Ceza", "Dostoyevski", Color(0xFF3498DB))
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(similarBooks) { sBook ->
                    Card(
                        modifier = Modifier.width(140.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.7f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(sBook.color)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = sBook.title,
                                    fontFamily = FontFamily.Serif,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = sBook.title,
                                fontFamily = FontFamily.Serif,
                                fontSize = 12.sp,
                                color = TextDark,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = sBook.author,
                                fontFamily = FontFamily.Serif,
                                fontSize = 10.sp,
                                color = TextGray,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("E-posta", fontFamily = FontFamily.Serif, fontSize = 14.sp, color = TextGray)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("S.S.S.", fontFamily = FontFamily.Serif, fontSize = 14.sp, color = TextGray)
                    Text("Şikayetler", fontFamily = FontFamily.Serif, fontSize = 14.sp, color = TextGray)
                }
            }
        }
    }
}


