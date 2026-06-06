package com.timas.superapp.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlin.math.absoluteValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SuperAppSlider() {
    // Örnek slider öğeleri (Renkler ve başlıklar)
    val sliderItems = listOf(
        Pair("Büyük Yaz İndirimi", listOf(Color(0xFFff9a9e), Color(0xFFfecfef))),
        Pair("Yeni Sezon Ürünleri", listOf(Color(0xFFa18cd1), Color(0xFFfbc2eb))),
        Pair("Fırsat Ürünleri %50", listOf(Color(0xFF84fab0), Color(0xFF8fd3f4))),
        Pair("Ücretsiz Kargo", listOf(Color(0xFFfccb90), Color(0xFFd57eeb)))
    )

    val actualItemCount = sliderItems.size
    val startIndex = Int.MAX_VALUE / 2
    val initialPage = startIndex - (startIndex % actualItemCount)

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )
    val coroutineScope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    // Ekran genişliğine göre duyarlı hesaplamalar
    val horizontalPadding = screenWidth * 0.15f // Ekranın %15'i kadar kenar boşluğu
    val cardWidth = screenWidth - (horizontalPadding * 2)
    val cardHeight = (cardWidth / 1.8f).coerceAtMost(220.dp) // 1.8 oran, tablette devasa olmaması için max 220dp
    val arrowPadding = max(4.dp, (horizontalPadding - 36.dp) / 2)

    // Otomatik kaydırma için
    LaunchedEffect(pagerState.settledPage) {
        delay(3000) // 3 saniyede bir kaydır
        pagerState.animateScrollToPage(pagerState.currentPage + 1)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight),
                pageSpacing = 16.dp,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = horizontalPadding)
            ) { page ->
                val actualPage = page % actualItemCount
                val item = sliderItems[actualPage]

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(item.second)),
                    contentAlignment = Alignment.BottomStart
                ) {
                    AsyncImage(
                        model = "https://cdn.timas.com.tr/other/kampanyalar.jpg",
                        contentDescription = item.first,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // Metin alanı
                    Text(
                        text = item.first,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Sol Ok
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = arrowPadding)
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.9f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Önceki",
                    tint = Color.Black
                )
            }

            // Sağ Ok
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = arrowPadding)
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.9f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Sonraki",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sayfa Göstergeleri (Dots)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val actualCurrentPage = pagerState.currentPage % actualItemCount
            repeat(actualItemCount) { iteration ->
                val isSelected = actualCurrentPage == iteration
                val color = if (isSelected) Color(0xFFF26122) else Color.LightGray
                val width = if (isSelected) 24.dp else 8.dp

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .height(8.dp)
                        .width(width)
                )
            }
        }
    }
}
