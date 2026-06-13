package com.timas.superapp.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.delay

// ─── Renk Paleti (E-Book ve Sesli Kitap ile Uyumlu) ───────────────────
private val Orange       = Color(0xFFF26122)
private val LightOrange  = Color(0xFFFFF0EB)
private val DarkSlate    = Color(0xFF1E293B)
private val SlateLight   = Color(0xFF334155)
private val BgColor      = Color(0xFFF8FAFC)
private val CardBg       = Color(0xFFFFFFFF)
private val TextMain     = Color(0xFF0F172A)
private val TextMuted    = Color(0xFF64748B)
private val BorderClr    = Color(0xFFE2E8F0)

// ---------------- DATA STRUCTURES ----------------

data class ClubMember(
    val name: String,
    val avatarUrl: String,
    val isOnline: Boolean = false,
    val role: String = "Üye",
    val currentBook: String = "",
    val booksFinished: Int = 0
)

data class DiscussionPost(
    val authorName: String,
    val authorAvatarUrl: String,
    val timeAgo: String,
    val content: String,
    val likesCount: Int,
    var isLikedByUser: Boolean = false,
    val tag: String = "Genel"
)

data class PastReading(
    val title: String,
    val author: String,
    val coverUrl: String,
    val rating: Float,
    val completionDate: String
)

