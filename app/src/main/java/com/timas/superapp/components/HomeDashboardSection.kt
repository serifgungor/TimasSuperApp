package com.timas.superapp.components

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class DashboardBook(val title: String, val author: String, val coverUrl: String, val description: String = "")
data class DashboardEvent(
    val title: String, 
    val date: String, 
    val icon: ImageVector, 
    val iconTint: Color,
    val location: String = "İstanbul Kongre Merkezi",
    val description: String = "Etkinliğimize katılarak benzersiz bir deneyim yaşayabilirsiniz. Detaylı bilgi için kayıt olun."
)

@Composable
fun HomeDashboardSection(onKatilClick: (String) -> Unit = {}) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val bookCardHeight = if (isTablet) 325.dp else 255.dp

    val books = listOf(
        DashboardBook(
            "Mutluluğun İnşası",
            "Mecit Ömür Öztürk",
            "https://cdn.timas.com.tr/urun/mutlulugun-insasi-9786050849745.jpg",
            "İnsanın dünyada iki temel görevi vardır: Bunlardan biri iç dünyasını düzene sokmak, diğeri tabiatı şekillendirmektir. Mecit Ömür Öztürk, Mutluluğun İnşası’nda içimizdeki mutluluğun temellerini nasıl sağlam bir şekilde atabileceğimizi sıcak ve samimi bir dille aktarıyor."
        ),
        DashboardBook(
            "Keşke Dememek İçin",
            "Yasin Pişgin",
            "https://cdn.timas.com.tr/urun/keske-dememek-icin-9786259621555.jpg",
            "Bu kitapta incelenen ayetlerde; ölümden sonra dirilenler, hesaba girip çıkanlar ve yaşadıklarına bin pişman olanlar konuşuyorlar. Ve bize günahlarını, pişmanlıklarını ve keşkelerini anlatarak adeta dirilere nasihat ediyorlar."
        ),
        DashboardBook(
            "Dilin Afetleri",
            "İmam Gazzâlî",
            "https://cdn.timas.com.tr/urun/dilin-afetleri-9786259445182.jpg",
            "Dil, Allah’ın (cc) insana bahşettiği en büyük nimetlerden biridir. Büyük İslam düşünürü İmam Gazzâlî’nin çağları aşan meşhur eseri İhya’nın Mühlikat kısmında yer alan Dilin Afetleri, konuşmanın sınırlarını ve susmanın erdemini akıcı ve rahat anlaşılır bir üslupla ele alıyor."
        ),
        DashboardBook(
            "Kur'an Atlası",
            "Yusuf Gündüz",
            "https://cdn.timas.com.tr/urun/kuran-atlasi-9786256360525.jpg",
            "Kur'an-ı Kerim, dünyanın kullanım kılavuzu. Kur'an Atlası, bize bu önemli kılavuzu anlamakta yardımcı olacak. Başta Sevgili Peygamberimiz Hz. Muhammed olmak üzere Kur'an'da adı geçen peygamberlerin hayatlarını, coğrafi mekanları ve tarihi olayları bu eşsiz kitapta okuyacaksınız."
        ),
        DashboardBook(
            "Kitabın Yolcuları",
            "Olga Tokarczuk",
            "https://cdn.timas.com.tr/urun/kitapin-yolculari-9786050849776.jpg",
            "Fransa, 1685. Bir grup Protestan Huguenot, yeni bir vatan bulma umuduyla ülkenin diğer ucuna ulaşmak üzere yola çıkar. Zaman zaman maceraya dönüşen bu yolculuk, yalnızca onların kaderini değil, tüm insanlığın tarihini değiştirebilir."
        ),
        DashboardBook(
            "Malma İstasyonu",
            "Alex Schulman",
            "https://cdn.timas.com.tr/urun/malma-istasyonu-9786050848533.jpg",
            "Bir tren, enfes bir yaz manzarasında hızla ilerliyor. Harriet, Oskar ve Yana –her biri, noktaları birleştirmeye çalışırken bir önceki zamanın izlerini taşıyor. Kuşaklar üzerinden yalnızlıklar, travmalar ve tamir edilemez ilişkilerin dağıttığı bir ailenin hikâyesi."
        ),
        DashboardBook(
            "İçimdeki Hayal",
            "Sharon M. Draper",
            "https://cdn.timas.com.tr/urun/icimdeki-hayal-9786259727455.jpg",
            "Melody konuşma engeline rağmen içindeki zengin dünyayı yansıtmaya devam ediyor. 'İçimdeki Müzik'in devam romanında Melody'nin yeni hayalleri ve hayat dolu mücadelesi."
        ),
        DashboardBook(
            "İyilik Timi",
            "Metin Özdamarlar",
            "https://cdn.timas.com.tr/urun/iyilik-timi-9786259834658.jpg",
            "İyilik Timi’yle beraber heyecanlı maceralar yaşamaya hazır mısın? Çikolata makinesi yapımı, Kuru Fasulye Şenliği, Tüm mahalleye dondurma dağıtmaca ve daha nicesi. Bu kitap seni kalbinden vuracak maceralara sürükleyecek!"
        ),
        DashboardBook(
            "Uçan Anne Terliği",
            "Anıl Basılı",
            "https://cdn.timas.com.tr/urun/ucan-anne-terligi-9786255978714.jpg",
            "Nerede birlik orada dirlik! Aile içi yardımlaşma, dostluk ve dayanışmanın eğlenceli ve sıcacık hikayesini anlatan Anıl Basılı imzalı neşeli bir macera."
        ),
        DashboardBook(
            "Alyanak Projesi / Nuriye Ecmel",
            "Merve Özcan",
            "https://cdn.timas.com.tr/urun/alyanak-projesi-nuriye-ecmel-9786259802008.jpg",
            "Ben Nuriye Ecmel... Bu iyilik macerasının adı da 'Alyanak Projesi'. İyilik macerasında neler mi oluyor? Alyanak kim mi? Rahat olun ve arkanıza yaslanarak bu iyilik serüveninin tadını çıkarın."
        ),
        DashboardBook(
            "Kalpsizler",
            "Marissa Meyer",
            "https://cdn.timas.com.tr/urun/kalpsizler-9786050847642.jpg",
            "Harikalar Diyarı'nın en korkulan kraliçesi olmadan önce, Catherine sadece aşık olmak ve pastalar pişirmek isteyen bir genç kızdı..."
        ),
        DashboardBook(
            "Tavuk Bacaklı Ev Kaçıyor",
            "Sophie Anderson",
            "https://cdn.timas.com.tr/urun/tavuk-bacakli-ev-kaciyor-9786259232645.jpg",
            "Marinka kendini bildi bileli tavuk bacakları olan bir evde yaşadı. Evleri sürekli yer değiştirir, onlara hayal edilemez dünyalar sunardı..."
        ),
        DashboardBook(
            "Ağaçların Fısıltısı",
            "Murat Moroğlu",
            "https://cdn.timas.com.tr/urun/agaclarin-fisiltisi-9786258618112.jpg",
            "Çorak topraklardan yemyeşil masal diyarına uzanan bir doğa yolculuğu… Bir çocuğun azmi dünyayı nasıl yeşile boyar?"
        )
    )

    val events = listOf(
        DashboardEvent("Yazarlarla Buluşma", "15 Haziran", Icons.Default.Groups, Color(0xFFF26122)),
        DashboardEvent("Okuma Kulübü", "Her Cumartesi", Icons.AutoMirrored.Filled.MenuBook, Color(0xFF4AC2E3)),
        DashboardEvent("Şiir Dinletisi", "21 Mart", Icons.Default.Mic, Color(0xFF9C27B0)),
        DashboardEvent("İstanbul Kitap Fuarı", "1-3 Kasım", Icons.Default.Event, Color(0xFF4AC2E3)),
        DashboardEvent("Çocuk Şenliği", "23 Nisan", Icons.Default.ChildCare, Color(0xFFF26122)),
        DashboardEvent("Kitap İmza Günü", "10 Mayıs", Icons.Default.Edit, Color(0xFF4CAF50))
    )

    if (isTablet) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(modifier = Modifier.weight(1.4f)) { YeniGelenler(books) }
            Box(modifier = Modifier.weight(1f)) { 
                EtkinlikTakvimi(
                    events = events, 
                    onKatilClick = onKatilClick,
                    listModifier = Modifier
                        .height(bookCardHeight)
                        .verticalScroll(rememberScrollState())
                ) 
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) { YeniGelenler(books) }
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.padding(horizontal = 16.dp)) { 
                EtkinlikTakvimi(
                    events = events, 
                    onKatilClick = onKatilClick,
                    listModifier = Modifier
                        .height(bookCardHeight)
                        .verticalScroll(rememberScrollState())
                ) 
            }
        }
    }
}

