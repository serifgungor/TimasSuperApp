package com.timas.superapp

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.delay

// ---------------- DATA MODEL ----------------

data class Book(
    val title: String,
    val author: String,
    val coverUrl: String,
    val description: String = "",
    val progress: Int = -1, // -1: Not started, 0-100: Progress percentage
    val type: String = "E-Kitap & Sesli",
    val category: String = "",
    val color: Color = Color(0xFFF26122),
    val isOwned: Boolean = true, // Whether the user has unlocked/purchased this volume
    val volumeNumber: Int = 1 // Volume position in the series
)

// ---------------- ROOT SCREEN ----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Library3DScreen() {
    val context = LocalContext.current

    // Mutable state list containing the entire database of 23 verified books
    val libraryBooks = remember {
        mutableStateListOf(
            // malamander series
            Book(
                title = "Malamander",
                author = "Thomas Taylor",
                coverUrl = "https://cdn.timas.com.tr/urun/malamander-9786050831634.jpg",
                description = "Eerie-on-Sea kasabasında, gizemli canavar Malamander'ın izini süren iki cesur çocuğun maceraları.",
                progress = 45,
                category = "Thomas Taylor Serisi",
                color = Color(0xFF1B4F72),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Gargantis",
                author = "Thomas Taylor",
                coverUrl = "https://cdn.timas.com.tr/urun/gargantis-9786050834116.jpg",
                description = "Malamander'ın devam macerası! Fırtınalar koptuğunda kasaba sular altında kalma tehlikesiyle karşı karşıyadır.",
                progress = -1,
                category = "Thomas Taylor Serisi",
                color = Color(0xFF0E6251),
                isOwned = true,
                volumeNumber = 2
            ),
            Book(
                title = "Shadowghast",
                author = "Thomas Taylor",
                coverUrl = "https://cdn.timas.com.tr/urun/shadowghast-9786050843231.jpg",
                description = "Karanlık çöktüğünde gölgeler canlanıyor. Serinin 3. kitabında macera doruk noktasına ulaşıyor.",
                progress = -1,
                category = "Thomas Taylor Serisi",
                color = Color(0xFF512E5F),
                isOwned = true,
                volumeNumber = 3
            ),
            Book(
                title = "Festergrimm",
                author = "Thomas Taylor",
                coverUrl = "https://cdn.timas.com.tr/urun/festergrimm-9786050846669.jpg",
                description = "Thomas Taylor'ın ödüllü serisinin 4. cildinde sırlar açığa çıkıyor.",
                progress = -1,
                category = "Thomas Taylor Serisi",
                color = Color(0xFF641E16),
                isOwned = false,
                volumeNumber = 4
            ),
            Book(
                title = "Mermedusa",
                author = "Thomas Taylor",
                coverUrl = "https://cdn.timas.com.tr/urun/mermedusa-9786050848984.jpg",
                description = "Serinin heyecan verici final kitabı! Eerie-on-Sea'nin kaderi bu romanda belirleniyor.",
                progress = -1,
                category = "Thomas Taylor Serisi",
                color = Color(0xFF154360),
                isOwned = false,
                volumeNumber = 5
            ),
            // Adem Güneş series
            Book(
                title = "Güvenli Bağlanma",
                author = "Adem Güneş",
                coverUrl = "https://cdn.timas.com.tr/urun/guvenli-baglanma-9786050820423.jpg",
                description = "Anne ve bebek arasındaki o ilk bağın, çocuğun tüm yaşamını ve psikolojisini şekillendiren gücü.",
                progress = 70,
                category = "Adem Güneş Serisi",
                color = Color(0xFF2E4053),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Cezasız Eğitim 2",
                author = "Adem Güneş",
                coverUrl = "https://cdn.timas.com.tr/urun/cezasiz-egitim-9786050814675.jpg",
                description = "Ceza ve baskı olmadan, sevgi ve bilinçle çocuk eğitimi vermenin yolları.",
                progress = -1,
                category = "Adem Güneş Serisi",
                color = Color(0xFF154360),
                isOwned = true,
                volumeNumber = 2
            ),
            Book(
                title = "Bırak ve Rahatla",
                author = "Adem Güneş",
                coverUrl = "https://cdn.timas.com.tr/urun/birak-ve-rahatla-9786050817027.jpg",
                description = "Hayatın temposunda kaybolurken kendimize sormayı unuttuğumuz o soru: Gerçekten rahat mıyız? Ruhumuzu serbest bırakma vakti.",
                progress = -1,
                category = "Adem Güneş Serisi",
                color = Color(0xFF16A085),
                isOwned = true,
                volumeNumber = 3
            ),
            Book(
                title = "Çocukluk Sırrı",
                author = "Adem Güneş",
                coverUrl = "https://cdn.timas.com.tr/urun/cocukluk-sirri-9786050819762.jpg",
                description = "Adem Güneş'ten çocukluk yıllarının bilinmeyen gizemli dünyasına dair rehber niteliğinde bir çalışma.",
                progress = -1,
                category = "Adem Güneş Serisi",
                color = Color(0xFF7D6608),
                isOwned = false,
                volumeNumber = 4
            ),
            Book(
                title = "Çocuk Eğitiminde Yanlışlar",
                author = "Adem Güneş",
                coverUrl = "https://cdn.timas.com.tr/urun/cocuk-egitiminde-dogru-bilinen-yanlislar-9786050811122.jpg",
                description = "Anne babaların çocuk yetiştirirken doğru sandığı temel hatalar ve pedagojik çözümleri.",
                progress = -1,
                category = "Adem Güneş Serisi",
                color = Color(0xFF78281F),
                isOwned = false,
                volumeNumber = 5
            ),
            // Nazan Bekiroğlu series
            Book(
                title = "Nar Ağacı",
                author = "Nazan Bekiroğlu",
                coverUrl = "https://cdn.timas.com.tr/urun/nar-agaci-9786050807073.jpg",
                description = "Trabzon-Tebriz-Tiflis-Batum-İstanbul hattında geçen, I. Dünya Savaşı'nın gölgesinde büyüleyici bir aşk ve tarih romanı.",
                progress = 100,
                category = "Nazan Bekiroğlu Serisi",
                color = Color(0xFF9B59B6),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Mücella",
                author = "Nazan Bekiroğlu",
                coverUrl = "https://cdn.timas.com.tr/urun/mucella-9786050819779.jpg",
                description = "Nazan Bekiroğlu'nun kaleminden, Osmanlı'nın son döneminden Cumhuriyet'e uzanan sessiz bir hayat hikayesi.",
                progress = -1,
                category = "Nazan Bekiroğlu Serisi",
                color = Color(0xFF7D6608),
                isOwned = true,
                volumeNumber = 2
            ),
            Book(
                title = "Yusuf ile Züleyha",
                author = "Nazan Bekiroğlu",
                coverUrl = "https://cdn.timas.com.tr/urun/yusuf-ile-zuleyha-9786050828351.jpg",
                description = "Aşkın ve güzelliğin kadim hikayesi, Nazan Bekiroğlu'nun şiirsel ve büyüleyici üslubuyla yeniden hayat buluyor.",
                progress = -1,
                category = "Nazan Bekiroğlu Serisi",
                color = Color(0xFF78281F),
                isOwned = true,
                volumeNumber = 3
            ),
            Book(
                title = "Mimoza Sürgünü",
                author = "Nazan Bekiroğlu",
                coverUrl = "https://cdn.timas.com.tr/urun/mimoza-surgunu-9786050812732.jpg",
                description = "Edebiyat ve tarihin derinliklerinde, kelimelerin izini süren sürgünlerin ve konakların öyküsü.",
                progress = -1,
                category = "Nazan Bekiroğlu Serisi",
                color = Color(0xFF641E16),
                isOwned = true,
                volumeNumber = 4
            ),
            Book(
                title = "Lâ: Sonsuzluk Hecesi",
                author = "Nazan Bekiroğlu",
                coverUrl = "https://cdn.timas.com.tr/urun/la-sonsuzluk-hecesi-9786050828368.jpg",
                description = "Nazan Bekiroğlu'nun kaleminden varoluşun ve yaratılışın ilk hecesi üzerine lirik bir roman.",
                progress = -1,
                category = "Nazan Bekiroğlu Serisi",
                color = Color(0xFF512E5F),
                isOwned = false,
                volumeNumber = 5
            ),
            Book(
                title = "Kehribar Geçidi",
                author = "Nazan Bekiroğlu",
                coverUrl = "https://cdn.timas.com.tr/urun/kehribar-gecidi-9786050843002.jpg",
                description = "Nazan Bekiroğlu'ndan Roma İmparatorluğu'nun geçiş döneminde geçen tarihsel ve felsefi bir başyapıt.",
                progress = -1,
                category = "Nazan Bekiroğlu Serisi",
                color = Color(0xFF154360),
                isOwned = false,
                volumeNumber = 6
            ),
            // Standalone books (owned by default)
            Book(
                title = "Mutluluğun İnşası",
                author = "Mecit Ömür Öztürk",
                coverUrl = "https://cdn.timas.com.tr/urun/mutlulugun-insasi-9786050849745.jpg",
                description = "Mutluluk bir durum değil, bir yolculuktur. Bu kitapta, iç huzuru bulmanın ve gerçek mutluluğu inşa etmenin adımlarını keşfedeceksiniz.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFFE67E22),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Dilin Afetleri",
                author = "İmam Gazzâlî",
                coverUrl = "https://cdn.timas.com.tr/urun/dilin-afetleri-9786259445182.jpg",
                description = "İnsanın en büyük imtihanlarından biri olan dilin, ahlak ve maneviyat üzerindeki etkilerini anlatan klasik bir eser.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF27AE60),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Kur'an Atlası",
                author = "Timaş Yayınları",
                coverUrl = "https://cdn.timas.com.tr/urun/kuran-atlasi-9786256360525.jpg",
                description = "Kur'an-ı Kerim'de adı geçen coğrafi mekanların, kavimlerin ve olayların tarihi ve arkeolojik bilgilerle açıklandığı kapsamlı bir rehber.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF2980B9),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Kalpsizler",
                author = "Marissa Meyer",
                coverUrl = "https://cdn.timas.com.tr/urun/kalpsizler-9786050847642.jpg",
                description = "Harikalar Diyarı'nın en korkulan kraliçesi olmadan önce, Catherine sadece aşık olmak ve pastalar pişirmek isteyen bir genç kızdı...",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF8E44AD),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Tavuk Bacaklı Ev Kaçıyor",
                author = "Sophie Anderson",
                coverUrl = "https://cdn.timas.com.tr/urun/tavuk-bacakli-ev-kaciyor-9786259232645.jpg",
                description = "Marinka kendini bildi bileli tavuk bacakları olan bir evde yaşadı. Evleri sürekli yer değiştirir, onlara hayal edilemez dünyalar sunardı...",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFFD35400),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Ağaçların Fısıltısı",
                author = "Murat Moroğlu",
                coverUrl = "https://cdn.timas.com.tr/urun/agaclarin-fisiltisi-9786258618112.jpg",
                description = "Çorak topraklardan yemyeşil masal diyarına uzanan bir doğa yolculuğu… Bir çocuğun azmi dünyayı nasıl yeşile boyar?",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF16A085),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Öz Saygı Dersleri",
                author = "Yoon Hong Gyun",
                coverUrl = "https://cdn.timas.com.tr/urun/oz-saygi-dersleri-9786050849851.jpg",
                description = "Koreli psikiyatrist Yoon Hong Gyun'un öz saygıyı kazanma ve hayata daha güvenli bakma rehberi.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF7F8C8D),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Dirilt Kalbini",
                author = "Nouman Ali Khan",
                coverUrl = "https://cdn.timas.com.tr/urun/dirilt-kalbini-9786050825992.jpg",
                description = "Modern çağın karmaşasında kalbini Kur'an ile diriltmek isteyenler için ufuk açıcı bir eser.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF34495E),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "İçimdeki Müzik",
                author = "Sharon M. Draper",
                coverUrl = "https://cdn.timas.com.tr/urun/icimdeki-muzik-9786050821277.jpg",
                description = "Melody'nin konuşma engeline rağmen iç dünyasındaki zenginlik ve müzik dolu mücadelesi.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF2980B9),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Kiraz Ağacı ile Aramızdaki Mesafe",
                author = "Paola Peretti",
                coverUrl = "https://cdn.timas.com.tr/urun/kiraz-agaci-ile-aramizdaki-mesafe-9786050828238.jpg",
                description = "Mafalda'nın yavaş yavaş görme yetisini kaybederken kiraz ağacıyla kurduğu dostluk ve hayata tutunma öyküsü.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF9C27B0),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Göğü Yere İndirelim",
                author = "Özgür Balpınar",
                coverUrl = "https://cdn.timas.com.tr/urun/gogu-yere-indirelim-9786050824049.jpg",
                description = "Özgür Balpınar'dan dostluk, doğa ve hayal kurmaktan vazgeçmeyen çocukların sıcacık hikayesi.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF3F51B5),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Düşler Atlası",
                author = "Özgür Balpınar",
                coverUrl = "https://cdn.timas.com.tr/urun/dusler-atlasi-9786050830026.jpg",
                description = "Özgür Balpınar'nın kaleminden hayal gücü, dostluk ve macera dolu bir keşif yolculuğu.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF7D6608),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Huzur Sokağı",
                author = "Şule Yüksel Şenler",
                coverUrl = "https://cdn.timas.com.tr/urun/huzur-sokagi-ciltli-9786050830491.jpg",
                description = "İstanbul'un eski sokaklarında başlayan, inanç ve sevginin sınandığı unutulmaz bir klasik aşk hikayesi.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF6E2C00),
                isOwned = true,
                volumeNumber = 1
            ),
            Book(
                title = "Son Ayı",
                author = "Hannah Gold",
                coverUrl = "https://cdn.timas.com.tr/urun/son-ayi-9786050844320.jpg",
                description = "Buzların eridiği bir dünyada, yalnız bir kutup ayısı ile küçük bir kızın dostluğunun sarsıcı ve sevgi dolu öyküsü.",
                progress = -1,
                category = "Bağımsız Kitaplar",
                color = Color(0xFF0E6251),
                isOwned = true,
                volumeNumber = 1
            )
        )
    }

    var searchQuery by remember { mutableStateOf("") }

    // Dialog state controllers
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var activeEBookReader by remember { mutableStateOf<Book?>(null) }
    var activeAudioPlayer by remember { mutableStateOf<Book?>(null) }
    var bookToPurchase by remember { mutableStateOf<Book?>(null) } // Purchase dialog state

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    // Filtered lists
    val filteredBooks = remember(searchQuery, libraryBooks) {
        libraryBooks.filter { book ->
            book.title.contains(searchQuery, ignoreCase = true) ||
                    book.author.contains(searchQuery, ignoreCase = true)
        }
    }

    // Series definitions
    val seriesList = remember(filteredBooks) {
        listOf(
            "Thomas Taylor Serisi" to filteredBooks.filter { it.category == "Thomas Taylor Serisi" }.sortedBy { it.volumeNumber },
            "Adem Güneş Serisi" to filteredBooks.filter { it.category == "Adem Güneş Serisi" }.sortedBy { it.volumeNumber },
            "Nazan Bekiroğlu Serisi" to filteredBooks.filter { it.category == "Nazan Bekiroğlu Serisi" }.sortedBy { it.volumeNumber }
        )
    }

    val standaloneBooks = remember(filteredBooks) {
        filteredBooks.filter { it.category == "Bağımsız Kitaplar" }
    }

    val totalBooksCount = remember(libraryBooks) { libraryBooks.size }
    val ownedBooksCount = remember(libraryBooks) { libraryBooks.count { it.isOwned } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF120C0A)) // Warm dark chocolate premium lounge theme
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // --- 1. CLEAN PERSISTENT LIBRARY HEADER ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF261D1A), Color(0xFF1C1311))
                        )
                    )
                    .padding(top = 24.dp, bottom = 28.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Kütüphanem",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "Seri kitap koleksiyonunu tamamla",
                                fontSize = 12.sp,
                                color = Color(0xFFFF9F43).copy(alpha = 0.85f),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Collection completion progress badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFF9F43).copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Koleksiyon: $ownedBooksCount/$totalBooksCount",
                                color = Color(0xFFFF9F43),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Reading statistics panel
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF120C0A).copy(alpha = 0.5f))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(label = "Sahip Olunan", value = "$ownedBooksCount Cilt", icon = Icons.Default.LibraryBooks)
                        StatItem(label = "Eksik Kitaplar", value = "${totalBooksCount - ownedBooksCount} Cilt", icon = Icons.Default.AddShoppingCart)
                        StatItem(label = "Toplam Kitap", value = "$totalBooksCount Cilt", icon = Icons.Default.MenuBook)
                    }
                }
            }

            // --- 2. SEARCH INTERFACE ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Seri, kitap veya yazar ara...", color = Color(0xFF64748B), fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF261D1A),
                        unfocusedContainerColor = Color(0xFF261D1A),
                        focusedBorderColor = Color(0xFFFF9F43),
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            // --- 3. BOOK SERIES SHELVES (Wooden-styled collection lines) ---
            seriesList.forEach { (seriesName, books) ->
                if (books.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = seriesName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = FontFamily.Serif
                            )

                            // Show completion percent of this series
                            val ownedInSeries = books.count { it.isOwned }
                            val totalInSeries = books.size
                            Text(
                                text = "Cilt: $ownedInSeries/$totalInSeries",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (ownedInSeries == totalInSeries) Color(0xFF27AE60) else Color(0xFFFF9F43)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Books Shelf Row Container
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(books) { book ->
                                    ShelfBookItem(
                                        book = book,
                                        onClick = {
                                            if (book.isOwned) {
                                                selectedBook = book
                                            } else {
                                                bookToPurchase = book
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        // Simulated horizontal wooden shelf line underneath the books row
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF8E613E), Color(0xFF6B4527))
                                    )
                                )
                                .shadow(4.dp)
                        )
                    }
                }
            }

            // --- 4. STANDALONE BOOKS (Bağımsız Kitaplar) ---
            if (standaloneBooks.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = "Bağımsız Kitaplar",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val gridCells = if (isTablet) GridCells.Fixed(3) else GridCells.Fixed(2)
                    LazyVerticalGrid(
                        columns = gridCells,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 1200.dp) // Bound vertical grid height
                    ) {
                        items(standaloneBooks) { book ->
                            StandaloneBookGridItem(
                                book = book,
                                onClick = { selectedBook = book }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }

        // --- 5. OVERLAYS, DIALOGS AND PURCHASE SYSTEM RESOLVERS ---

        // A. Book Details Dialog Overlay (For owned books)
        if (selectedBook != null) {
            Dialog(
                onDismissRequest = { selectedBook = null },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false,
                    decorFitsSystemWindows = true
                )
            ) {
                com.timas.superapp.screens.BookDetailScreen(
                    book = selectedBook!!,
                    onBack = { selectedBook = null },
                    onStartReading = { book ->
                        selectedBook = null
                        activeEBookReader = book
                    },
                    onStartListening = { book ->
                        selectedBook = null
                        activeAudioPlayer = book
                    }
                )
            }
        }

        // B. Interactive Purchase Dialog (For locked volumes)
        if (bookToPurchase != null) {
            val book = bookToPurchase!!
            Dialog(onDismissRequest = { bookToPurchase = null }) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF261D1A)),
                    modifier = Modifier
                        .width(320.dp)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Koleksiyonu Tamamla",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            IconButton(onClick = { bookToPurchase = null }) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White.copy(0.6f))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Book Cover in Purchase Dialog (Fully colored)
                        Box(
                            modifier = Modifier
                                .size(110.dp, 160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .shadow(8.dp)
                        ) {
                            SubcomposeAsyncImage(
                                model = book.coverUrl,
                                contentDescription = book.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                loading = { BookCoverFallback(book = book) },
                                error = { BookCoverFallback(book = book) }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = book.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${book.author} • Cilt ${book.volumeNumber}",
                            fontSize = 12.sp,
                            color = Color(0xFFFF9F43),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = book.description.ifEmpty { "Bu cildi satın alarak seriye ait tüm okuma ve dinleme ayrıcalıklarını hemen açın." },
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8),
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Price and purchase button
                        Button(
                            onClick = {
                                // Purchase volume: update the state list dynamically!
                                val targetIndex = libraryBooks.indexOfFirst { it.title == book.title }
                                if (targetIndex != -1) {
                                    libraryBooks[targetIndex] = libraryBooks[targetIndex].copy(isOwned = true)
                                }
                                Toast.makeText(context, "${book.title} satın alındı! Kütüphanenize eklendi.", Toast.LENGTH_LONG).show()
                                bookToPurchase = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9F43)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Kitaplığıma Ekle (79,90 TL)",
                                color = Color(0xFF120C0A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // C. Full-Screen E-Book Reader Simulator
        if (activeEBookReader != null) {
            Dialog(
                onDismissRequest = { activeEBookReader = null },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                EBookReaderSimulator(
                    book = activeEBookReader!!,
                    onClose = { activeEBookReader = null }
                )
            }
        }

        // D. Full-Screen Audiobook Player Simulator
        if (activeAudioPlayer != null) {
            Dialog(
                onDismissRequest = { activeAudioPlayer = null },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                AudiobookPlayerSimulator(
                    book = activeAudioPlayer!!,
                    onClose = { activeAudioPlayer = null }
                )
            }
        }
    }
}

// ---------------- SUB-COMPOSABLES ----------------

@Composable
private fun StatItem(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFFF9F43).copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFF9F43),
                modifier = Modifier.size(16.dp)
            )
        }
        Column {
            Text(text = label, fontSize = 9.sp, color = Color(0xFF64748B))
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
private fun ShelfBookItem(
    book: Book,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp, 130.dp)
                .clip(RoundedCornerShape(10.dp))
                .shadow(6.dp)
        ) {
            if (book.isOwned) {
                // Normal colored cover
                SubcomposeAsyncImage(
                    model = book.coverUrl,
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = { BookCoverFallback(book = book) },
                    error = { BookCoverFallback(book = book) }
                )

                // Format badges overlay (Ebook + Audio)
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Color(0xFF27AE60),
                        modifier = Modifier.size(10.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = Color(0xFFFF9F43),
                        modifier = Modifier.size(10.dp)
                    )
                }
            } else {
                // Locked desaturated desaturation look
                SubcomposeAsyncImage(
                    model = book.coverUrl,
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                    alpha = 0.5f,
                    loading = { BookCoverFallback(book = book) },
                    error = { BookCoverFallback(book = book) }
                )

                // Locked pad lock emblem overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier
                            .size(28.dp)
                            .shadow(4.dp, CircleShape)
                    )
                }
            }

            // Volume Number Tag Overlay (bottom-left)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (book.isOwned) Color(0xFFFF9F43) else Color(0xFF475569))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Cilt ${book.volumeNumber}",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (book.isOwned) Color(0xFF120C0A) else Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = book.title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (book.isOwned) Color.White else Color.White.copy(0.4f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        // Reading state label / progress bar
        if (book.isOwned) {
            if (book.progress >= 0) {
                val statusText = if (book.progress == 100) "Tamamlandı" else "%${book.progress} okundu"
                Text(
                    text = statusText,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFF9F43),
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Yeni",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF27AE60),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Text(
                text = "Kilitle",
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9F43),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.5.dp, Color(0xFFFF9F43).copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            )
        }
    }
}

