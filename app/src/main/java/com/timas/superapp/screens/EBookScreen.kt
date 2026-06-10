package com.timas.superapp.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// ─── Renk Paleti ─────────────────────────────────────────────
private val Orange       = Color(0xFFF26122)
private val LightOrange  = Color(0xFFFFF0EB)
private val DarkSlate    = Color(0xFF1E293B)
private val SlateLight   = Color(0xFF334155)
private val BgColor      = Color(0xFFF8FAFC)
private val CardBg       = Color(0xFFFFFFFF)
private val TextMain     = Color(0xFF0F172A)
private val TextMuted    = Color(0xFF64748B)
private val BorderClr    = Color(0xFFE2E8F0)

// ─── Okuyucu Temaları ─────────────────────────────────────────
enum class ReaderTheme(
    val label: String,
    val bgColor: Color,
    val textColor: Color,
    val borderClr: Color
) {
    LIGHT("Açık", Color(0xFFFFFFFF), Color(0xFF0F172A), Color(0xFFE2E8F0)),
    SEPIA("Sepya", Color(0xFFFAF6EE), Color(0xFF4A3E31), Color(0xFFE6DEC9)),
    DARK("Karanlık", Color(0xFF1E293B), Color(0xFFF1F5F9), Color(0xFF334155))
}

