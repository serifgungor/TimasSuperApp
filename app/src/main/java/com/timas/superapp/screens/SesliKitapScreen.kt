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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.timas.superapp.Book
import com.timas.superapp.LibraryDatabase

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
private val PlayerCardBg = Color(0xFFF1F5F9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SesliKitapScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Global kütüphane veritabanından dinamik olarak sesli kitap verilerini çekiyoruz
    val libraryBooks = LibraryDatabase.books
    val purchasedBooks = libraryBooks.filter { it.isOwned }
    val sampleBooks = libraryBooks.filter { !it.isOwned }

    // Durum Yönetimi
    var selectedBook by remember { mutableStateOf(purchasedBooks.firstOrNull() ?: libraryBooks[0]) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPositionSeconds by remember(selectedBook.title) { mutableStateOf(selectedBook.audioPositionSeconds) }
    var playbackSpeed by remember { mutableStateOf(1.0f) }
    var isMuted by remember { mutableStateOf(false) }
    var currentVolume by remember { mutableStateOf(0.8f) }
    
    // Telefonlarda oynatıcı ekranını tam ekran gösterme flag'i
    var showMobilePlayerScreen by remember { mutableStateOf(false) }

    val listScrollState = rememberScrollState()

    // Oynatma ilerleme verisini global veritabanına yazarak diğer ekranlar (örn. 3D Kütüphane) ile senkronize ediyoruz
    val updateAudioProgress: (String, Float) -> Unit = { title, position ->
        val idx = LibraryDatabase.books.indexOfFirst { it.title == title }
        if (idx != -1) {
            val book = LibraryDatabase.books[idx]
            val progressPercent = ((position / book.totalSeconds) * 100).toInt().coerceIn(0, 100)
            LibraryDatabase.books[idx] = book.copy(
                audioPositionSeconds = position,
                progress = progressPercent
            )
        }
    }

    // Oynatma süresi simülasyonu (250ms periyotlu akıcı ve sorunsuz ilerleme)
    LaunchedEffect(isPlaying, playbackSpeed, selectedBook) {
        if (isPlaying) {
            while (isPlaying && currentPositionSeconds < selectedBook.totalSeconds) {
                delay(250)
                currentPositionSeconds = minOf(
                    selectedBook.totalSeconds.toFloat(),
                    currentPositionSeconds + (0.25f * playbackSpeed)
                )
                updateAudioProgress(selectedBook.title, currentPositionSeconds)
            }
            if (currentPositionSeconds >= selectedBook.totalSeconds) {
                isPlaying = false
            }
        }
    }

    // Ekran Boyutu Tespiti
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sesli Kitap",
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (showMobilePlayerScreen && !isTablet) {
                            showMobilePlayerScreen = false
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
                        BookListSections(
                            purchasedBooks = purchasedBooks,
                            sampleBooks = sampleBooks,
                            activeBookTitle = selectedBook.title,
                            onPlayBook = { book ->
                                selectedBook = book
                                currentPositionSeconds = book.audioPositionSeconds
                                isPlaying = true
                                Toast.makeText(context, "${book.title} oynatılıyor...", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    // Sağ Taraf: Detaylı Oynatıcı (Weight: 5)
                    Card(
                        modifier = Modifier
                            .weight(5f)
                            .fillMaxHeight()
                            .shadow(8.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderClr)
                    ) {
                        AudioPlayerView(
                            book = selectedBook,
                            isPlaying = isPlaying,
                            onPlayPauseToggle = { isPlaying = !isPlaying },
                            currentPositionSeconds = currentPositionSeconds,
                            onPositionChange = { newPos ->
                                currentPositionSeconds = newPos
                                updateAudioProgress(selectedBook.title, newPos)
                            },
                            playbackSpeed = playbackSpeed,
                            onSpeedChange = {
                                playbackSpeed = when (playbackSpeed) {
                                    1.0f -> 1.25f
                                    1.25f -> 1.5f
                                    1.5f -> 2.0f
                                    else -> 1.0f
                                }
                            },
                            isMuted = isMuted,
                            onMuteToggle = { isMuted = !isMuted },
                            volume = currentVolume,
                            onVolumeChange = { currentVolume = it },
                            onBuyClick = {
                                Toast.makeText(context, "${selectedBook.title} satın alma sayfasına yönlendiriliyorsunuz...", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                }
            } else {
                // ─── Mobil Düzeni (Tek Ekran) ──────────────────────────────────
                AnimatedContent(
                    targetState = showMobilePlayerScreen,
                    transitionSpec = {
                        if (targetState) {
                            slideInVertically { it } + fadeIn() togetherWith
                                    slideOutVertically { -it } + fadeOut()
                        } else {
                            slideInVertically { -it } + fadeIn() togetherWith
                                    slideOutVertically { it } + fadeOut()
                        }
                    },
                    label = "mobile_screen_transition"
                ) { isPlayerVisible ->
                    if (isPlayerVisible) {
                        // Mobil Detaylı Oynatıcı Ekranı
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
                                AudioPlayerView(
                                    book = selectedBook,
                                    isPlaying = isPlaying,
                                    onPlayPauseToggle = { isPlaying = !isPlaying },
                                    currentPositionSeconds = currentPositionSeconds,
                                    onPositionChange = { newPos ->
                                        currentPositionSeconds = newPos
                                        updateAudioProgress(selectedBook.title, newPos)
                                    },
                                    playbackSpeed = playbackSpeed,
                                    onSpeedChange = {
                                        playbackSpeed = when (playbackSpeed) {
                                            1.0f -> 1.25f
                                            1.25f -> 1.5f
                                            1.5f -> 2.0f
                                            else -> 1.0f
                                        }
                                    },
                                    isMuted = isMuted,
                                    onMuteToggle = { isMuted = !isMuted },
                                    volume = currentVolume,
                                    onVolumeChange = { currentVolume = it },
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
                                BookListSections(
                                    purchasedBooks = purchasedBooks,
                                    sampleBooks = sampleBooks,
                                    activeBookTitle = selectedBook.title,
                                    onPlayBook = { book ->
                                        selectedBook = book
                                        currentPositionSeconds = book.audioPositionSeconds
                                        isPlaying = true
                                        showMobilePlayerScreen = true
                                    }
                                )
                                Spacer(modifier = Modifier.height(110.dp))
                            }

                            // Alt Mini Oynatıcı (Yalnızca bir kitap seçili olduğunda)
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                MiniPlayerView(
                                    book = selectedBook,
                                    isPlaying = isPlaying,
                                    onPlayPauseToggle = { isPlaying = !isPlaying },
                                    onOpenDetails = { showMobilePlayerScreen = true }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── Kitap Listeleri Arayüzü (Arama ve Sekmeli Yapı) ─────────────────────────
@Composable
private fun BookListSections(
    purchasedBooks: List<Book>,
    sampleBooks: List<Book>,
    activeBookTitle: String,
    onPlayBook: (Book) -> Unit
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
                text = { Text("Tadımlık Dinle", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
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
                        placeholder = { Text("Satın alınan sesli kitaplarda ara...", color = TextMuted, fontSize = 14.sp) },
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
                                text = "Aramanıza uygun satın alınmış sesli kitap bulunamadı.",
                                color = TextMuted,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        filteredPurchased.forEach { book ->
                            val isActive = book.title == activeBookTitle
                            BookRowItem(
                                book = book,
                                isActive = isActive,
                                onPlay = { onPlayBook(book) },
                                buttonText = "Dinle"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                } else {
                    // Tadımlık Arama Kutusu
                    OutlinedTextField(
                        value = sampleSearchQuery,
                        onValueChange = { sampleSearchQuery = it },
                        placeholder = { Text("Tadımlık sesli kitaplarda ara...", color = TextMuted, fontSize = 14.sp) },
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

                    // Tadımlık Dinle Kitaplar Listesi
                    if (filteredSample.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aramanıza uygun tadımlık sesli kitap bulunamadı.",
                                color = TextMuted,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        filteredSample.forEach { book ->
                            val isActive = book.title == activeBookTitle
                            BookRowItem(
                                book = book,
                                isActive = isActive,
                                onPlay = { onPlayBook(book) },
                                buttonText = "Tadımlık Dinle"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

// ─── Tek Kitap Satırı Öğesi ─────────────────────────────────
@Composable
private fun BookRowItem(
    book: Book,
    isActive: Boolean,
    onPlay: () -> Unit,
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
            .clickable(onClick = onPlay),
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
            // Kitap Kapağı
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

            // Kitap Bilgileri
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = book.duration,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Orange
                    )
                    if (book.progress >= 0) {
                        Text(
                            text = "• %${book.progress}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    }
                }
            }

            // Aksiyon Göstergesi
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

// ─── Mobil Alt Mini Oynatıcı ──────────────────────────────
@Composable
private fun MiniPlayerView(
    book: Book,
    isPlaying: Boolean,
    onPlayPauseToggle: () -> Unit,
    onOpenDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .clickable(onClick = onOpenDetails),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSlate)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kitap Kapağı
            AsyncImage(
                model = book.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Kitap Başlık/Sanatçı
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Seslendiren: ${book.narrator}",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Oynat/Durdur Butonu
            IconButton(
                onClick = onPlayPauseToggle,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Oynat/Durdur",
                    tint = Color.White
                )
            }
        }
    }
}

// ─── Detaylı Oynatıcı Görünümü ─────────────────────────────
@Composable
private fun AudioPlayerView(
    book: Book,
    isPlaying: Boolean,
    onPlayPauseToggle: () -> Unit,
    currentPositionSeconds: Float,
    onPositionChange: (Float) -> Unit,
    playbackSpeed: Float,
    onSpeedChange: () -> Unit,
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    onBuyClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // En Üst Kitap Adı
        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextMain,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Büyük Kitap Kapağı Gölgesiyle birlikte
        Box(
            modifier = Modifier
                .size(170.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(BorderClr)
        ) {
            AsyncImage(
                model = book.coverUrl,
                contentDescription = book.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Kitap Başlık, Yazar ve Seslendiren Bilgisi
        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextMain,
            textAlign = TextAlign.Center
        )
        Text(
            text = book.author,
            fontSize = 13.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp)
        )
        Text(
            text = "Seslendiren: ${book.narrator}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = SlateLight,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "Bölüm 1 - Giriş",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Orange,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // İlerleme Çubuğu (Slider)
        Slider(
            value = currentPositionSeconds,
            onValueChange = onPositionChange,
            valueRange = 0f..book.totalSeconds.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Orange,
                activeTrackColor = Orange,
                inactiveTrackColor = BorderClr
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Süre Bilgileri
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPositionSeconds.toInt()),
                fontSize = 12.sp,
                color = TextMuted
            )
            Text(
                text = formatTime(book.totalSeconds),
                fontSize = 12.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Kontroller Satırı
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 15sn Geri
            IconButton(
                onClick = { onPositionChange(maxOf(0f, currentPositionSeconds - 15f)) },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, BorderClr, CircleShape)
            ) {
                Text(
                    text = "-15",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
            }

            // Oynat / Durdur (Büyük Buton)
            IconButton(
                onClick = onPlayPauseToggle,
                modifier = Modifier
                    .size(56.dp)
                    .shadow(4.dp, CircleShape)
                    .background(Orange, CircleShape)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Oynat/Durdur",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // 15sn İleri
            IconButton(
                onClick = { onPositionChange(minOf(book.totalSeconds.toFloat(), currentPositionSeconds + 15f)) },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, BorderClr, CircleShape)
            ) {
                Text(
                    text = "+15",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ekstra Kontroller: Hız & Ses Seviyesi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Oynatma Hızı Ayarı
            OutlinedButton(
                onClick = onSpeedChange,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderClr),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMain)
            ) {
                Text(
                    text = "${playbackSpeed}x",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Ses Açma/Kapama & Kaydırıcı
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(150.dp)
            ) {
                IconButton(onClick = onMuteToggle) {
                    Icon(
                        imageVector = if (isMuted || volume == 0f) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Ses Seviyesi",
                        tint = TextMain
                    )
                }
                Slider(
                    value = if (isMuted) 0f else volume,
                    onValueChange = onVolumeChange,
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = DarkSlate,
                        activeTrackColor = DarkSlate,
                        inactiveTrackColor = BorderClr
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Kitap Açıklaması Kutusu
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PlayerCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderClr.copy(alpha = 0.8f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Kitap Hakkında",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = SlateLight
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = book.description,
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            }
        }

        // Satın Alınmamış Kitaplar İçin Satın Al Butonu (Buraya Taşındı)
        if (!book.isOwned) {
            Spacer(modifier = Modifier.height(20.dp))
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
                    text = "Sesli Kitabı Satın Al",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// ─── Yardımcı Fonksiyon: Saniye -> Saat:Dakika:Saniye formatlama ────
private fun formatTime(seconds: Int): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return if (h > 0) {
        String.format("%02d:%02d:%02d", h, m, s)
    } else {
        String.format("%02d:%02d", m, s)
    }
}