@Composable
private fun StandaloneBookGridItem(
    book: Book,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF261D1A)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.68f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                SubcomposeAsyncImage(
                    model = book.coverUrl,
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = { BookCoverFallback(book = book) },
                    error = { BookCoverFallback(book = book) }
                )

                // Dual Format Badge
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Color(0xFF27AE60),
                        modifier = Modifier.size(11.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = Color(0xFFFF9F43),
                        modifier = Modifier.size(11.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = book.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.author,
                fontSize = 10.sp,
                color = Color(0xFF94A3B8),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (book.progress >= 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val statusText = if (book.progress == 100) "Tamamlandı" else "%${book.progress}"
                    Text(
                        text = statusText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF9F43)
                    )
                    LinearProgressIndicator(
                        progress = { book.progress / 100f },
                        color = Color(0xFFFF9F43),
                        trackColor = Color(0xFF120C0A),
                        modifier = Modifier
                            .width(50.dp)
                            .height(3.dp)
                            .clip(CircleShape)
                    )
                }
            } else {
                Text(
                    text = "Başlanmadı",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

@Composable
private fun BookCoverFallback(book: Book, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(book.color, book.color.copy(alpha = 0.6f))
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
                    modifier = Modifier.size(12.dp)
                )
                Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.title,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 11.sp
            )
        }
    }
}

