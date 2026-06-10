package com.timas.superapp.screens

import android.widget.Toast
import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
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
private val PlayerCardBg = Color(0xFFF1F5F9)

// ─── Veri Sınıfı ─────────────────────────────────────────────
data class Audiobook(
    val id: String,
    val title: String,
    val author: String,
    val narrator: String,
    val duration: String,
    val coverUrl: String,
    val description: String,
    val totalSeconds: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SesliKitapScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Kitap Listeleri
    val purchasedBooks = remember {
        listOf(
            Audiobook(
                id = "p1",
                title = "Mutluluğun İnşası",
                author = "Mecit Ömür Öztürk",
                narrator = "Ufuk Bayraktar",
                duration = "4 sa 32 dk",
                totalSeconds = 16320,
                coverUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?auto=format&fit=crop&q=80&w=300",
                description = "Mutluluk bir durum değil, bir yolculuktur. Bu sesli kitapta, iç huzuru bulmanın ve gerçek mutluluğu inşa etmenin adımlarını dinleyeceksiniz. Her bölüm, pratik egzersizler ve düşünce provokasyonları ile desteklenmiştir."
            ),
            Audiobook(
                id = "p2",
                title = "Dilin Afetleri",
                author = "İmam Gazali",
                narrator = "Sinan Bengier",
                duration = "3 sa 15 dk",
                totalSeconds = 11700,
                coverUrl = "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&q=80&w=300",
                description = "İnsanın en büyük imtihanlarından biri olan dilin, ahlak ve maneviyat üzerindeki etkilerini anlatan klasik bir eser. Gıybet, yalan ve boş konuşma gibi dilin afetlerinden korunma yolları akıcı bir üslupla sunuluyor."
            ),
            Audiobook(
                id = "p3",
                title = "Kur'an Atlası",
                author = "Timaş Yayınları",
                narrator = "Seda Yücel",
                duration = "6 sa 10 dk",
                totalSeconds = 22200,
                coverUrl = "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?auto=format&fit=crop&q=80&w=300",
                description = "Kur'an-ı Kerim'de adı geçen coğrafi mekanların, kavimlerin ve olayların tarihi ve arkeolojik bilgilerle açıklandığı kapsamlı bir rehber eser."
            )
        )
    }

    val sampleBooks = remember {
        listOf(
            Audiobook(
                id = "s1",
                title = "Kalpsizler",
                author = "Timaş Yayınları",
                narrator = "Cem Yılmaz",
                duration = "5 sa 45 dk",
                totalSeconds = 20700,
                coverUrl = "https://images.unsplash.com/photo-1532012197267-da84d127e765?auto=format&fit=crop&q=80&w=300",
                description = "Kalpsizlerin dünyasında insanlık sorgulanıyor. Distopik bir gelecekte, duygulardan arındırılmış bir toplumda var olma mücadelesi veren gençlerin hikayesi."
            ),
            Audiobook(
                id = "s2",
                title = "Politik Bir Beden",
                author = "Timaş Yayınları",
                narrator = "Ayşe Kulin",
                duration = "4 sa 20 dk",
                totalSeconds = 15600,
                coverUrl = "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?auto=format&fit=crop&q=80&w=300",
                description = "Toplumsal cinsiyet ve siyaset üzerine modern bir analiz. Bedenin politikleşmesi, iktidar ilişkileri ve bireysel özgürlüklerin sınırları tartışılıyor."
            ),
            Audiobook(
                id = "s3",
                title = "Od",
                author = "İskender Pala",
                narrator = "Haluk Bilginer",
                duration = "7 sa 05 dk",
                totalSeconds = 25500,
                coverUrl = "https://images.unsplash.com/photo-1474932430478-367dbb6832c1?auto=format&fit=crop&q=80&w=300",
                description = "Yunus Emre'nin hayatından kesitler sunan, tasavvufi derinliği ve şiirsel anlatımıyla dinleyiciyi büyüleyen tarihi bir roman."
            )
        )
    }

    // Durum Yönetimi
    var selectedBook by remember { mutableStateOf(purchasedBooks[0]) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPositionSeconds by remember { mutableStateOf(0f) }
    var playbackSpeed by remember { mutableStateOf(1.0f) }
    var isMuted by remember { mutableStateOf(false) }
    var currentVolume by remember { mutableStateOf(0.8f) }
    
    // Telefonlarda oynatıcı ekranını tam ekran gösterme flag'i
    var showMobilePlayerScreen by remember { mutableStateOf(false) }

    // Oynatma süresi simülasyonu (250ms periyotlu akıcı ve sorunsuz ilerleme)
    LaunchedEffect(isPlaying, playbackSpeed, selectedBook) {
        if (isPlaying) {
            while (isPlaying && currentPositionSeconds < selectedBook.totalSeconds) {
                delay(250)
                currentPositionSeconds = minOf(
                    selectedBook.totalSeconds.toFloat(),
                    currentPositionSeconds + (0.25f * playbackSpeed)
                )
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
                        BookListSections(
                            purchasedBooks = purchasedBooks,
                            sampleBooks = sampleBooks,
                            activeBookId = selectedBook.id,
                            onPlayBook = { book, isSample ->
                                selectedBook = book
                                currentPositionSeconds = 0f
                                isPlaying = true
                                Toast.makeText(context, "${book.title} oynatılıyor...", Toast.LENGTH_SHORT).show()
                            },
                            onBuyClick = {
                                Toast.makeText(context, "Sesli Kitap satın alma sayfasına yönlendiriliyorsunuz...", Toast.LENGTH_LONG).show()
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
                            onPositionChange = { currentPositionSeconds = it },
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
                            onVolumeChange = { currentVolume = it }
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
                                    onPositionChange = { currentPositionSeconds = it },
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
                                    onVolumeChange = { currentVolume = it }
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
                                BookListSections(
                                    purchasedBooks = purchasedBooks,
                                    sampleBooks = sampleBooks,
                                    activeBookId = selectedBook.id,
                                    onPlayBook = { book, isSample ->
                                        selectedBook = book
                                        currentPositionSeconds = 0f
                                        isPlaying = true
                                        showMobilePlayerScreen = true
                                    },
                                    onBuyClick = {
                                        Toast.makeText(context, "Sesli Kitap satın alma sayfasına yönlendiriliyorsunuz...", Toast.LENGTH_LONG).show()
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

// ─── Kitap Listeleri Arayüzü ───────────────────────────────
@Composable
private fun BookListSections(
    purchasedBooks: List<Audiobook>,
    sampleBooks: List<Audiobook>,
    activeBookId: String,
    onPlayBook: (Audiobook, Boolean) -> Unit,
    onBuyClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Satın Alınmış Sesli Kitaplar",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateLight,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        
        purchasedBooks.forEach { book ->
            val isActive = book.id == activeBookId
            BookRowItem(
                book = book,
                isActive = isActive,
                onPlay = { onPlayBook(book, false) },
                buttonText = "Dinle"
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tadımlık Dinle",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SlateLight,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        sampleBooks.forEach { book ->
            val isActive = book.id == activeBookId
            BookRowItem(
                book = book,
                isActive = isActive,
                onPlay = { onPlayBook(book, true) },
                buttonText = "Tadımlık Dinle"
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(28.dp))

        // "Sesli Kitap Al" Butonu
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
                    imageVector = Icons.Default.Headphones,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sesli Kitap Al",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ─── Tek Kitap Satırı Öğesi ─────────────────────────────────
@Composable
private fun BookRowItem(
    book: Audiobook,
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
            .clickable(onClick = onPlay), // Kartın tamamı artık tıklanabilir
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
                Text(
                    text = book.duration,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Orange
                )
            }

            // Aksiyon Göstergesi (Artık buton değil, sadece şık bir gösterge kutusu)
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
    book: Audiobook,
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
    book: Audiobook,
    isPlaying: Boolean,
    onPlayPauseToggle: () -> Unit,
    currentPositionSeconds: Float,
    onPositionChange: (Float) -> Unit,
    playbackSpeed: Float,
    onSpeedChange: () -> Unit,
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    volume: Float,
    onVolumeChange: (Float) -> Unit
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
                        imageVector = if (isMuted || volume == 0f) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
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