@Composable
fun YeniGelenler(books: List<DashboardBook>) {
    var selectedBook by remember { mutableStateOf<DashboardBook?>(null) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Yeni Gelenler", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 16.sp)
            Text("Tümünü Gör", fontWeight = FontWeight.SemiBold, color = Color(0xFFF26122), fontSize = 14.sp, modifier = Modifier.clickable {  })
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(books) { book ->
                BookCard(book = book, onInceleClick = { selectedBook = book })
            }
        }
    }

    selectedBook?.let { book ->
        BookDetailDialog(book = book, onDismiss = { selectedBook = null })
    }
}

@Composable
fun BookCard(book: DashboardBook, onInceleClick: () -> Unit) {
    val isTablet = LocalConfiguration.current.screenWidthDp >= 600
    val cardWidth = if (isTablet) 170.dp else 130.dp
    val cardHeight = if (isTablet) 325.dp else 255.dp
    val imageHeight = if (isTablet) 245.dp else 190.dp
    val titleFontSize = if (isTablet) 14.sp else 12.sp
    val authorFontSize = if (isTablet) 12.sp else 10.sp

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .clickable { onInceleClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                    .data(book.coverUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color.White)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(book.title, fontWeight = FontWeight.Bold, fontSize = titleFontSize, color = Color(0xFF1E293B), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(2.dp))
                Text(book.author, fontSize = authorFontSize, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

// Helper to parse dates like "15 Haziran" or "Her Cumartesi" into Day and Month parts
fun parseEventDate(dateStr: String): Pair<String, String> {
    val parts = dateStr.split(" ")
    return if (parts.size >= 2) {
        val day = parts[0]
        val month = parts[1].take(3).uppercase()
        Pair(day, month)
    } else {
        // Fallback for strings without spaces
        val clean = dateStr.take(3)
        Pair(clean, "ETK")
    }
}

@Composable
fun EtkinlikTakvimi(events: List<DashboardEvent>, onKatilClick: (String) -> Unit, listModifier: Modifier = Modifier) {
    var selectedEvent by remember { mutableStateOf<DashboardEvent?>(null) }
    var eventToConfirm by remember { mutableStateOf<DashboardEvent?>(null) }

    Column {
        Text("Etkinlik Takvimi", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))
        
        // Show events in a beautiful vertical timeline list
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = listModifier
        ) {
            events.forEach { event ->
                EventRowCard(
                    event = event, 
                    onKatilClick = { eventToConfirm = event }, 
                    onDetayClick = { selectedEvent = event }
                )
            }
        }
    }

    selectedEvent?.let { event ->
        EventDetailDialog(
            event = event,
            onDismiss = { selectedEvent = null },
            onKatilClick = { 
                selectedEvent = null
                eventToConfirm = event
            }
        )
    }

    if (eventToConfirm != null) {
        AlertDialog(
            onDismissRequest = { eventToConfirm = null },
            title = {
                Text(
                    text = "Katılım Onayı",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            },
            text = {
                Text(
                    text = "${eventToConfirm!!.title} etkinliğine katılmak istediğinizden emin misiniz?",
                    color = Color(0xFF64748B)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onKatilClick(eventToConfirm!!.title)
                        eventToConfirm = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF26122))
                ) {
                    Text("Evet, Katıl", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { eventToConfirm = null }) {
                    Text("Vazgeç", color = Color(0xFF64748B))
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun EventRowCard(
    event: DashboardEvent, 
    onKatilClick: () -> Unit, 
    onDetayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (day, month) = remember(event.date) { parseEventDate(event.date) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDetayClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = borderStroke(Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Date Badge
            Box(
                modifier = Modifier
                    .width(62.dp)
                    .height(58.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF0EB))
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = day,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = Color(0xFFF26122),
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = month,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = Color(0xFFF26122).copy(alpha = 0.8f),
                        lineHeight = 10.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 2. Info details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF1E293B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = event.icon,
                        contentDescription = null,
                        tint = event.iconTint,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = event.location,
                        fontSize = 10.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• 120+ Katılımcı",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF27AE60)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // 3. Actions Column
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Detay",
                    color = Color(0xFF4AC2E3),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onDetayClick() }
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF26122))
                        .clickable { onKatilClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Katıl",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun borderStroke(color: Color) = androidx.compose.foundation.BorderStroke(1.dp, color)

@Composable
fun BookDetailDialog(book: DashboardBook, onDismiss: () -> Unit) {
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val dialogWidth = if (configuration.screenWidthDp >= 600) 0.5f else 0.95f
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(dialogWidth)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            com.timas.superapp.screens.BookDetailScreen(
                book = com.timas.superapp.Book(
                    title = book.title,
                    author = book.author,
                    coverUrl = book.coverUrl,
                    description = book.description,
                    isOwned = true
                ),
                onBack = onDismiss,
                onStartReading = { onDismiss() },
                onStartListening = { onDismiss() },
                isLightMode = true
            )
        }
    }
}

@Composable
fun EventDetailDialog(event: DashboardEvent, onDismiss: () -> Unit, onKatilClick: (String) -> Unit) {
    val hostName = when {
        event.title.contains("Yazarlar") -> "Nazan Bekiroğlu"
        event.title.contains("Okuma Kulübü") -> "Zeynep Koç"
        event.title.contains("Şiir") -> "Ahmet Yılmaz"
        else -> "Timaş Yayınları Editör Ekibi"
    }
    
    val hostTitle = when {
        event.title.contains("Yazarlar") -> "Yazar & Akademisyen"
        event.title.contains("Okuma Kulübü") -> "Kulüp Moderatörü"
        event.title.contains("Şiir") -> "Şair & Edebiyatçı"
        else -> "Etkinlik Koordinatörü"
    }

    val hostAvatarUrl = when {
        event.title.contains("Yazarlar") -> "https://picsum.photos/id/1011/100/100"
        event.title.contains("Okuma Kulübü") -> "https://picsum.photos/id/1025/100/100"
        event.title.contains("Şiir") -> "https://picsum.photos/id/1012/100/100"
        else -> "https://picsum.photos/id/1015/100/100"
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header (Ticket Head)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF0E5))
                        .padding(vertical = 24.dp, horizontal = 20.dp)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.05f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Kapat",
                            tint = Color(0xFF1E293B),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = event.icon,
                                contentDescription = null,
                                tint = event.iconTint,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "TİMAŞ KÜLTÜR ETKİNLİĞİ",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF26122),
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        Text(
                            text = event.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF1E293B),
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
                
                // Bottom Details
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 1. Info Cards (Date & Location)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Date Box
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = Color(0xFFF26122),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Tarih & Saat",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF64748B)
                                    )
                                }
                                Text(
                                    text = event.date,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                        }

                        // Location Box
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFFF26122),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Konum",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF64748B)
                                    )
                                }
                                Text(
                                    text = event.location,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1E293B),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // 2. RSVP & Capacity Stats
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF1F5F9).copy(alpha = 0.5f))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF27AE60),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "120+ Kişi Katılıyor",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF27AE60)
                                )
                            }
                            Text(
                                text = "Kapasite: %85 Dolu",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B)
                            )
                        }
                        
                        LinearProgressIndicator(
                            progress = { 0.85f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = Color(0xFFF26122),
                            trackColor = Color(0xFFFFF0EB)
                        )

                        // Attendee Avatars Stack Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            // Render 3 overlapping avatars
                            val colors = listOf(Color(0xFF3498DB), Color(0xFFE74C3C), Color(0xFFF1C40F))
                            val initials = listOf("A", "M", "Z")
                            Box(modifier = Modifier.height(24.dp)) {
                                for (i in 0 until 3) {
                                    Box(
                                        modifier = Modifier
                                            .offset(x = (i * 16).dp)
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(colors[i])
                                            .border(1.5.dp, Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = initials[i],
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(36.dp))
                            Text(
                                text = "ve 117 kişi daha bu etkinliğe katılıyor.",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // 3. Speaker / Host section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFF8F5))
                            .border(1.dp, Color(0xFFFFF0EB), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AsyncImage(
                                model = hostAvatarUrl,
                                contentDescription = hostName,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2E8F0)),
                                contentScale = ContentScale.Crop
                            )
                            Column {
                                Text(
                                    text = "EV SAHİBİ / KONUŞMACI",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF26122),
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = hostName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    text = hostTitle,
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    // 4. Description
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Etkinlik Hakkında",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            fontSize = 14.sp
                        )
                        Text(
                            text = event.description,
                            color = Color(0xFF64748B),
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 5. Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B))
                        ) {
                            Text("Kapat", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onKatilClick(event.title) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF26122)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(2.5f)
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Etkinliğe Katıl",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


