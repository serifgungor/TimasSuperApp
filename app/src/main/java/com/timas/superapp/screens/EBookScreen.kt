package com.timas.superapp.screens

import com.timas.superapp.Book
import com.timas.superapp.LibraryDatabase

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EBookScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // E-Kitap Listeleri (Değişikliklerin ekranda anlık güncellenmesi için mutableStateListOf kullanıyoruz)
    val libraryBooks = LibraryDatabase.books
    val purchasedBooks = libraryBooks.filter { it.isOwned }
    val sampleBooks = libraryBooks.filter { !it.isOwned }

    // Durum Yönetimi
    var selectedBook by remember { mutableStateOf(purchasedBooks.firstOrNull() ?: libraryBooks[0]) }

    val initialPage = remember(selectedBook.id) {
        val total = selectedBook.getBookPages().size
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

    val selectBookAndSyncProgress: (Book) -> Unit = { book ->
        val total = book.getBookPages().size
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

        val idx = LibraryDatabase.books.indexOfFirst { it.title == book.title }
        if (idx != -1) {
            LibraryDatabase.books[idx] = LibraryDatabase.books[idx].copy(progress = exactProgress)
            selectedBook = LibraryDatabase.books[idx]
        }
    }

    // Okuyucu Kişiselleştirme Ayarları
    var readerTheme by remember { mutableStateOf(ReaderTheme.SEPIA) }
    var readerFontSize by remember { mutableStateOf(16.sp) }
    var isSerifFont by remember { mutableStateOf(true) }

    // Telefonlarda okuyucuyu tam ekran gösterme flag'i
    var showMobileReaderScreen by remember { mutableStateOf(false) }

    val listScrollState = rememberScrollState()

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
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
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
                            .verticalScroll(listScrollState)
                    ) {
                        EBookListSections(
                            purchasedBooks = purchasedBooks,
                            sampleBooks = sampleBooks,
                            activeBookId = selectedBook.id,
                            onSelectBook = { book ->
                                selectBookAndSyncProgress(book)
                                Toast.makeText(context, "${book.title} açıldı.", Toast.LENGTH_SHORT).show()
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
                                val totalPages = selectedBook.getBookPages().size
                                if (totalPages > 0) {
                                    val newProgress = if (totalPages > 1) {
                                        kotlin.math.round((newPageIdx.toFloat() / (totalPages - 1)) * 100).toInt()
                                    } else {
                                        100
                                    }
                                    val idx = LibraryDatabase.books.indexOfFirst { it.title == selectedBook.title }
                                    if (idx != -1) {
                                        LibraryDatabase.books[idx] = LibraryDatabase.books[idx].copy(progress = newProgress)
                                        selectedBook = LibraryDatabase.books[idx]
                                    }
                                }
                            },
                            theme = readerTheme,
                            onThemeChange = { readerTheme = it },
                            fontSize = readerFontSize,
                            onFontSizeChange = { readerFontSize = it },
                            isSerif = isSerifFont,
                            onFontFamilyToggle = { isSerifFont = !isSerifFont },
                            onBuyClick = {
                                Toast.makeText(context, "${selectedBook.title} satın alma sayfasına yönlendiriliyorsunuz...", Toast.LENGTH_LONG).show()
                            }
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
                                        val totalPages = selectedBook.getBookPages().size
                                        if (totalPages > 0) {
                                            val newProgress = if (totalPages > 1) {
                                                kotlin.math.round((newPageIdx.toFloat() / (totalPages - 1)) * 100).toInt()
                                            } else {
                                                100
                                            }
                                            val idx = LibraryDatabase.books.indexOfFirst { it.title == selectedBook.title }
                                    if (idx != -1) {
                                        LibraryDatabase.books[idx] = LibraryDatabase.books[idx].copy(progress = newProgress)
                                        selectedBook = LibraryDatabase.books[idx]
                                    }
                                        }
                                    },
                                    theme = readerTheme,
                                    onThemeChange = { readerTheme = it },
                                    fontSize = readerFontSize,
                                    onFontSizeChange = { readerFontSize = it },
                                    isSerif = isSerifFont,
                                    onFontFamilyToggle = { isSerifFont = !isSerifFont },
                                    onBuyClick = {
                                        Toast.makeText(context, "${selectedBook.title} satın alma sayfasına yönlendiriliyorsunuz...", Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                        }
                    } else {
                        // Mobil Kitap Listesi Ekranı
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(listScrollState)
                            ) {
                                EBookListSections(
                                    purchasedBooks = purchasedBooks,
                                    sampleBooks = sampleBooks,
                                    activeBookId = selectedBook.id,
                                    onSelectBook = { book ->
                                        selectBookAndSyncProgress(book)
                                        showMobileReaderScreen = true
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

// ─── Kitap Listeleri Arayüzü (Arama ve Tabs Desteği ile) ────────────────────────
@Composable
private fun EBookListSections(
    purchasedBooks: List<Book>,
    sampleBooks: List<Book>,
    activeBookId: String,
    onSelectBook: (Book) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    var purchasedSearchQuery by remember { mutableStateOf("") }
    var sampleSearchQuery by remember { mutableStateOf("") }

    val filteredPurchased = remember(purchasedSearchQuery, purchasedBooks) {
        purchasedBooks.filter {
            it.title.contains(purchasedSearchQuery, ignoreCase = true) ||
            it.author.contains(purchasedSearchQuery, ignoreCase = true)
        }
    }

    val filteredSample = remember(sampleSearchQuery, sampleBooks) {
        sampleBooks.filter {
            it.title.contains(sampleSearchQuery, ignoreCase = true) ||
            it.author.contains(sampleSearchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Sekmeler (Kendi aralarında kaymalı geçişi destekler)
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = BgColor,
            contentColor = Orange,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = { Text("Satın Alınanlar", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                selectedContentColor = Orange,
                unselectedContentColor = TextMuted
            )
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = { Text("Tadımlık Oku", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                selectedContentColor = Orange,
                unselectedContentColor = TextMuted
            )
        }

        // Pager ile kaydırılabilir içerikler
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { pageIndex ->
            Column(modifier = Modifier.fillMaxWidth()) {
                if (pageIndex == 0) {
                    // Satın Alınanlar Arama Kutusu
                    OutlinedTextField(
                        value = purchasedSearchQuery,
                        onValueChange = { purchasedSearchQuery = it },
                        placeholder = { Text("Satın alınan kitaplarda ara...", color = TextMuted, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Orange) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Orange,
                            unfocusedBorderColor = BorderClr,
                            focusedContainerColor = CardBg,
                            unfocusedContainerColor = CardBg
                        ),
                        singleLine = true
                    )

                    // Satın Alınan Kitaplar Listesi
                    if (filteredPurchased.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aramanıza uygun satın alınmış kitap bulunamadı.",
                                color = TextMuted,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        filteredPurchased.forEach { book ->
                            val isActive = book.id == activeBookId
                            EBookRowItem(
                                book = book,
                                isActive = isActive,
                                onSelect = { onSelectBook(book) },
                                buttonText = "Oku"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                } else {
                    // Tadımlık Arama Kutusu
                    OutlinedTextField(
                        value = sampleSearchQuery,
                        onValueChange = { sampleSearchQuery = it },
                        placeholder = { Text("Tadımlık kitaplarda ara...", color = TextMuted, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Orange) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Orange,
                            unfocusedBorderColor = BorderClr,
                            focusedContainerColor = CardBg,
                            unfocusedContainerColor = CardBg
                        ),
                        singleLine = true
                    )

                    // Tadımlık Oku Kitaplar Listesi
                    if (filteredSample.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aramanıza uygun tadımlık kitap bulunamadı.",
                                color = TextMuted,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        filteredSample.forEach { book ->
                            val isActive = book.id == activeBookId
                            EBookRowItem(
                                book = book,
                                isActive = isActive,
                                onSelect = { onSelectBook(book) },
                                buttonText = "Tadımlık Oku"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

// ─── Tek E-Kitap Satırı Öğesi ───────────────────────────────
@Composable
private fun EBookRowItem(
    book: Book,
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
            .clickable(onClick = onSelect),
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

            // Oku / Tadımlık Oku Durum Butonu
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
    book: Book,
    currentPageIndex: Int,
    onPageChange: (Int) -> Unit,
    theme: ReaderTheme,
    onThemeChange: (ReaderTheme) -> Unit,
    fontSize: androidx.compose.ui.unit.TextUnit,
    onFontSizeChange: (androidx.compose.ui.unit.TextUnit) -> Unit,
    isSerif: Boolean,
    onFontFamilyToggle: () -> Unit,
    onBuyClick: () -> Unit
) {
    val totalPages = book.getBookPages().size
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
            // Yazı Tipi Kontrolü
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

            // Harf Boyutu
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

            // Temalar
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

        // ── Kitap Sayfası İçeriği ──
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
                    if (book.id.startsWith("es")) {
                        // TADIMLIK OKU DETAYLI KAPAK SAYFASI (Yazar ve Özet Bilgileri Tek Sayfa - Req 1)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(145.dp)
                                        .shadow(8.dp, RoundedCornerShape(12.dp)),
                                    shape = RoundedCornerShape(12.dp),
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
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = book.title,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = fontFamily,
                                        color = theme.textColor
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = book.author,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        fontFamily = fontFamily,
                                        color = theme.textColor.copy(alpha = 0.7f)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Orange.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "TADIMLIK OKU",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Orange,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            // Basit çizgi ayırıcı
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(theme.borderClr)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Yazar Hakkında",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = fontFamily,
                                color = theme.textColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = book.authorDetails,
                                fontSize = 12.sp,
                                fontFamily = fontFamily,
                                color = theme.textColor.copy(alpha = 0.8f),
                                lineHeight = 18.sp,
                                textAlign = TextAlign.Justify
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Kitap Özeti",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = fontFamily,
                                color = theme.textColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = book.shortSummary,
                                fontSize = 12.sp,
                                fontFamily = fontFamily,
                                color = theme.textColor.copy(alpha = 0.8f),
                                lineHeight = 18.sp,
                                textAlign = TextAlign.Justify
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // KAPAK ÜZERİNDEKİ SATIN AL BUTONU (Req 3)
                            Button(
                                onClick = onBuyClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Orange)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Bu Kitabı Satın Al",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        // Satın Alınan Kitaplar İçin Klasik Kapak Tasarımı
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
                    }
                } else {
                    Text(
                        text = book.getBookPages().getOrElse(pageIdx) { "" },
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

        // ── Alt Navigasyon Kontrolleri ──
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
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, modifier = Modifier.size(16.dp))
                    Text("Önceki", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // İlerleme Çubuğu
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
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }

        // TADIMLIK KİTAPLARDA OKUMA ESNASINDA DA ALTA SATIN ALMA SEÇENEĞİ (Req 3)
        if (book.id.startsWith("es") && currentPageIndex > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onBuyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .shadow(2.dp, RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Kitabı Satın Al",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