// ─── Veri Sınıfları ───────────────────────────────────────────
data class EBook(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String,
    val progress: Int, // okuma yüzdesi (örn: 25)
    val pages: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EBookScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // E-Kitap Listeleri (Değişikliklerin ekranda anlık güncellenmesi için mutableStateListOf kullanıyoruz)
    val purchasedBooks = remember {
        mutableStateListOf(
            EBook(
                id = "eb1",
                title = "Mutluluğun İnşası",
                author = "Mecit Ömür Öztürk",
                coverUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?auto=format&fit=crop&q=80&w=300",
                progress = 25, // Sayfa 2 / 5 -> %25 (İçerik 1. Sayfa)
                pages = listOf(
                    "Kapak", // Görsel Kapak Sayfası (Sayfa 1 / %0)
                    "Giriş: Mutluluk bir durum değil, bir yolculuktur. Bu kitapta, iç huzuru bulmanın ve gerçek mutluluğu inşa etmenin adımlarını keşfedeceksiniz. Her bölüm, pratik egzersizler ve düşünce provokasyonları ile desteklenmiştir.",
                    "Bölüm 1: Hayatı Anlamlandırmak. Çoğu insan mutluluğu dışsal faktörlerde arar: yeni bir araba, daha büyük bir ev veya daha yüksek bir maaş. Ancak asıl huzur, zihnimizin içindeki sessiz köşelerde gizlidir.",
                    "Bölüm 2: Kendini Kabul. Kendimizi olduğumuz gibi kabul etmek, büyümenin ilk şartıdır. Kusurlarımız bizi zayıf değil, eşsiz kılar. Hatalarımızdan öğrenip yola devam etmeliyiz.",
                    "Bölüm 3: Anı Yaşamak. Geçmişin pişmanlıkları ve geleceğin kaygıları, şimdiki anın güzelliğini gölgeler. Sadece 'şimdi' gerçektir ve mutluluk ancak şimdiki zamanda inşa edilebilir."
                )
            ),
            EBook(
                id = "eb2",
                title = "Dilin Afetleri",
                author = "İmam Gazali",
                coverUrl = "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&q=80&w=300",
                progress = 0, // Sayfa 1 / 5 -> %0 (Kapak Sayfası)
                pages = listOf(
                    "Kapak", // Görsel Kapak Sayfası (Sayfa 1 / %0)
                    "Önsöz: İnsanın en büyük imtihanlarından biri olan dilin, ahlak ve maneviyat üzerindeki etkilerini anlatan klasik bir eser. Dil, kalbin aynasıdır der bilgeler.",
                    "Gıybet ve Yalan: Gıybet etmek, ölü kardeşinin etini yemeye benzetilmiştir kadim metinlerde. Yalan ise, güveni ve toplumsal bağları kökünden sarsan en tehlikeli afettir.",
                    "Sükutun Erdemi: İki kere düşünüp bir kere konuşmak, ya da susmak... Konuşmanın gümüş, sükutun ise altın olduğu unutulmamalıdır.",
                    "Gözetmek ve Sakınmak: Söz söylemeden önce onun doğuracağı sonuçları tartmak olgun bir karakterin en temel göstergesidir."
                )
            ),
            EBook(
                id = "eb3",
                title = "Kur'an Atlası",
                author = "Timaş Yayınları",
                coverUrl = "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?auto=format&fit=crop&q=80&w=300",
                progress = 75, // Sayfa 4 / 5 -> %75 (İçerik 3. Sayfa)
                pages = listOf(
                    "Kapak", // Görsel Kapak Sayfası (Sayfa 1 / %0)
                    "Giriş: Kur'an-ı Kerim'de adı geçen coğrafi mekanların, kavimlerin ve olayların tarihi ve arkeolojik bilgilerle açıklandığı kapsamlı bir rehber.",
                    "Kavimler Coğrafyası: Ad, Semud ve Nuh kavimlerinin yaşadığı bölgeler, arkeolojik kazılar ve tarihsel belgeler ışığında incelenmektedir.",
                    "Kutsal Vadiler: Sina Dağı, Mekke ve Kudüs gibi kutsal mekanların tarihi önemleri ve Kur'an'daki tasvirleri.",
                    "Tarihsel Yolculuklar: Peygamberlerin tebliğ faaliyetlerini sürdürdükleri güzergahlar ve bu güzergahların antik ticaret yolları ile ilişkisi."
                )
            )
        )
    }

    val sampleBooks = remember {
        mutableStateListOf(
            EBook(
                id = "es1",
                title = "Kalpsizler",
                author = "Timaş Yayınları",
                coverUrl = "https://images.unsplash.com/photo-1532012197267-da84d127e765?auto=format&fit=crop&q=80&w=300",
                progress = -1, // Henüz okunmadı -> %-1
                pages = listOf(
                    "Kapak", // Görsel Kapak Sayfası (Sayfa 1 / %0)
                    "Tadımlık Bölüm 1: Kalpsizlerin dünyasında insanlık sorgulanıyor. Distopik bir gelecekte, duygulardan arındırılmış bir toplumda var olma mücadelesi veren gençlerin hikayesi.",
                    "Tadımlık Bölüm 2: Sistemin kuralları nettir: Duygu belirtisi gösterenler izole edilir. Ancak bazı şeyler kurallardan daha güçlüdür."
                )
            ),
            EBook(
                id = "es2",
                title = "Politik Bir Beden",
                author = "Timaş Yayınları",
                coverUrl = "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?auto=format&fit=crop&q=80&w=300",
                progress = -1, // Henüz okunmadı -> %-1
                pages = listOf(
                    "Kapak", // Görsel Kapak Sayfası (Sayfa 1 / %0)
                    "Tadımlık Bölüm 1: Toplumsal cinsiyet ve siyaset üzerine modern bir analiz. Bedenin politikleşmesi, iktidar ilişkileri ve bireysel özgürlüklerin sınırları tartışılıyor.",
                    "Tadımlık Bölüm 2: Toplumun normları bedeni nasıl şekillendirir ve birey bu normlar karşısında kendi özgür alanını nasıl korur?"
                )
            ),
            EBook(
                id = "es3",
                title = "Od",
                author = "İskender Pala",
                coverUrl = "https://images.unsplash.com/photo-1474932430478-367dbb6832c1?auto=format&fit=crop&q=80&w=300",
                progress = -1, // Henüz okunmadı -> %-1
                pages = listOf(
                    "Kapak", // Görsel Kapak Sayfası (Sayfa 1 / %0)
                    "Tadımlık Bölüm 1: Yunus Emre'nin hayatından kesitler sunan, tasavvufi derinliği ve şiirsel anlatımıyla dinleyiciyi büyüleyen tarihi bir roman.",
                    "Tadımlık Bölüm 2: Aşkın ateşinde yanmak, benlikten geçip bütüne kavuşmak. Yunus'un Taptuk Emre dergahındaki çileli ama kutlu kapısı."
                )
            )
        )
    }

    // Durum Yönetimi
    var selectedBook by remember { mutableStateOf(purchasedBooks[0]) }
    
    val initialPage = remember(selectedBook.id) {
        val total = selectedBook.pages.size
        if (total > 1) {
            if (selectedBook.progress >= 0) {
                kotlin.math.round((selectedBook.progress.toFloat() / 100f) * (total - 1)).toInt().coerceIn(0, total - 1)
            } else {
                0
            }
        } else {
            0
        }
    }
    var currentPageIndex by remember(selectedBook.id) { mutableStateOf(initialPage) }

    val selectBookAndSyncProgress: (EBook) -> Unit = { book ->
        val total = book.pages.size
        val initialPageIdx = if (total > 1) {
            if (book.progress >= 0) {
                kotlin.math.round((book.progress.toFloat() / 100f) * (total - 1)).toInt().coerceIn(0, total - 1)
            } else {
                0
            }
        } else {
            0
        }
        val exactProgress = if (total > 1) {
            kotlin.math.round((initialPageIdx.toFloat() / (total - 1)) * 100).toInt()
        } else {
            100
        }
        
        val idx = purchasedBooks.indexOfFirst { it.id == book.id }
        if (idx != -1) {
            purchasedBooks[idx] = purchasedBooks[idx].copy(progress = exactProgress)
            selectedBook = purchasedBooks[idx]
        } else {
            val sIdx = sampleBooks.indexOfFirst { it.id == book.id }
            if (sIdx != -1) {
                sampleBooks[sIdx] = sampleBooks[sIdx].copy(progress = exactProgress)
                selectedBook = sampleBooks[sIdx]
            }
        }
    }
    
    // Okuyucu Kişiselleştirme Ayarları
    var readerTheme by remember { mutableStateOf(ReaderTheme.SEPIA) }
    var readerFontSize by remember { mutableStateOf(16.sp) }
    var isSerifFont by remember { mutableStateOf(true) }

    // Telefonlarda okuyucuyu tam ekran gösterme flag'i
    var showMobileReaderScreen by remember { mutableStateOf(false) }

    // Ekran Boyutu Tespiti
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "E-Book",
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (showMobileReaderScreen && !isTablet) {
                            showMobileReaderScreen = false
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Geri",
                            tint = DarkSlate,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Bildirimleriniz güncel.", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Bildirimler",
                            tint = DarkSlate
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BgColor
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgColor)
                .padding(innerPadding)
        ) {
            if (isTablet) {
                // ─── Tablet / Geniş Ekran Düzeni (Split-Pane) ─────────────────
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Sol Taraf: Kitap Listesi (Weight: 4)
                    Column(
                        modifier = Modifier
                            .weight(4f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        EBookListSections(
                            purchasedBooks = purchasedBooks,
                            sampleBooks = sampleBooks,
                            activeBookId = selectedBook.id,
                            onSelectBook = { book ->
                                selectBookAndSyncProgress(book)
                                Toast.makeText(context, "${book.title} açıldı.", Toast.LENGTH_SHORT).show()
                            },
                            onBuyClick = {
                                Toast.makeText(context, "E-Book satın alma sayfasına yönlendiriliyorsunuz...", Toast.LENGTH_LONG).show()
                            }
                        )
                    }

                    // Sağ Taraf: Detaylı Etkileşimli E-Okuyucu (Weight: 5)
                    Card(
                        modifier = Modifier
                            .weight(5f)
                            .fillMaxHeight()
                            .shadow(8.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderClr)
                    ) {
                        EBookReaderView(
                            book = selectedBook,
                            currentPageIndex = currentPageIndex,
                            onPageChange = { newPageIdx ->
                                currentPageIndex = newPageIdx
                                val totalPages = selectedBook.pages.size
                                if (totalPages > 0) {
                                    val newProgress = if (totalPages > 1) {
                                        kotlin.math.round((newPageIdx.toFloat() / (totalPages - 1)) * 100).toInt()
                                    } else {
                                        100
                                    }
                                    val idx = purchasedBooks.indexOfFirst { it.id == selectedBook.id }
                                    if (idx != -1) {
                                        purchasedBooks[idx] = purchasedBooks[idx].copy(progress = newProgress)
                                        selectedBook = purchasedBooks[idx]
                                    } else {
                                        val sIdx = sampleBooks.indexOfFirst { it.id == selectedBook.id }
                                        if (sIdx != -1) {
                                            sampleBooks[sIdx] = sampleBooks[sIdx].copy(progress = newProgress)
                                            selectedBook = sampleBooks[sIdx]
                                        }
                                    }
                                }
                            },
                            theme = readerTheme,
                            onThemeChange = { readerTheme = it },
                            fontSize = readerFontSize,
                            onFontSizeChange = { readerFontSize = it },
                            isSerif = isSerifFont,
                            onFontFamilyToggle = { isSerifFont = !isSerifFont }
                        )
                    }
                }
            } else {
                // ─── Mobil Düzeni (Tek Ekran) ──────────────────────────────────
                AnimatedContent(
                    targetState = showMobileReaderScreen,
                    transitionSpec = {
                        if (targetState) {
                            slideInVertically { it } + fadeIn() togetherWith
                                    slideOutVertically { -it } + fadeOut()
                        } else {
                            slideInVertically { -it } + fadeIn() togetherWith
                                    slideOutVertically { it } + fadeOut()
                        }
                    },
                    label = "mobile_ebook_transition"
                ) { isReaderVisible ->
                    if (isReaderVisible) {
                        // Mobil Detaylı Okuyucu Ekranı
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shadow(6.dp, RoundedCornerShape(20.dp)),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = CardBg),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderClr)
                            ) {
                                EBookReaderView(
                                    book = selectedBook,
                                    currentPageIndex = currentPageIndex,
                                    onPageChange = { newPageIdx ->
                                        currentPageIndex = newPageIdx
                                        val totalPages = selectedBook.pages.size
                                        if (totalPages > 0) {
                                            val newProgress = if (totalPages > 1) {
                                                kotlin.math.round((newPageIdx.toFloat() / (totalPages - 1)) * 100).toInt()
                                            } else {
                                                100
                                            }
                                            val idx = purchasedBooks.indexOfFirst { it.id == selectedBook.id }
                                            if (idx != -1) {
                                                purchasedBooks[idx] = purchasedBooks[idx].copy(progress = newProgress)
                                                selectedBook = purchasedBooks[idx]
                                            } else {
                                                val sIdx = sampleBooks.indexOfFirst { it.id == selectedBook.id }
                                                if (sIdx != -1) {
                                                    sampleBooks[sIdx] = sampleBooks[sIdx].copy(progress = newProgress)
                                                    selectedBook = sampleBooks[sIdx]
                                                }
                                            }
                                        }
                                    },
                                    theme = readerTheme,
                                    onThemeChange = { readerTheme = it },
                                    fontSize = readerFontSize,
                                    onFontSizeChange = { readerFontSize = it },
                                    isSerif = isSerifFont,
                                    onFontFamilyToggle = { isSerifFont = !isSerifFont }
                                )
                            }
                        }
                    } else {
                        // Mobil Kitap Listesi Ekranı
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                EBookListSections(
                                    purchasedBooks = purchasedBooks,
                                    sampleBooks = sampleBooks,
                                    activeBookId = selectedBook.id,
                                    onSelectBook = { book ->
                                        selectBookAndSyncProgress(book)
                                        showMobileReaderScreen = true
                                    },
                                    onBuyClick = {
                                        Toast.makeText(context, "E-Book satın alma sayfasına yönlendiriliyorsunuz...", Toast.LENGTH_LONG).show()
                                    }
                                )
                                Spacer(modifier = Modifier.height(110.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── Kitap Listeleri Arayüzü ───────────────────────────────
@Composable
private fun EBookListSections(
    purchasedBooks: List<EBook>,
    sampleBooks: List<EBook>,
    activeBookId: String,
    onSelectBook: (EBook) -> Unit,
    onBuyClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Satın Alınmış E-Booklar",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateLight,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        purchasedBooks.forEach { book ->
            val isActive = book.id == activeBookId
            EBookRowItem(
                book = book,
                isActive = isActive,
                onSelect = { onSelectBook(book) },
                buttonText = "Oku"
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tadımlık Oku",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateLight,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        sampleBooks.forEach { book ->
            val isActive = book.id == activeBookId
            EBookRowItem(
                book = book,
                isActive = isActive,
                onSelect = { onSelectBook(book) },
                buttonText = "Tadımlık Oku"
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(28.dp))

        // "E-Book Al" Butonu
        Button(
            onClick = onBuyClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(4.dp, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Orange)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "E-Book Al",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ─── Tek E-Kitap Satırı Öğesi ───────────────────────────────
@Composable
private fun EBookRowItem(
    book: EBook,
    isActive: Boolean,
    onSelect: () -> Unit,
    buttonText: String
) {
    val borderStroke = if (isActive) {
        androidx.compose.foundation.BorderStroke(1.5.dp, Orange)
    } else {
        androidx.compose.foundation.BorderStroke(1.dp, BorderClr)
    }

    val backgroundBrush = if (isActive) {
        Brush.verticalGradient(listOf(Color.White, LightOrange))
    } else {
        Brush.verticalGradient(listOf(Color.White, Color.White))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(14.dp))
            .clickable(onClick = onSelect), // Bütün kart tıklanabilir
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = borderStroke
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundBrush)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kapak Resmi
            AsyncImage(
                model = book.coverUrl,
                contentDescription = book.title,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(BorderClr),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Kitap Metin Bilgileri ve Okuma İlerlemesi
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextMain,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.author,
                    fontSize = 12.sp,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (book.progress >= 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LinearProgressIndicator(
                            progress = { book.progress / 100f },
                            color = Orange,
                            trackColor = BorderClr,
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(CircleShape)
                        )
                        Text(
                            text = "%${book.progress}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Orange
                        )
                    }
                } else {
                    Text(
                        text = "Henüz okunmadı",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Oku / Tadımlık Oku Durum Butonu (Görsel Gösterge)
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isActive) Orange else SlateLight)
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buttonText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// ─── Etkileşimli E-Okuyucu Görünümü ──────────────────────────
@Composable
private fun EBookReaderView(
    book: EBook,
    currentPageIndex: Int,
    onPageChange: (Int) -> Unit,
    theme: ReaderTheme,
    onThemeChange: (ReaderTheme) -> Unit,
    fontSize: androidx.compose.ui.unit.TextUnit,
    onFontSizeChange: (androidx.compose.ui.unit.TextUnit) -> Unit,
    isSerif: Boolean,
    onFontFamilyToggle: () -> Unit
) {
    val totalPages = book.pages.size
    val fontFamily = if (isSerif) FontFamily.Serif else FontFamily.Default

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(theme.bgColor)
            .padding(16.dp)
    ) {
        // ── Üst Araç Çubuğu (Reader Settings & Zoom) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .border(1.dp, theme.borderClr, RoundedCornerShape(12.dp))
                .background(theme.bgColor)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Yazı Tipi Kontrolü (Serif / Sans)
            TextButton(
                onClick = onFontFamilyToggle,
                contentPadding = PaddingValues(horizontal = 6.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = theme.textColor)
            ) {
                Text(
                    text = if (isSerif) "Serif" else "Sans",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Harf Boyutu Küçült / Büyüt (A- / A+)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        val current = fontSize.value
                        if (current > 12f) onFontSizeChange((current - 2f).sp)
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text("A-", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = theme.textColor)
                }
                IconButton(
                    onClick = {
                        val current = fontSize.value
                        if (current < 28f) onFontSizeChange((current + 2f).sp)
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text("A+", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = theme.textColor)
                }
            }

            // Temalar (Açık, Sepya, Karanlık)
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReaderTheme.values().forEach { th ->
                    val isSelected = th == theme
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(th.bgColor)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Orange else theme.borderClr,
                                shape = CircleShape
                            )
                            .clickable { onThemeChange(th) }
                    )
                }
            }

            // Sayfa Sayısı Göstergesi
            Text(
                text = "${currentPageIndex + 1} / $totalPages",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = theme.textColor.copy(alpha = 0.8f)
            )
        }

        // ── Kitap Başlığı ──
        Text(
            text = book.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = theme.textColor.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Kitap Sayfası İçeriği (Yumuşak Geçiş Animasyonlu) ──
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedContent(
                targetState = currentPageIndex,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith
                            fadeOut(animationSpec = tween(220))
                },
                label = "page_flip_transition"
            ) { pageIdx ->
                if (pageIdx == 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .width(180.dp)
                                .height(260.dp)
                                .shadow(12.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = theme.bgColor),
                            border = androidx.compose.foundation.BorderStroke(1.dp, theme.borderClr)
                        ) {
                            AsyncImage(
                                model = book.coverUrl,
                                contentDescription = book.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = book.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamily,
                            color = theme.textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = book.author,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = fontFamily,
                            color = theme.textColor.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Orange.copy(alpha = 0.15f),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            Text(
                                text = "KAPAK SAYFASI",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Orange,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                } else {
                    Text(
                        text = book.pages.getOrElse(pageIdx) { "" },
                        fontSize = fontSize,
                        fontFamily = fontFamily,
                        color = theme.textColor,
                        lineHeight = (fontSize.value * 1.55f).sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Alt Navigasyon Kontrolleri (Önceki - İlerleme - Sonraki) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Önceki Sayfa
            TextButton(
                onClick = {
                    if (currentPageIndex > 0) onPageChange(currentPageIndex - 1)
                },
                enabled = currentPageIndex > 0,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = theme.textColor,
                    disabledContentColor = theme.textColor.copy(alpha = 0.25f)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null, modifier = Modifier.size(16.dp))
                    Text("Önceki", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // İlerleme Çubuğu (Lineer Çizgi)
            LinearProgressIndicator(
                progress = { if (totalPages > 1) currentPageIndex.toFloat() / (totalPages - 1) else 1f },
                color = Orange,
                trackColor = theme.borderClr,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .height(6.dp)
                    .clip(CircleShape)
            )

            // Sonraki Sayfa
            TextButton(
                onClick = {
                    if (currentPageIndex < totalPages - 1) onPageChange(currentPageIndex + 1)
                },
                enabled = currentPageIndex < totalPages - 1,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = theme.textColor,
                    disabledContentColor = theme.textColor.copy(alpha = 0.25f)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Sonraki", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
