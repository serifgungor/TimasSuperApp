package com.timas.superapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

data class KampanyaItem(val title: String, val imageUrl: String, val targetUrl: String = "")

@Composable
fun KampanyalarSection() {
    var activeUrl by remember { mutableStateOf<String?>(null) }
    var activeTitle by remember { mutableStateOf<String?>(null) }

    val kampanyalar = listOf(
        KampanyaItem(
            title = "Timaş Çocuk",
            imageUrl = "https://cdn.timas.com.tr/other/timascocukapp-banner-nisan2026.jpg",
            targetUrl = "https://app.timascocuk.com/?utm_source=timascom&utm_medium=anasayfa-banner&utm_campaign=timascocukapp&utm_term=tanitim&utm_content=banner&dev=macos&ref=aHR0cHM6Ly90aW1hcy5jb20udHIv"
        ),
        KampanyaItem(
            title = "Mayıs Kitapları",
            imageUrl = "https://cdn.timas.com.tr/other/mayiskitaplari-banner-mayis2026.jpg",
            targetUrl = "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme"
        ),
        KampanyaItem(
            title = "Kampanyalar",
            imageUrl = "https://cdn.timas.com.tr/other/kampanyalar.jpg",
            targetUrl = "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme"
        ),
        KampanyaItem(
            title = "Timaş Europe",
            imageUrl = "https://cdn.timas.com.tr/other/timas-europe-banner-aralik2024.jpg",
            targetUrl = "https://www.timaseurope.com/?utm_source=turkiye+web+site&utm_medium=web+banner&utm_campaign=turkiye+timas+banner&utm_id=turkiye+timas+sitesi"
        ),
        KampanyaItem(
            title = "Okula Dönüş",
            imageUrl = "https://cdn.timas.com.tr/other/okul-banner.jpg",
            targetUrl = "https://timasokul.com/?utm_source=timascom+banner&utm_medium=anasayfa+banner&utm_campaign=timascom+banner"
        ),
        // Yedek placeholder resimler
        KampanyaItem("Fırsatlar", "https://picsum.photos/id/10/600/300", "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme"),
        KampanyaItem("Yeni Gelenler", "https://picsum.photos/id/20/600/300", "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme"),
        KampanyaItem("Çok Satanlar", "https://picsum.photos/id/30/600/300", "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme"),
        KampanyaItem("Yaz Okumaları", "https://picsum.photos/id/1025/600/300", "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme"),
        KampanyaItem("Haftanın Fırsatı", "https://picsum.photos/id/367/600/300", "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme"),
        KampanyaItem("Edebiyat Kulübü", "https://picsum.photos/id/364/600/300", "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme"),
        KampanyaItem("Süper İndirimler", "https://picsum.photos/id/42/600/300", "https://satinal.timas.com.tr/kampanyalar?utm_source=timascom+anasayfa&utm_medium=online+kampanyalar&utm_campaign=timascomwebsite+yonlendirme")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Kampanyalar",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 12.dp)
        )

        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(340.dp)
        ) {
            items(kampanyalar) { kampanya ->
                KampanyaCard(kampanya = kampanya, onClick = {
                    if (kampanya.targetUrl.isNotEmpty()) {
                        activeUrl = kampanya.targetUrl
                        activeTitle = kampanya.title
                    }
                })
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

@Composable
fun KampanyaCard(kampanya: KampanyaItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(400.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F5F9))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = kampanya.imageUrl,
            contentDescription = kampanya.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