// ---------------- READER & PLAYER SIMULATORS ----------------

private enum class SimTheme(
    val label: String,
    val bgColor: Color,
    val textColor: Color,
    val borderClr: Color
) {
    LIGHT("Açık", Color(0xFFFFFFFF), Color(0xFF0F172A), Color(0xFFE2E8F0)),
    SEPIA("Sepya", Color(0xFFFAF6EE), Color(0xFF4A3E31), Color(0xFFE6DEC9)),
    DARK("Karanlık", Color(0xFF1E293B), Color(0xFFF1F5F9), Color(0xFF334155))
}

@Composable
private fun EBookReaderSimulator(
    book: Book,
    onClose: () -> Unit
) {
    val pages = remember(book.title) {
        listOf(
            "KAPAK SAYFASI - ${book.title}\n\nYazar: ${book.author}\n\nTimaş Yayıncılık E-Kitap Kitaplığı",
            "Giriş\n\nBu eser, e-kitap kütüphanemizin en seçkin parçalarından biridir. Keyifli okumalar dileriz.",
            "Bölüm 1: Sayfa 1\n\n${book.description.ifEmpty { "Bu kitap için ek detaylı içerik yükleniyor..." }}\n\nOkuma salonumuzda, odaklanma sayacı ve dinlendirici müzikler eşliğinde okumanızı sürdürebilirsiniz.",
            "Bölüm 2: Sayfa 2\n\nEdebiyatın zenginliklerini hissettiğiniz her an, ruhunuzun gelişimine katkıda bulunur. Timaş yayınlarıyla aklınızı ve ruhunuzu beslemeye devam edin.",
            "Bölüm 3: Son Söz\n\nKitabın sonuna ulaştınız. Dilerseniz bu kitaba Sesli Dinle seçeneğiyle de başlayabilir, kaldığınız yerden hikayeyi dinleyebilirsiniz."
        )
    }

    var pageIdx by remember { mutableStateOf(0) }
    var currentTheme by remember { mutableStateOf(SimTheme.SEPIA) }
    var fontSize by remember { mutableStateOf(16.sp) }
    var isSerifFont by remember { mutableStateOf(true) }

    val fontFamily = if (isSerifFont) FontFamily.Serif else FontFamily.Default

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    Text(
                        text = book.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = currentTheme.textColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = currentTheme.textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = currentTheme.bgColor)
            )
        },
        containerColor = currentTheme.bgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            // Control panel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, currentTheme.borderClr, RoundedCornerShape(12.dp))
                    .background(currentTheme.bgColor)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Font Toggle
                TextButton(onClick = { isSerifFont = !isSerifFont }) {
                    Text(
                        text = if (isSerifFont) "Serif" else "Sans",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.textColor
                    )
                }

                // Font Size Adjust
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (fontSize.value > 12f) fontSize = (fontSize.value - 2).sp },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Text("A-", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = currentTheme.textColor)
                    }
                    IconButton(
                        onClick = { if (fontSize.value < 28f) fontSize = (fontSize.value + 2).sp },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Text("A+", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = currentTheme.textColor)
                    }
                }

                // Themes
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SimTheme.values().forEach { th ->
                        val isSel = currentTheme == th
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(th.bgColor)
                                .border(
                                    width = if (isSel) 2.dp else 1.dp,
                                    color = if (isSel) Color(0xFFFF9F43) else currentTheme.borderClr,
                                    shape = CircleShape
                                )
                                .clickable { currentTheme = th }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Readable page content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = if (pageIdx == 0) Alignment.Center else Alignment.TopStart
            ) {
                if (pageIdx == 0) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(110.dp, 160.dp)
                                .shadow(8.dp, RoundedCornerShape(12.dp))
                        ) {
                            SubcomposeAsyncImage(
                                model = book.coverUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                loading = { BookCoverFallback(book = book) },
                                error = { BookCoverFallback(book = book) }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = book.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamily,
                            color = currentTheme.textColor,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = book.author,
                            fontSize = 14.sp,
                            color = currentTheme.textColor.copy(alpha = 0.7f),
                            fontFamily = fontFamily,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Text(
                        text = pages.getOrElse(pageIdx) { "" },
                        fontSize = fontSize,
                        fontFamily = fontFamily,
                        color = currentTheme.textColor,
                        lineHeight = (fontSize.value * 1.5f).sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation page buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { if (pageIdx > 0) pageIdx-- },
                    enabled = pageIdx > 0,
                    colors = ButtonDefaults.textButtonColors(contentColor = currentTheme.textColor)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
                        Text("Önceki", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "${pageIdx + 1} / ${pages.size}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textColor.copy(alpha = 0.6f)
                )

                TextButton(
                    onClick = { if (pageIdx < pages.size - 1) pageIdx++ },
                    enabled = pageIdx < pages.size - 1,
                    colors = ButtonDefaults.textButtonColors(contentColor = currentTheme.textColor)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Sonraki", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
private fun AudiobookPlayerSimulator(
    book: Book,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val totalSeconds = 320 // Simulation length
    var currentSeconds by remember { mutableStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }
    var speed by remember { mutableStateOf(1.0f) }

    LaunchedEffect(isPlaying, speed) {
        if (isPlaying) {
            while (isPlaying && currentSeconds < totalSeconds) {
                delay(1000)
                currentSeconds = minOf(totalSeconds.toFloat(), currentSeconds + speed)
            }
            if (currentSeconds >= totalSeconds) {
                isPlaying = false
            }
        }
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Sesli Kitap Oynatıcı", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF261D1A))
            )
        },
        containerColor = Color(0xFF120C0A)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Large Book Cover
            Box(
                modifier = Modifier
                    .size(160.dp, 240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(12.dp)
            ) {
                SubcomposeAsyncImage(
                    model = book.coverUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = { BookCoverFallback(book = book) },
                    error = { BookCoverFallback(book = book) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = book.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = book.author,
                fontSize = 14.sp,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Seslendiren: Timaş Seslendirme Ekibi",
                fontSize = 12.sp,
                color = Color(0xFFFF9F43),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Time slider progress bar
            Slider(
                value = currentSeconds,
                onValueChange = { currentSeconds = it },
                valueRange = 0f..totalSeconds.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFF9F43),
                    activeTrackColor = Color(0xFFFF9F43),
                    inactiveTrackColor = Color(0xFF261D1A)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val minsCurrent = currentSeconds.toInt() / 60
                val secsCurrent = currentSeconds.toInt() % 60
                Text(
                    text = String.format("%02d:%02d", minsCurrent, secsCurrent),
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )

                val minsTotal = totalSeconds / 60
                val secsTotal = totalSeconds % 60
                Text(
                    text = String.format("%02d:%02d", minsTotal, secsTotal),
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Playback controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Adjust Speed
                IconButton(
                    onClick = {
                        speed = when (speed) {
                            1.0f -> 1.25f
                            1.25f -> 1.5f
                            1.5f -> 2.0f
                            else -> 1.0f
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFF261D1A), CircleShape)
                ) {
                    Text(
                        text = "${speed}x",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF9F43)
                    )
                }

                // Go back 15s
                IconButton(
                    onClick = { currentSeconds = maxOf(0f, currentSeconds - 15f) },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFF261D1A), CircleShape)
                ) {
                    Icon(Icons.Default.Replay10, contentDescription = null, tint = Color.White)
                }

                // Play / Pause
                IconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFFFF9F43), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color(0xFF120C0A),
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Skip forward 15s
                IconButton(
                    onClick = { currentSeconds = minOf(totalSeconds.toFloat(), currentSeconds + 15f) },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFF261D1A), CircleShape)
                ) {
                    Icon(Icons.Default.Forward10, contentDescription = null, tint = Color.White)
                }

                // Volume Indicator
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Ses seviyesi optimize edildi.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFF261D1A), CircleShape)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}
