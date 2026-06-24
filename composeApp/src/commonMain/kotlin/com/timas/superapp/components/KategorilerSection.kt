package com.timas.superapp.components

import com.timas.superapp.openUrl
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

data class KategoriItem(val name: String, val icon: String, val url: String)

@Composable
fun KategorilerSection() {
    val kategoriler = listOf(
        KategoriItem("Timaş Yayınları", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/timasyayinlari-tr-1.svg", "https://timas.com.tr/timas-yayinlari"),
        KategoriItem("Timaş Tarih", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/tarih-tr-2.jpg", "https://timas.com.tr/timas-tarih"),
        KategoriItem("Timaş Akademi", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/akademi-tr-4.jpg", "https://timas.com.tr/akademi"),
        KategoriItem("Sufi Kitap", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/sufi-tr-5.jpg", "https://timas.com.tr/sufi"),
        KategoriItem("Portakal Kitap", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/portakal-tr-16.jpg", "https://timas.com.tr/portakal"),
        KategoriItem("Genç Timaş", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/genc-tr-6.jpg", "https://timas.com.tr/genc-timas"),
        KategoriItem("İlk Genç Timaş", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/ilkgenc-tr-7.jpg", "https://timas.com.tr/ilk-genclik"),
        KategoriItem("Carpe Diem Kitap", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/carpe-tr-14.jpg", "https://timas.com.tr/carpe-diem"),
        KategoriItem("Timaş Çocuk", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/timascocuk-tr-8.jpg", "https://timas.com.tr/timas-cocuk"),
        KategoriItem("Mavi Kirpi Kitap", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/mavikirpi-tr-11.jpg", "https://timas.com.tr/mavi-kirpi"),
        KategoriItem("Eğlenceli Bilgi", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/eglenceli-tr-9.jpg", "https://timas.com.tr/eglenceli-bilgi"),
        KategoriItem("Sincap Kitap", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/sincap-tr-13.jpg", "https://timas.com.tr/sincap"),
        KategoriItem("Timaş İnanç", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/inanc-tr-3.jpg", "https://timas.com.tr/timas-inanc"),
        KategoriItem("Gülce Kitap", "https://satinal.timas.com.tr/Data/BlockUploadData/slider/img1/759/gulce-tr-10.jpg", "https://timas.com.tr/gulce-kitap"),
        KategoriItem("Dikkat ve Zeka Akademisi", "https://cdn.timas.com.tr/dikkat-zeka-akademisi.svg", "https://timas.com.tr/dikkat-ve-zeka-akademisi"),
        KategoriItem("Antik Kitap", "https://cdn.timas.com.tr/antik.svg", "https://timas.com.tr/antik-kitap")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(kategoriler) { kategori ->
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .border(2.dp, Color(0xFFF26122), CircleShape)
                        .background(Color.White, CircleShape)
                        .clip(CircleShape)
                        .clickable {
                            openUrl(kategori.url)
                        }
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = kategori.icon,
                        contentDescription = kategori.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
