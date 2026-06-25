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
import androidx.compose.foundation.Image
import org.jetbrains.compose.resources.painterResource
import timas.composeapp.generated.resources.Res
import timas.composeapp.generated.resources.*

data class KategoriItem(
    val name: String,
    val icon: org.jetbrains.compose.resources.DrawableResource,
    val url: String
)

@Composable
fun KategorilerSection() {
    var activeUrl by remember { mutableStateOf<String?>(null) }
    var activeTitle by remember { mutableStateOf<String?>(null) }

    val kategoriler = listOf(
        KategoriItem("Timaş Yayınları", Res.drawable.cat_timas_yayinlari, "https://timas.com.tr/timas-yayinlari"),
        KategoriItem("Timaş Tarih", Res.drawable.cat_tarih, "https://timas.com.tr/timas-tarih"),
        KategoriItem("Timaş Akademi", Res.drawable.cat_akademi, "https://timas.com.tr/akademi"),
        KategoriItem("Sufi Kitap", Res.drawable.cat_sufi, "https://timas.com.tr/sufi"),
        KategoriItem("Portakal Kitap", Res.drawable.cat_portakal, "https://timas.com.tr/portakal"),
        KategoriItem("Genç Timaş", Res.drawable.cat_genc, "https://timas.com.tr/genc-timas"),
        KategoriItem("İlk Genç Timaş", Res.drawable.cat_ilkgenc, "https://timas.com.tr/ilk-genclik"),
        KategoriItem("Carpe Diem Kitap", Res.drawable.cat_carpe_diem, "https://timas.com.tr/carpe-diem"),
        KategoriItem("Timaş Çocuk", Res.drawable.cat_timas_cocuk, "https://timas.com.tr/timas-cocuk"),
        KategoriItem("Mavi Kirpi Kitap", Res.drawable.cat_mavi_kirpi, "https://timas.com.tr/mavi-kirpi"),
        KategoriItem("Eğlenceli Bilgi", Res.drawable.cat_eglenceli_bilgi, "https://timas.com.tr/eglenceli-bilgi"),
        KategoriItem("Sincap Kitap", Res.drawable.cat_sincap, "https://timas.com.tr/sincap"),
        KategoriItem("Timaş İnanç", Res.drawable.cat_inanc, "https://timas.com.tr/timas-inanc"),
        KategoriItem("Gülce Kitap", Res.drawable.cat_gulce, "https://timas.com.tr/gulce-kitap"),
        KategoriItem("Dikkat ve Zeka Akademisi", Res.drawable.cat_dikkat_zeka, "https://timas.com.tr/dikkat-ve-zeka-akademisi"),
        KategoriItem("Antik Kitap", Res.drawable.cat_antik, "https://timas.com.tr/antik-kitap")
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
                            if (kategori.url.isNotEmpty()) {
                                activeUrl = kategori.url
                                activeTitle = kategori.name
                            }
                        }
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(kategori.icon),
                        contentDescription = kategori.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
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

