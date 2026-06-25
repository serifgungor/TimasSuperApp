package com.timas.superapp.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import kotlin.math.absoluteValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SuperAppSlider() {
    var activeUrl by remember { mutableStateOf<String?>(null) }
    var activeTitle by remember { mutableStateOf<String?>(null) }

    // Slider items: (Title, ImageUrl, TargetUrl)
    val sliderItems = listOf(
        Triple(
            "Timaş Çocuk",
            "https://cdn.timas.com.tr/other/timascocukapp-banner-nisan2026.jpg",
            "https://app.timascocuk.com/?utm_source=timascom&utm_medium=anasayfa-banner&utm_campaign=timascocukapp&utm_term=tanitim&utm_content=banner&dev=macos&ref=aHR0cHM6Ly90aW1hcy5jb20udHIv"
        ),
        Triple(
            "Mayıs Kitapları",
            "https://cdn.timas.com.tr/other/mayiskitaplari-banner-mayis2026.jpg",
            "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme"
        ),
        Triple(
            "Kampanyalar",
            "https://cdn.timas.com.tr/other/kampanyalar.jpg",
            "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme"
        ),
        Triple(
            "Timaş Europe",
            "https://cdn.timas.com.tr/other/timas-europe-banner-aralik2024.jpg",
            "https://www.timaseurope.com/?utm_source=turkiye+web+site&utm_medium=web+banner&utm_campaign=turkiye+timas+banner&utm_id=turkiye+timas+sitesi"
        ),
        Triple(
            "Okula Dönüş",
            "https://cdn.timas.com.tr/other/okul-banner.jpg",
            "https://timasokul.com/?utm_source=timascom+banner&utm_medium=anasayfa+banner&utm_campaign=timascom+banner"
        )
    )

    val actualItemCount = sliderItems.size
    val startIndex = Int.MAX_VALUE / 2
    val initialPage = startIndex - (startIndex % actualItemCount)

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )
    val coroutineScope = rememberCoroutineScope()

    // Otomatik kaydırma için
    LaunchedEffect(pagerState.settledPage) {
        delay(3000) // 3 saniyede bir kaydır
        pagerState.animateScrollToPage(pagerState.currentPage + 1)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    pageSpacing = 16.dp,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
                ) { page ->
                    val actualPage = page % actualItemCount
                    val item = sliderItems[actualPage]

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray)
                            .clickable {
                                if (item.third.isNotEmpty()) {
                                    activeUrl = item.third
                                    activeTitle = item.first
                                }
                            },
                        contentAlignment = Alignment.BottomStart
                    ) {
                        AsyncImage(
                            model = item.second,
                            contentDescription = item.first,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        // Metin alanı
                        Text(
                            text = item.first,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(12.dp)
                                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
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
                        .padding(start = 24.dp)
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
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
                        .padding(end = 24.dp)
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
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

    if (activeUrl != null) {
        FullScreenWebViewDialog(
            title = activeTitle.orEmpty(),
            url = activeUrl!!,
            onDismiss = {
                activeUrl = null
                activeTitle = null
            }
        )
    }
}


