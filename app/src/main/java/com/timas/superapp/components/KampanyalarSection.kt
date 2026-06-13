package com.timas.superapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class KampanyaItem(val title: String, val imageUrl: String)

@Composable
fun KampanyalarSection() {
    val kampanyalar = listOf(
        KampanyaItem("Timaş Çocuk", "https://cdn.timas.com.tr/other/timascocukapp-banner-nisan2026.jpg"),
        KampanyaItem("Mayıs Kitapları", "https://cdn.timas.com.tr/other/mayiskitaplari-banner-mayis2026.jpg"),
        KampanyaItem("Kampanyalar", "https://cdn.timas.com.tr/other/kampanyalar.jpg"),
        KampanyaItem("Timaş Europe", "https://cdn.timas.com.tr/other/timas-europe-banner-aralik2024.jpg"),
        KampanyaItem("Okula Dönüş", "https://cdn.timas.com.tr/other/okul-banner.jpg"),
        // Yedek placeholder resimler
        KampanyaItem("Fırsatlar", "https://picsum.photos/id/10/600/300"),
        KampanyaItem("Yeni Gelenler", "https://picsum.photos/id/20/600/300"),
        KampanyaItem("Çok Satanlar", "https://picsum.photos/id/30/600/300")
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
            modifier = Modifier.height(340.dp) // Increased height to make campaigns larger
        ) {
            items(kampanyalar) { kampanya ->
                KampanyaCard(kampanya = kampanya)
            }
        }
    }
}

@Composable
fun KampanyaCard(kampanya: KampanyaItem) {
    Box(
        modifier = Modifier
            .width(400.dp) // Adjusted to 400dp to fit the native banner aspect ratio (approx 2.4:1)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F5F9))
            .clickable { /* Kampanyaya git */ },
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