data class ClubEvent(
    val id: String,
    val title: String,
    val dateDay: String,
    val dateMonth: String,
    val dateFull: String,
    val platform: String,
    val description: String,
    val coverUrl: String,
    var isRSVPed: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OkumaKulubuScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // 1. Mock Data Source
    val members = remember {
        listOf(
            ClubMember("Nazan B.", "https://picsum.photos/id/1011/100/100", true, "Moderatör", "Mücella", 24),
            ClubMember("Ahmet Y.", "https://picsum.photos/id/1025/100/100", true, "Üye", "Dirilt Kalbini", 12),
            ClubMember("Zeynep K.", "https://picsum.photos/id/1012/100/100", false, "Üye", "Nar Ağacı", 18),
            ClubMember("Elif D.", "https://picsum.photos/id/1027/100/100", true, "Üye", "Mücella", 9),
            ClubMember("Mehmet A.", "https://picsum.photos/id/1005/100/100", false, "Üye", "Malamander", 15),
            ClubMember("Buse T.", "https://picsum.photos/id/1015/100/100", true, "Üye", "Kalpsizler", 7),
            ClubMember("Can S.", "https://picsum.photos/id/1016/100/100", false, "Üye", "Mücella", 11)
        )
    }

    val pastReadings = remember {
        listOf(
            PastReading("Nar Ağacı", "Nazan Bekiroğlu", "https://cdn.timas.com.tr/urun/nar-agaci-9786050807073.jpg", 4.9f, "Mayıs 2026"),
            PastReading("Malamander", "Thomas Taylor", "https://cdn.timas.com.tr/urun/malamander-9786050833775.jpg", 4.7f, "Nisan 2026"),
            PastReading("Dirilt Kalbini", "Nouman Ali Khan", "https://cdn.timas.com.tr/urun/dirilt-kalbini-9786050825992.jpg", 4.8f, "Mart 2026"),
            PastReading("Kalpsizler", "Marissa Meyer", "https://cdn.timas.com.tr/urun/kalpsizler-9786050847642.jpg", 4.5f, "Şubat 2026")
        )
    }

    val discussionPosts = remember {
        mutableStateListOf(
            DiscussionPost(
                "Nazan Bekiroğlu",
                "https://picsum.photos/id/1011/100/100",
                "2 saat önce",
                "Mücella'nın hikayesinde Osmanlı'nın son döneminden Cumhuriyet'e geçiş sancılarını karakterler üzerinden nasıl hissettiniz?",
                24,
                false,
                "Karakterler"
            ),
            DiscussionPost(
                "Ahmet Yılmaz",
                "https://picsum.photos/id/1025/100/100",
                "4 saat önce",
                "Mücella'nın sessizliği ve hayatı kabul edişi beni çok etkiledi. Bugünün dünyasıyla tezat bir karakter.",
                15,
                true,
                "Sembolizm"
            ),
            DiscussionPost(
                "Elif Demir",
                "https://picsum.photos/id/1027/100/100",
                "6 saat önce",
                "Kitabın dil işçiliği muazzam. Her satırda dantel gibi işlenmiş bir Türkçe var.",
                18,
                false,
                "Üslup"
            )
        )
    }

    // 9 Sample Events with Cover Images
    val events = remember {
        mutableStateListOf(
            ClubEvent(
                "e1",
                "Mücella: Karakter Analizi ve İlk Bölümler",
                "20",
                "Haz",
                "20 Haziran Cumartesi, 20:00",
                "Zoom (Görüntülü)",
                "Romanın ilk 150 sayfasını ve karakterlerin psikolojik yapısını tartışıyoruz.",
                "https://cdn.timas.com.tr/urun/mucella-9786050820416.jpg"
            ),
            ClubEvent(
                "e2",
                "Nazan Bekiroğlu ile Özel Söyleşi",
                "27",
                "Haz",
                "27 Haziran Cumartesi, 21:00",
                "Canlı Yayın (SuperApp Live)",
                "Değerli yazarımız ile Mücella'nın yazım yolculuğu ve edebi sembolleri.",
                "https://cdn.timas.com.tr/urun/nar-agaci-9786050807073.jpg"
            ),
            ClubEvent(
                "e3",
                "Temmuz Ayı Kitap Seçimi Toplantısı",
                "04",
                "Tem",
                "04 Temmuz Cumartesi, 19:30",
                "Zoom (Görüntülü)",
                "Gelecek ay okuyacağımız eseri adaylar arasından tartışarak belirliyoruz.",
                "https://cdn.timas.com.tr/urun/kehribar-gecidi-9786050843101.jpg"
            ),
            ClubEvent(
                "e4",
                "Dirilt Kalbini: Modern Dünyada İnanç",
                "11",
                "Tem",
                "11 Temmuz Cumartesi, 20:00",
                "Zoom (Görüntülü)",
                "Dirilt Kalbini kitabı ışığında günlük hayatta manevi odaklanmayı konuşuyoruz.",
                "https://cdn.timas.com.tr/urun/dirilt-kalbini-9786050825992.jpg"
            ),
            ClubEvent(
                "e5",
                "Malamander: Fantastik Kurgu İncelemesi",
                "18",
                "Tem",
                "18 Temmuz Cumartesi, 18:30",
                "Zoom (Görüntülü)",
                "Malamander romanının dünya inşası ve çocuk edebiyatındaki yerini analiz ediyoruz.",
                "https://cdn.timas.com.tr/urun/malamander-9786050833775.jpg"
            ),
            ClubEvent(
                "e6",
                "Kalpsizler: Distopik Gelecek Tartışması",
                "25",
                "Tem",
                "25 Temmuz Cumartesi, 20:00",
                "Zoom (Görüntülü)",
                "Duygulardan arındırılmış bir distopyada birey olma mücadelesini ele alıyoruz.",
                "https://cdn.timas.com.tr/urun/kalpsizler-9786050847642.jpg"
            ),
            ClubEvent(
                "e7",
                "Bırak ve Rahatla: Zihinsel Arınma Seansı",
                "01",
                "Ağu",
                "01 Ağustos Cumartesi, 21:00",
                "Canlı Yayın (SuperApp Live)",
                "Stres yönetimi ve zihinsel rahatlama teknikleri üzerine pratik çalışmalar.",
                "https://cdn.timas.com.tr/urun/birak-ve-rahatla-9786050848038.jpg"
            ),
            ClubEvent(
                "e8",
                "Güvenli Bağlanma: Ebeveyn-Çocuk İletişimi",
                "08",
                "Ağu",
                "08 Ağustos Cumartesi, 19:30",
                "Zoom (Görüntülü)",
                "Çocuklarda güvenli bağlanma ilkeleri ve ebeveyn tutumlarını değerlendiriyoruz.",
                "https://cdn.timas.com.tr/urun/guvenli-baglanma-9786050815061.jpg"
            ),
            ClubEvent(
                "e9",
                "Mimoza Sürgünü: Tarihsel Anlatı Kritik",
                "15",
                "Ağu",
                "15 Ağustos Cumartesi, 20:00",
                "Zoom (Görüntülü)",
                "Mimoza Sürgünü eserinin edebi derinliğini ve tarihi gerçekliklerle bağını tartışıyoruz.",
                "https://cdn.timas.com.tr/urun/mimoza-surgunu-9786050812350.jpg"
            )
        )
    }

    // 2. Interactive States
    var newCommentText by remember { mutableStateOf("") }
    var selectedTag by remember { mutableStateOf("Tümü") } // Discussion Board Tag filter
    var votedBookIndex by remember { mutableStateOf(-1) } // Voting Poll state
    var votes by remember { mutableStateOf(listOf(42, 38, 20)) } // Voting Poll tally
    var selectedMemberProfile by remember { mutableStateOf<ClubMember?>(null) } // Member detail popup state
    var showBookDetailDialog by remember { mutableStateOf(false) } // Book details dialog state
    var selectedEventDetail by remember { mutableStateOf<ClubEvent?>(null) } // Event details popup state
    var isJoinedMonthSelection by remember { mutableStateOf(false) } // Join common reading state

    // Countdown Timer to Next Meeting (Simulated)
    var timeLeftSeconds by remember { mutableStateOf(48500L) } // ~13.5 hours
    LaunchedEffect(Unit) {
        while (timeLeftSeconds > 0) {
            delay(1000)
            timeLeftSeconds--
        }
    }

    val hours = timeLeftSeconds / 3600
    val minutes = (timeLeftSeconds % 3600) / 60
    val seconds = timeLeftSeconds % 60
    val countdownText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    // --- Interactive Member Profile Dialog ---
    selectedMemberProfile?.let { member ->
        AlertDialog(
            onDismissRequest = { selectedMemberProfile = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .border(2.dp, Orange, CircleShape)
                    ) {
                        SubcomposeAsyncImage(
                            model = member.avatarUrl,
                            contentDescription = member.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column {
                        Text(member.name, fontWeight = FontWeight.Bold, color = TextMain, fontSize = 18.sp)
                        Text(member.role, fontSize = 11.sp, color = Orange, fontWeight = FontWeight.Bold)
                    }
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Kozada Tamamlanan:", fontSize = 12.sp, color = TextMuted)
                        Text("${member.booksFinished} Kitap", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMain)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Şu An Ne Okuyor?", fontSize = 12.sp, color = TextMuted)
                        Text(member.currentBook, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Orange)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Aktiflik Durumu:", fontSize = 12.sp, color = TextMuted)
                        Text(
                            if (member.isOnline) "Aktif (Kozada)" else "Çevrimdışı",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (member.isOnline) Color(0xFF27AE60) else TextMuted
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedMemberProfile = null }) {
                    Text("Kapat", color = Orange, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = CardBg
        )
    }

    // --- Bu Ayın Seçimi (Book Detail Dialog with RSVP Option) ---
    if (showBookDetailDialog) {
        AlertDialog(
            onDismissRequest = { showBookDetailDialog = false },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .size(110.dp, 165.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .shadow(4.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = "https://cdn.timas.com.tr/urun/mucella-9786050820416.jpg",
                            contentDescription = "Mücella",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Mücella",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Nazan Bekiroğlu",
                        fontSize = 13.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Orange.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "AYIN ORTAK OKUMASI • 280 SAYFA",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Orange
                        )
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Mücella, Nazan Bekiroğlu'nun Osmanlı'nın son döneminden Cumhuriyet'e geçiş sancılarını tek bir hayat üzerinden dantel gibi işlediği başyapıtıdır. Roman, hayata sessizce tanıklık eden, kendi hikayesini yazmaktansa başkalarının hikayesini izleyen Mücella'nın büyüleyici ve hüzünlü öyküsünü anlatır. Roman boyunca Türkiye'nin toplumsal dönüşümünü de karakterler eşliğinde derinlemesine gözlemliyoruz.",
                        fontSize = 11.sp,
                        color = TextMain,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            isJoinedMonthSelection = !isJoinedMonthSelection
                            if (isJoinedMonthSelection) {
                                Toast.makeText(context, "Ortak okumaya katılımınız onaylandı! Takviminize eklendi.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isJoinedMonthSelection) Color(0xFF27AE60) else Orange
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isJoinedMonthSelection) Icons.Default.CheckCircle else Icons.Default.GroupAdd,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isJoinedMonthSelection) "Ortak Okumaya Katılıyorsunuz" else "Ortak Okumaya Katıl",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    TextButton(
                        onClick = { showBookDetailDialog = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Kapat", color = TextMuted, fontWeight = FontWeight.Bold)
                    }
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = CardBg
        )
    }

    // --- Aylık Oturum ve Etkinlikler (Event Detail & RSVP Dialog) ---
    selectedEventDetail?.let { event ->
        val originalIndex = events.indexOfFirst { it.id == event.id }
        AlertDialog(
            onDismissRequest = { selectedEventDetail = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Orange.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(event.dateDay, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Orange)
                            Text(event.dateMonth.uppercase(), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Orange)
                        }
                    }
                    Column {
                        Text(event.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextMain)
                        Text(event.dateFull, fontSize = 9.sp, color = TextMuted)
                    }
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    // Event Cover Image inside Popup
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        SubcomposeAsyncImage(
                            model = event.coverUrl,
                            contentDescription = event.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Text(
                        text = event.description,
                        fontSize = 11.sp,
                        color = TextMain,
                        lineHeight = 16.sp
                    )
                    
                    HorizontalDivider(color = BorderClr)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (event.platform.contains("Zoom")) Icons.Default.Videocam else Icons.Default.LiveTv,
                            contentDescription = null,
                            tint = if (event.platform.contains("Zoom")) Color(0xFF27AE60) else Orange,
                            modifier = Modifier.size(16.dp)
                        )
                        Column {
                            Text("Platform ve Katılım Detayı:", fontSize = 10.sp, color = TextMuted)
                            Text(
                                text = if (event.platform.contains("Zoom")) "${event.platform} (zoom.us/j/timas-koza)" else event.platform,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (event.platform.contains("Zoom")) Color(0xFF27AE60) else Orange
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (originalIndex != -1) {
                                val updatedRSVP = !event.isRSVPed
                                events[originalIndex] = event.copy(isRSVPed = updatedRSVP)
                                selectedEventDetail = events[originalIndex] // update dialog state
                                if (updatedRSVP) {
                                    Toast.makeText(context, "${event.title} oturumuna katılımınız onaylandı!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (event.isRSVPed) Color(0xFF27AE60) else Orange
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (event.isRSVPed) Icons.Default.CheckCircle else Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (event.isRSVPed) "Oturuma Katılıyorsunuz" else "Oturuma Katılacağım",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                TextButton(
                    onClick = { selectedEventDetail = null },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Kapat", color = TextMuted, fontWeight = FontWeight.Bold)
                }
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = CardBg
    )
}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Okuma Kulübü", 
                        color = DarkSlate, 
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Geri", 
                            tint = Orange
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Okuma Kulübü kuralları ve detaylar açılıyor...", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Bilgi", tint = DarkSlate.copy(alpha = 0.8f))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgColor)
            )
        },
        containerColor = BgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            // --- 1. HERO HEADER: WELCOME (No Reading Challenge) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(LightOrange, BgColor)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Timaş Kozası",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextMain,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Birlikte Okuyor, Paylaşıyor ve Büyüyoruz",
                            fontSize = 11.sp,
                            color = Orange,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Member Count Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Orange.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Groups, contentDescription = null, tint = Orange, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "1.240 Üye",
                                color = Orange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // --- 2. LITERARY QUOTE OF THE DAY BANNER ---
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LightOrange.copy(alpha = 0.4f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Orange.copy(alpha = 0.15f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = Orange,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "\"Bir kitap okumak, başka bir dünyada yaşamaktır.\"",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Medium
                            ),
                            color = TextMain
                        )
                        Text(
                            text = "— Günün Sözü",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Orange
                        )
                    }
                }
            }

            // --- 3. AYIN SEÇİMİ (Current Reading Selection Overview - Clickable for details) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "Bu Ayın Seçimi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderClr),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                        .clickable { showBookDetailDialog = true } // Opens detail dialog
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Book Cover
                            Box(
                                modifier = Modifier
                                    .size(90.dp, 135.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .shadow(2.dp)
                            ) {
                                SubcomposeAsyncImage(
                                    model = "https://cdn.timas.com.tr/urun/mucella-9786050820416.jpg",
                                    contentDescription = "Mücella",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    loading = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Orange.copy(alpha = 0.1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = Orange)
                                        }
                                    }
                                )
                            }

                            // Book Info
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Orange.copy(alpha = 0.12f))
                                            .padding(horizontal = 6.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "ORTAK OKUMA",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Orange
                                        )
                                    }
                                    
                                    // RSVP Badge showing on card too
                                    if (isJoinedMonthSelection) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFF27AE60).copy(alpha = 0.12f))
                                                .padding(horizontal = 6.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "Katılıyorsunuz",
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF27AE60)
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Info, 
                                            contentDescription = "Detay", 
                                            tint = Orange, 
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Mücella",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                                Text(
                                    text = "Yazar: Nazan Bekiroğlu",
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Mücella, Nazan Bekiroğlu'nun Osmanlı'nın son döneminden Cumhuriyet'e geçiş sancılarını tek bir hayat üzerinden dantel gibi işlediği başyapıtıdır. Katılmak ve detaylar için dokunun...",
                                    fontSize = 10.sp,
                                    color = TextMuted,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            // --- 4. AYIN ETKİNLİK TAKVİMİ (Horizontally Scrollable LazyRow of 9 Event Cards) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Aylık Oturum ve Etkinlikler",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        fontFamily = FontFamily.Serif
                    )

                    // Nearest event countdown badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Orange.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Geri Sayım: $countdownText",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Orange,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(events) { event ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderClr),
                            modifier = Modifier
                                .width(185.dp) // Fixed width for horizontal scrolling
                                .shadow(2.dp, RoundedCornerShape(16.dp))
                                .clickable { selectedEventDetail = event } // Opens event detail dialog with RSVP
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Event Cover Image
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(115.dp)
                                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                ) {
                                    SubcomposeAsyncImage(
                                        model = event.coverUrl,
                                        contentDescription = event.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    
                                    // Overlaid Date badge
                                    Box(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Orange.copy(alpha = 0.9f))
                                            .padding(horizontal = 6.dp, vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${event.dateDay} ${event.dateMonth.uppercase()}",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }

                                    // Overlaid RSVP active checkmark
                                    if (event.isRSVPed) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(8.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF27AE60))
                                                .padding(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Katılıyorsunuz",
                                                tint = Color.White,
                                                modifier = Modifier.size(10.dp)
                                            )
                                        }
                                    }
                                }

                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = event.title,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMain,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = event.dateFull,
                                        fontSize = 8.sp,
                                        color = TextMuted,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (event.platform.contains("Zoom")) Icons.Default.Videocam else Icons.Default.LiveTv,
                                            contentDescription = null,
                                            tint = if (event.platform.contains("Zoom")) Color(0xFF27AE60) else Orange,
                                            modifier = Modifier.size(10.dp)
                                        )
                                        Text(
                                            text = event.platform, 
                                            fontSize = 8.sp, 
                                            color = if (event.platform.contains("Zoom")) Color(0xFF27AE60) else Orange, 
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- 5. FUTURE READING VOTE POLL ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Gelecek Ay Ne Okuyalım?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderClr),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Temmuz 2026 oturumu için okunacak kitabı birlikte seçelim. Tercihiniz için oy verin:",
                            fontSize = 11.sp,
                            color = TextMuted,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        val candidateBooks = listOf(
                            "Kehribar Geçidi" to "Nazan Bekiroğlu",
                            "İçimdeki Müzik" to "Sharon M. Draper",
                            "Nar Ağacı" to "Nazan Bekiroğlu"
                        )

                        candidateBooks.forEachIndexed { index, (title, author) ->
                            val totalVotes = votes.sum()
                            val percentage = if (totalVotes > 0) (votes[index] * 100) / totalVotes else 0
                            val isSelected = votedBookIndex == index

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (votedBookIndex == -1) {
                                            votedBookIndex = index
                                            val updatedVotes = votes.toMutableList()
                                            updatedVotes[index] += 1
                                            votes = updatedVotes
                                            Toast.makeText(context, "$title kitabına oyunuz kaydedildi!", Toast.LENGTH_SHORT).show()
                                        } else if (votedBookIndex == index) {
                                            votedBookIndex = -1
                                            val updatedVotes = votes.toMutableList()
                                            updatedVotes[index] -= 1
                                            votes = updatedVotes
                                        } else {
                                            val updatedVotes = votes.toMutableList()
                                            updatedVotes[votedBookIndex] -= 1
                                            updatedVotes[index] += 1
                                            votes = updatedVotes
                                            votedBookIndex = index
                                            Toast.makeText(context, "Oyunuz $title olarak güncellendi!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .padding(vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = {
                                                if (votedBookIndex == -1) {
                                                    votedBookIndex = index
                                                    val updatedVotes = votes.toMutableList()
                                                    updatedVotes[index] += 1
                                                    votes = updatedVotes
                                                    Toast.makeText(context, "$title kitabına oyunuz kaydedildi!", Toast.LENGTH_SHORT).show()
                                                } else if (votedBookIndex == index) {
                                                    votedBookIndex = -1
                                                    val updatedVotes = votes.toMutableList()
                                                    updatedVotes[index] -= 1
                                                    votes = updatedVotes
                                                } else {
                                                    val updatedVotes = votes.toMutableList()
                                                    updatedVotes[votedBookIndex] -= 1
                                                    updatedVotes[index] += 1
                                                    votes = updatedVotes
                                                    votedBookIndex = index
                                                    Toast.makeText(context, "Oyunuz $title olarak güncellendi!", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            colors = RadioButtonDefaults.colors(selectedColor = Orange)
                                        )
                                        Column {
                                            Text(
                                                text = title,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextMain
                                            )
                                            Text(
                                                text = author,
                                                fontSize = 9.sp,
                                                color = TextMuted
                                            )
                                        }
                                    }
                                    Text(
                                        text = "%$percentage (${votes[index]} oy)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Orange else TextMuted
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                LinearProgressIndicator(
                                    progress = { percentage.toFloat() / 100f },
                                    color = if (isSelected) Orange else TextMuted,
                                    trackColor = BorderClr,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }
                    }
                }
            }

            // --- 6. HIGH-FIDELITY ACTIVE READINGS COCOON (Kozadaki Okurlar) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kozadaki Okurlar",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Detay için dokunun",
                        fontSize = 10.sp,
                        color = Orange,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(members) { member ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(74.dp)
                                .clickable { selectedMemberProfile = member }
                        ) {
                            // Avatar inside styled Instagram-story gradient active ring
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (member.isOnline) {
                                            Brush.linearGradient(listOf(Orange, Color(0xFFFF9F43)))
                                        } else {
                                            Brush.linearGradient(listOf(BorderClr, BorderClr))
                                        }
                                    )
                                    .padding(2.5.dp) // Ring thickness gap
                                    .clip(CircleShape)
                                    .background(CardBg)
                                    .padding(2.dp) // Outer avatar spacing
                                    .clip(CircleShape)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    SubcomposeAsyncImage(
                                        model = member.avatarUrl,
                                        contentDescription = member.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    // Small online status capsule dot on bottom right
                                    if (member.isOnline) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF27AE60))
                                                .border(2.dp, CardBg, CircleShape)
                                                .align(Alignment.BottomEnd)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            
                            Text(
                                text = member.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            // Tiny custom capsule display underneath reader's name
                            Box(
                                modifier = Modifier
                                    .padding(top = 3.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (member.role == "Moderatör") Orange.copy(alpha = 0.12f) 
                                        else BorderClr.copy(alpha = 0.5f)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (member.role == "Moderatör") "Moderatör" else member.currentBook,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (member.role == "Moderatör") Orange else TextMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // --- 7. INTERACTIVE DISCUSSION BOARD (FORUM) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tartışma Panosu",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        fontFamily = FontFamily.Serif
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Sohbet panosu yenilendi.", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Orange, modifier = Modifier.size(14.dp))
                        Text(text = "Yenile", fontSize = 11.sp, color = Orange, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Topic filtering pills
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    val tags = listOf("Tümü", "Karakterler", "Üslup", "Sembolizm")
                    items(tags) { tag ->
                        val isSelected = selectedTag == tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Orange else LightOrange.copy(alpha = 0.5f))
                                .clickable { selectedTag = tag }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tag,
                                color = if (isSelected) Color.White else Orange,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Post Comment Box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderClr),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // User Avatar (Mock)
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .border(1.dp, BorderClr, CircleShape)
                        ) {
                            SubcomposeAsyncImage(
                                model = "https://picsum.photos/id/1025/100/100",
                                contentDescription = "Ben",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Text Field Input
                        OutlinedTextField(
                            value = newCommentText,
                            onValueChange = { newCommentText = it },
                            placeholder = { Text("Mücella hakkında bir şeyler yaz...", color = TextMuted, fontSize = 12.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 40.dp),
                            maxLines = 3,
                            singleLine = false,
                            shape = RoundedCornerShape(20.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = {
                                if (newCommentText.isNotBlank()) {
                                    val postTag = if (selectedTag == "Tümü") "Genel" else selectedTag
                                    discussionPosts.add(
                                        0,
                                        DiscussionPost(
                                            "Siz (Kullanıcı)",
                                            "https://picsum.photos/id/1025/100/100",
                                            "Şimdi",
                                            newCommentText,
                                            0,
                                            false,
                                            tag = postTag
                                        )
                                    )
                                    newCommentText = ""
                                    Toast.makeText(context, "Düşünceniz paylaşıldı!", Toast.LENGTH_SHORT).show()
                                }
                            }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextMain,
                                unfocusedTextColor = TextMain,
                                focusedContainerColor = BgColor,
                                unfocusedContainerColor = BgColor,
                                focusedBorderColor = Orange,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )

                        // Send Button
                        IconButton(
                            onClick = {
                                if (newCommentText.isNotBlank()) {
                                    val postTag = if (selectedTag == "Tümü") "Genel" else selectedTag
                                    discussionPosts.add(
                                        0,
                                        DiscussionPost(
                                            "Siz (Kullanıcı)",
                                            "https://picsum.photos/id/1025/100/100",
                                            "Şimdi",
                                            newCommentText,
                                            0,
                                            false,
                                            tag = postTag
                                        )
                                    )
                                    newCommentText = ""
                                    Toast.makeText(context, "Düşünceniz paylaşıldı!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = newCommentText.isNotBlank(),
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    if (newCommentText.isNotBlank()) Orange else BgColor,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Gönder",
                                tint = if (newCommentText.isNotBlank()) Color.White else TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // List of Discussion Posts (Filtered dynamically by chosen tag pill)
                val filteredPosts = remember(selectedTag, discussionPosts.size, discussionPosts.map { it.isLikedByUser }) {
                    if (selectedTag == "Tümü") {
                        discussionPosts
                    } else {
                        discussionPosts.filter { it.tag == selectedTag }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (filteredPosts.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Bu başlıkta henüz düşünce paylaşılmamış.",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    } else {
                        filteredPosts.forEach { post ->
                            val originalIndex = discussionPosts.indexOf(post)
                            if (originalIndex != -1) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBg),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, BorderClr),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(1.dp, RoundedCornerShape(16.dp))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Author avatar
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(CircleShape)
                                                    .border(1.dp, BorderClr, CircleShape)
                                            ) {
                                                SubcomposeAsyncImage(
                                                    model = post.authorAvatarUrl,
                                                    contentDescription = post.authorName,
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }

                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Text(
                                                        text = post.authorName,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = TextMain
                                                    )
                                                    // Display Category Tag inside the post
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(Orange.copy(alpha = 0.08f))
                                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                                    ) {
                                                        Text(
                                                            text = post.tag,
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Orange
                                                        )
                                                    }
                                                }
                                                Text(
                                                    text = post.timeAgo,
                                                    fontSize = 8.sp,
                                                    color = TextMuted
                                                )
                                            }

                                            // Like indicator button
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(
                                                        if (post.isLikedByUser) Orange.copy(alpha = 0.12f) else Color.Transparent
                                                    )
                                                    .clickable {
                                                        val isLiked = !post.isLikedByUser
                                                        val likesDiff = if (isLiked) 1 else -1
                                                        discussionPosts[originalIndex] = post.copy(
                                                            likesCount = post.likesCount + likesDiff,
                                                            isLikedByUser = isLiked
                                                        )
                                                    }
                                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (post.isLikedByUser) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                    contentDescription = null,
                                                    tint = if (post.isLikedByUser) Orange else TextMuted,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Text(
                                                    text = "${post.likesCount}",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (post.isLikedByUser) Orange else TextMuted
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = post.content,
                                            fontSize = 11.sp,
                                            color = TextMain,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- 8. PAST READINGS SHELF ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Önceki Okuduklarımız",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 10.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(pastReadings) { past ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderClr),
                            modifier = Modifier
                                .width(120.dp)
                                .clickable {
                                    Toast.makeText(context, "${past.title} kitabı tartışma arşivi açılıyor...", Toast.LENGTH_SHORT).show()
                                }
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .shadow(1.dp)
                                ) {
                                    SubcomposeAsyncImage(
                                        model = past.coverUrl,
                                        contentDescription = past.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = past.title,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = past.author,
                                    fontSize = 8.sp,
                                    color = TextMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Orange,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Text(
                                        text = "${past.rating}",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMain
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = past.completionDate,
                                        fontSize = 7.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
