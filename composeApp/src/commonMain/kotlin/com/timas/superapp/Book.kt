package com.timas.superapp

import androidx.compose.ui.graphics.Color

data class Book(
    val title: String,
    val author: String,
    val coverUrl: String,
    val id: String = title.lowercase().replace(" ", "_").replace("'", "").replace("’", "").replace("\"", ""),
    val description: String = "",
    val progress: Int = -1, // -1: Not started, 0-100: Progress percentage
    val type: String = "E-Kitap & Sesli",
    val category: String = "",
    val color: Color = Color(0xFFF26122),
    val isOwned: Boolean = true, // Whether the user has unlocked/purchased this volume
    val volumeNumber: Int = 1, // Volume position in the series
    val pages: List<String> = emptyList(),
    val authorDetails: String = "",
    val shortSummary: String = "",
    val narrator: String = "Ufuk Bayraktar",
    val duration: String = "4 sa 32 dk",
    val totalSeconds: Int = 16320,
    val audioPositionSeconds: Float = 0f
) {
    fun getBookPages(): List<String> {
        if (pages.isNotEmpty()) return pages
        return listOf(
            "Kapak",
            "Bu eser, okuyucuyu zihinsel ve ruhsal bir yolculuğa çıkarmak amacıyla kaleme alınmıştır. Her satırda yeni bir düşünce kapısı aralanmaktadır. Bu kitap, okuyucunun hayal gücünü geliştirmek ve edebi bir yolculuğa çıkarmak amacıyla hazırlanmıştır.",
            "Bölüm 1: Yolun Başlangıcı. Her büyük macera, ilk adımla başlar. Hayatın karmaşası içinde kendimizi bulmak ve kendi yolumuzu çizmek en büyük erdemlerden biridir.",
            "Bölüm 2: Keşifler ve Düşünceler. Yazarın bu derinlikli eserinde, hayata dair pek çok önemli gözlem ve felsefi yaklaşım bulacaksınız. Karşılaştığımız zorluklar bizi zayıflatmaz, aksine karakterimizi olgunlaştırır.",
            "Bölüm 3: Son Söz. Yolculuk tamamlandığında, geriye kalan en değerli hazine edindiğimiz tecrübeler ve iç huzurdur. İç huzura ve bilgeliğe ulaşmak, kendimizle barışık olmaktan geçer."
        )
    }
}
