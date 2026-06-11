package com.timas.superapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class KategoriItem(val name: String, val color: Color)

@Composable
fun KategorilerSection() {
    val kategoriler = listOf(
        KategoriItem("Timaş", Color(0xFFE65100)),
        KategoriItem("Tarih", Color(0xFF795548)),
        KategoriItem("Timaş Akademi", Color(0xFF455A64)),
        KategoriItem("Sufi", Color(0xFF8D6E63)),
        KategoriItem("Antik", Color(0xFF1565C0)),
        KategoriItem("Portakal", Color(0xFFFF7043)),
        KategoriItem("Genç Timaş", Color(0xFF2E7D32)),
        KategoriItem("İlk Genç", Color(0xFF1976D2)),
        KategoriItem("CarpeDiem", Color(0xFF7E57C2)),
        KategoriItem("Timaş Çocuk", Color(0xFFF57F17)),
        KategoriItem("Mavi Kirpi", Color(0xFF00ACC1)),
        KategoriItem("Eğlenceli Bilgi", Color(0xFF00897B)),
        KategoriItem("Sincap Çocuk", Color(0xFFD81B60)),
        KategoriItem("Dikkat ve Zeka", Color(0xFF3949AB)),
        KategoriItem("Timaş İnanç", Color(0xFF424242)),
        KategoriItem("Gülce Çocuk", Color(0xFFAB47BC))
    )

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Text(
            text = "Kategoriler",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(kategoriler) { kategori ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(kategori.color.copy(alpha = 0.08f))
                        .border(1.dp, kategori.color.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                        .clickable { /* Handle click */ }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = kategori.name,
                        color = kategori.color,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
