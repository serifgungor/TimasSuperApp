package com.timas.superapp.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.timas.superapp.Book
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

// ---------------- LOCAL HELPER CLASSES ----------------

data class BookStats(
    val pages: Int,
    val audioDuration: String,
    val year: String,
    val rating: String,
    val authorBiography: String
)

data class BookReview(
    val user: String,
    val ratingStars: String,
    val comment: String
)

// ---------------- ROOT SCREEN ----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    book: Book,
    onBack: () -> Unit,
    onStartReading: (Book) -> Unit = {},
    onStartListening: (Book) -> Unit = {},
    isLightMode: Boolean = false
) {
    val bgColor = if (isLightMode) Color(0xFFF8FAFC) else Color(0xFF18100C)
    val surfaceColor = if (isLightMode) Color(0xFFFFF0E5) else Color(0xFF261D1A)
    val textColor = if (isLightMode) Color(0xFF1E293B) else Color.White
    val cardBgColor = if (isLightMode) Color(0xFFFFF0E5) else Color(0xFF261D1A)
    val subtitleColor = if (isLightMode) Color(0xFF64748B) else Color(0xFF94A3B8)
    val topBarColor = if (isLightMode) Color(0xFFFFF0E5) else Color(0xFF261D1A)

    val context = LocalContext.current
    
    // We bind local book state so users can click similar books and update the view dynamically
    var currentBook by remember(book) { mutableStateOf(book) }
    var isLiked by remember { mutableStateOf(false) }
    var selectedTabState by remember { mutableStateOf(0) } // 0: Detaylar, 1: Önsöz, 2: Yazar, 3: Yorumlar

    val stats = remember(currentBook) { getBookDetailStats(currentBook.title) }
    val preface = remember(currentBook) { getBookPreface(currentBook.title) }
    val reviews = remember(currentBook) { getBookReviews(currentBook.title) }

    val similarBooks = remember {
        listOf(
            Book(
                title = "Nar Ağacı",
                author = "Nazan Bekiroğlu",
                coverUrl = "https://cdn.timas.com.tr/urun/nar-agaci-9786050807073.jpg",
                description = "Trabzon-Tebriz-Tiflis-Batum-İstanbul hattında geçen, I. Dünya Savaşı'nın gölgesinde büyüleyici bir aşk ve tarih romanı.",
                color = Color(0xFF9B59B6),
                type = "E-Kitap & Sesli",
                category = "Tarih & Roman"
            ),
            Book(
                title = "Dirilt Kalbini",
                author = "Nouman Ali Khan",
                coverUrl = "https://cdn.timas.com.tr/urun/dirilt-kalbini-9786050825992.jpg",
                description = "Modern çağın karmaşasında kalbini Kur'an ile diriltmek isteyenler için ufuk açıcı bir eser.",
                color = Color(0xFF34495E),
                type = "E-Kitap & Sesli",
                category = "İnanç & Düşünce"
            ),
            Book(
                title = "Öz Saygı Dersleri",
                author = "Yoon Hong Gyun",
                coverUrl = "https://cdn.timas.com.tr/urun/oz-saygi-dersleri-9786050849851.jpg",
                description = "Koreli psikiyatrist Yoon Hong Gyun'un öz saygıyı kazanma ve hayata daha güvenli bakma rehberi.",
                color = Color(0xFF7F8C8D),
                type = "E-Kitap & Sesli",
                category = "Kişisel Gelişim"
            ),
            Book(
                title = "Mutluluğun İnşası",
                author = "Mecit Ömür Öztürk",
                coverUrl = "https://cdn.timas.com.tr/urun/mutlulugun-insasi-9786050849745.jpg",
                description = "Mutluluk bir durum değil, bir yolculuktur. Bu kitapta, iç huzuru bulmanın ve gerçek mutluluğu inşa etmenin adımlarını öğreneceksiniz.",
                color = Color(0xFFE67E22),
                type = "E-Kitap & Sesli",
                category = "Kişisel Gelişim"
            )
        )
    }

    Scaffold(
        containerColor = bgColor // Matching dark premium theme
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. HERO IMAGE (COVER & BASIC INFO)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                SubcomposeAsyncImage(
                    model = currentBook.coverUrl,
                    contentDescription = currentBook.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = { LocalCoverFallback(book = currentBook) },
                    error = { LocalCoverFallback(book = currentBook) }
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                                startY = 200f
                            )
                        )
                )

                // Back button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.4f), androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White)
                }

                // Metadata details over image
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    // Type Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFF9F43).copy(alpha = 0.85f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = currentBook.type,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentBook.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 30.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentBook.author,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Rating stars
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB900), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${stats.rating} / 5.0",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(140 Yorum)",
                            color = textColor,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Rest of the content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {

            // 2. DETAILED METADATA GRID PANEL (Pages, Format, Year, Lang)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(surfaceColor)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetailTileItem(
                    label = "Sayfa",
                    value = "${stats.pages} s.",
                    icon = Icons.Default.Description,
                    textColor = textColor,
                    subtitleColor = subtitleColor,
                    modifier = Modifier.weight(1f)
                )
                DetailTileItem(
                    label = "Süre",
                    value = stats.audioDuration,
                    icon = Icons.Default.Headphones,
                    textColor = textColor,
                    subtitleColor = subtitleColor,
                    modifier = Modifier.weight(1f)
                )
                DetailTileItem(
                    label = "Yıl",
                    value = stats.year,
                    icon = Icons.Default.CalendarToday,
                    textColor = textColor,
                    subtitleColor = subtitleColor,
                    modifier = Modifier.weight(1f)
                )
                DetailTileItem(
                    label = "Dil",
                    value = "Türkçe",
                    icon = Icons.Default.Language,
                    textColor = textColor,
                    subtitleColor = subtitleColor,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. MAIN DYNAMIC ACTIONS HUB
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // E-Book Button
                Button(
                    onClick = { onStartReading(currentBook) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27AE60)), // Warm Green for E-Books
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "E-Kitap Oku",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }

                // Audiobook Button
                Button(
                    onClick = { onStartListening(currentBook) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9F43)), // Timaş Orange/Gold for Audiobooks
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sesli Dinle",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }

                // Favorite Button
                IconButton(
                    onClick = {
                        isLiked = !isLiked
                        Toast.makeText(
                            context,
                            if (isLiked) "Favorilere eklendi" else "Favorilerden çıkarıldı",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(surfaceColor)
                ) {
                    val heartScale by animateFloatAsState(
                        targetValue = if (isLiked) 1.25f else 1.0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                        label = "heart"
                    )
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Beğen",
                        tint = if (isLiked) Color(0xFFEF4444) else textColor,
                        modifier = Modifier.graphicsLayer(scaleX = heartScale, scaleY = heartScale)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 4. INFORMATION TAB SELECTOR
            val tabLabels = listOf("Detaylar", "Önsöz", "Yazar", "Yorumlar")
            ScrollableTabRow(
                selectedTabIndex = selectedTabState,
                containerColor = Color.Transparent,
                contentColor = Color(0xFFFF9F43),
                divider = {},
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabState])
                            .height(3.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF9F43))
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                tabLabels.forEachIndexed { index, label ->
                    val isSelected = selectedTabState == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTabState = index },
                        text = {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. TAB CONTENTS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            ) {
                when (selectedTabState) {
                    0 -> { // Synopsis / Details
                        Column {
                            Text(
                                text = currentBook.description.ifEmpty { "Bu kitap için detaylı bir açıklama bulunmamaktadır." },
                                fontSize = 13.sp,
                                color = textColor,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Justify
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Kategori: ${currentBook.category}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF9F43)
                            )
                        }
                    }
                    1 -> { // Preface
                        Text(
                            text = preface,
                            fontSize = 13.sp,
                            color = textColor,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    2 -> { // Author Biography
                        Text(
                            text = stats.authorBiography,
                            fontSize = 13.sp,
                            color = textColor,
                            lineHeight = 20.sp
                        )
                    }
                    3 -> { // User Reviews
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            reviews.forEach { r ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = r.user, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                                            Text(text = r.ratingStars, fontSize = 11.sp, color = Color(0xFFFFB900))
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = r.comment, fontSize = 12.sp, color = textColor, lineHeight = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 6. RECOMMENDED / SIMILAR BOOKS CAROUSEL (Infinite loop selection)
            Text(
                text = "Benzer Kitaplar",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(similarBooks) { sBook ->
                    Card(
                        modifier = Modifier
                            .width(110.dp)
                            .clickable {
                                // Load similar book details in this page dynamically!
                                currentBook = sBook
                                selectedTabState = 0
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBgColor)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.7f)
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                SubcomposeAsyncImage(
                                    model = sBook.coverUrl,
                                    contentDescription = sBook.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    loading = { LocalCoverFallback(book = sBook) },
                                    error = { LocalCoverFallback(book = sBook) }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = sBook.title,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = sBook.author,
                                fontSize = 8.sp,
                                color = Color(0xFF64748B),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

// ---------------- LOCAL BOOK COVER FALLBACK ----------------

@Composable
private fun LocalCoverFallback(book: Book, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        book.color,
                        book.color.copy(alpha = 0.6f)
                    )
                )
            )
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(14.dp)
                )
                Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.title,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.Serif,
                lineHeight = 12.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = book.author,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ---------------- LOCAL METADATA RESOLVERS ----------------

private fun getBookDetailStats(bookTitle: String): BookStats {
    return when (bookTitle) {
        "Mutluluğun İnşası" -> BookStats(180, "4.5 Saat", "2024", "4.8", "Mecit Ömür Öztürk, modern psikoloji ve tasavvuf ahlakını harmanlayan eserleriyle tanınan araştırmacı yazardır.")
        "Dilin Afetleri" -> BookStats(120, "3.2 Saat", "2023", "4.9", "İmam Gazzâlî (1058-1111), İslam dünyasının en büyük mütefekkir, fıkıhçı ve kelamcılarından biridir.")
        "Kur'an Atlası" -> BookStats(280, "7.7 Saat", "2022", "4.7", "Timaş Yayınları Akademik Araştırma Heyeti tarafından coğrafya ve tefsir bilimi ışığında hazırlanmıştır.")
        "Kalpsizler" -> BookStats(400, "10.2 Saat", "2021", "4.6", "Marissa Meyer, dünya çapında en çok satan fantastik ve distopik gençlik romanlarının yazarıdır.")
        "Tavuk Bacaklı Ev Kaçıyor" -> BookStats(250, "6.6 Saat", "2020", "4.7", "Sophie Anderson, geleneksel halk hikayelerini modern fantastik çocuk romanlarına uyarlayan ödüllü bir İngiliz yazardır.")
        "Ağaçların Fısıltısı" -> BookStats(200, "3.2 Saat", "2021", "4.8", "Murat Moroğlu, çocuk edebiyatı ve doğa bilinci aşılayan masallarıyla tanınan ödüllü yazardır.")
        "Öz Saygı Dersleri" -> BookStats(312, "7.2 Saat", "2023", "4.9", "Yoon Hong Gyun, Koreli psikiyatrist ve ruh sağlığı alanında çok satan kişisel gelişim kitaplarının yazarıdır.")
        "Nar Ağacı" -> BookStats(540, "14.5 Saat", "2012", "4.9", "Nazan Bekiroğlu, Trabzon doğumlu akademisyen, yazar ve edebiyat profesörüdür.")
        "Dirilt Kalbini" -> BookStats(160, "5.1 Saat", "2017", "4.9", "Nouman Ali Khan, ABD merkezli Arapça ve Kur'an araştırmaları enstitüsü Bayyinah'ın kurucusudur.")
        "İçimdeki Müzik" -> BookStats(256, "6.2 Saat", "2016", "4.9", "Sharon M. Draper, Amerikalı çocuk ve gençlik edebiyatı yazarı, aynı zamanda ödüllü bir eğitimcidir.")
        "Bırak ve Rahatla" -> BookStats(240, "5.5 Saat", "2018", "4.8", "Adem Güneş, pedagoji ve çocuk ruh sağlığı üzerine yazdığı kitaplarla tanınan aile danışmanı ve yazardır.")
        "Mücella" -> BookStats(348, "9.2 Saat", "2015", "4.9", "Nazan Bekiroğlu, Trabzon doğumlu akademisyen, yazar ve edebiyat profesörüdür.")
        "Kiraz Ağacı ile Aramızdaki Mesafe" -> BookStats(200, "4.7 Saat", "2018", "4.9", "Paola Peretti, çocuk edebiyatı alanında uluslararası üne sahip İtalyan yazardır.")
        "Göğü Yere İndirelim" -> BookStats(192, "4.1 Saat", "2017", "4.8", "Özgür Balpınar, gençlik edebiyatı ve doğa masalları yazarıdır.")
        "Huzur Sokağı" -> BookStats(480, "12.2 Saat", "1970", "4.9", "Şule Yüksel Şenler (1938-2019), Türk yazar, aktivist ve gazetecidir.")
        "Yusuf ile Züleyha" -> BookStats(360, "8.5 Saat", "2008", "4.9", "Nazan Bekiroğlu, Trabzon doğumlu akademisyen, yazar ve edebiyat profesörüdür.")
        "Malamander" -> BookStats(304, "6.8 Saat", "2019", "4.8", "Thomas Taylor, İngiliz çocuk kitapları yazarı ve illüstratörüdür.")
        "Son Ayı" -> BookStats(288, "6.3 Saat", "2021", "4.9", "Hannah Gold, çocuk edebiyatı ve ekoloji masalları ile ödüller almış bir İngiliz yazardır.")
        "Düşler Atlası" -> BookStats(176, "3.8 Saat", "2019", "4.8", "Özgür Balpınar, gençlik edebiyatı ve hayal gücünü besleyen eserlerin yazarıdır.")
        "Lâ: Sonsuzluk Hecesi" -> BookStats(380, "9.7 Saat", "2008", "4.9", "Nazan Bekiroğlu, Trabzon doğumlu akademisyen, yazar ve edebiyat profesörüdür.")
        "Güvenli Bağlanma" -> BookStats(260, "6.2 Saat", "2015", "4.8", "Adem Güneş, pedagoji ve çocuk gelişimine yönelik çok satan eserlerin yazarıdır.")
        "Mimoza Sürgünü" -> BookStats(296, "7.5 Saat", "2013", "4.8", "Nazan Bekiroğlu, Trabzon doğumlu akademisyen, yazar ve edebiyat profesörüdür.")
        "Cezasız Eğitim 2" -> BookStats(224, "5.2 Saat", "2014", "4.8", "Adem Güneş, çocuk disiplini ve ebeveynlik üzerine pratik tavsiyeler sunan uzmandır.")
        else -> BookStats(150, "5.0 Saat", "2022", "4.6", "Bu eserin yazarı hakkında detaylı biyografi bilgisi Timaş kütüphane veritabanında güncellenmektedir.")
    }
}

private fun getBookPreface(bookTitle: String): String {
    return when (bookTitle) {
        "Mutluluğun İnşası" -> "Huzur, dışarıdaki gürültüde değil, içerideki dinginliktedir. Mutluluk bir hedef değil, her adımda hissettiğimiz bir inşa sürecidir..."
        "Dilin Afetleri" -> "Dil, kalbin tercümanı, ruhun aynasıdır. İnsanı felakete sürükleyen de kurtuluşa erdiren de iki dudağı arasındaki o küçük organdır..."
        "Kur'an Atlası" -> "Vahiylerin indiği dağlar, peygamberlerin yürüdüğü çöller ve helak olan kavimlerin kalıntıları... Bu eser, kutsal coğrafyanın izlerini sürüyor..."
        "Kalpsizler" -> "Harikalar Diyarı'nın en korkulan kraliçesi olmadan önce, Catherine sadece aşık olmak ve pastalar pişirmek isteyen bir genç kızdı..."
        "Tavuk Bacaklı Ev Kaçıyor" -> "Marinka kendini bildi bileli tavuk bacakları olan bir evde yaşadı. Evleri sürekli yer değiştirir, onlara hayal edilemez dünyalar sunardı..."
        "Ağaçların Fısıltısı" -> "Çorak topraklardan yemyeşil masal diyarına uzanan bir doğa yolculuğu… Bir çocuğun azmi dünyayı nasıl yeşile boyar?"
        "Öz Saygı Dersleri" -> "Ruh sağlığının temeli öz saygıdır. Kendini sevmeyi başaramayan, dünyayı ve insanları sevmeyi de öğrenemez. Bu dersler seni değiştirecek..."
        "Nar Ağacı" -> "Trabzon'dan Tebriz'e, oradan Tiflis ve İstanbul'a uzanan tarihi bir aşk hikayesi. Savaşın gölgesinde yeşeren iki hayatın önsözü..."
        "Dirilt Kalbini" -> "Modern dünyanın karmaşası kalbimizi uyuştururken, ilahi mesajlarla yeniden canlanmak, ruhumuzu diriltmek elimizdedir..."
        "İçimdeki Müzik" -> "Kelime ve konuşma yeteneği olmayan Melody'nin iç dünyasındaki harika melodilere, hayallerine ve kendisini dünyaya kanıtlama çabasına dair sarsıcı bir başlangıç..."
        "Bırak ve Rahatla" -> "Hayatın temposunda kaybolurken kendimize sormayı unuttuğumuz o soru: Gerçekten rahat mıyız? Ruhumuzu serbest bırakma vakti geldi..."
        "Mücella" -> "Zamanın ve tarihin akışında bir genç kızın sessiz ve vakur hikayesi. Çeyiz sandığında biriken umutlar ve yarım kalan hayaller..."
        "Kiraz Ağacı ile Aramızdaki Mesafe" -> "Kiraz ağacının dalları arasından dünyayı seyreden küçük Mafalda'nın, kararan dünyasında renkleri ve sevgiyi arama serüveni..."
        "Göğü Yere İndirelim" -> "Yıldızlara dokunmak isteyen çocukların, uçurtmaların kanadında taşıdıkları saf ve temiz hayallerin önsözür..."
        "Huzur Sokağı" -> "İstanbul'un eski sokaklarında, inanç ve sevginin sınandığı bir dünyada başlayan, nesiller boyu okunan unutulmaz bir klasik..."
        "Yusuf ile Züleyha" -> "Aşkın kuyusuna düşenlerin, güzelliğin ve tutkunun sırrını arayanların şiirsel bir dille yeniden yazılan kadim öyküsü..."
        "Malamander" -> "Sisli kasabada gizemli bir canavar ve kayıp bir ailenin izleri... Macera ve sırlar dolu bu hikayeye hoş geldiniz..."
        "Son Ayı" -> "Buzların eridiği bir dünyada, yalnız bir kutup ayısı ile küçük bir kızın dostluğunun dünyayı nasıl değiştirebileceğinin kanıtı..."
        "Düşler Atlası" -> "Haritaların ötesinde, çocukların hayal gücüyle çizilen sınır tanımayan düşlerin ve umudun kılavuzu..."
        "Lâ: Sonsuzluk Hecesi" -> "Cennetten yeryüzüne düşerken yanımıza aldığımız o tek hece. İnsanın varoluş ve gurbet hikayesinin derin anlatısı..."
        "Güvenli Bağlanma" -> "Anne ve çocuk arasındaki o ilk bağ, hayat boyu kuracağımız tüm köprülerin temelidir. Bağlanmanın gizemli dünyası..."
        "Mimoza Sürgünü" -> "Sürgünlerin, kelimelerin ve konakların arasında gezinirken, tarihin ve edebiyatın iz bırakan derin sularına bir yolculuk..."
        "Cezasız Eğitim 2" -> "Korku ve ceza ile eğitilen ruhlar solar. Sevgi ve farkındalıkla, çocuğun özgürlüğünü kısıtlamadan disiplin kurmanın yolları..."
        else -> "Bu eser, Timaş Yayıncılık'ın editörleri tarafından özenle incelenmiş ve okurlarımızın beğenisine sunulmuş seçkin bir çalışmadır..."
    }
}

private fun getBookReviews(bookTitle: String): List<BookReview> {
    return listOf(
        BookReview("Yasir K.", "⭐️⭐️⭐️⭐️⭐️", "Harika bir eser, hayata ve kendime bakış açımı değiştirdi. Koyu tema tasarımıyla okumak çok keyifli."),
        BookReview("Elif T.", "⭐️⭐️⭐️⭐️⭐️", "Seslendirmesi inanılmaz başarılı olmuş, kütüphanemin baş köşesinde yer alacak bir başyapıt."),
        BookReview("Mustafa A.", "⭐️⭐️⭐️⭐️☆", "İçerik olarak son derece akıcı ve bilgilendirici. Herkese okumasını tavsiye ederim.")
    )
}

@Composable
private fun DetailTileItem(
    label: String,
    value: String,
    icon: ImageVector,
    textColor: Color,
    subtitleColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFFF9F43), modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, fontSize = 9.sp, color = Color(0xFF64748B))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 11.sp, color = textColor, fontWeight = FontWeight.Bold)
    }
}
