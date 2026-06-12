package com.timas.superapp.components

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
        DashboardBook("Mutluluğun İnşası", "Mecit Ömür Öztürk", "https://cdn.timas.com.tr/urun/mutlulugun-insasi-9786050849745.jpg", "Mutluluğa giden yolda içsel bir rehber."),
        DashboardBook("Dilin Afetleri", "İmam Gazzâlî", "https://cdn.timas.com.tr/urun/dilin-afetleri-9786259445182.jpg", "Dilin tehlikeleri ve korunma yolları."),
        DashboardBook("Kur'an Atlası", "Timaş Yayınları", "https://cdn.timas.com.tr/urun/kuran-atlasi-9786256360525.jpg", "Kur'an-ı Kerim'i anlamaya yardımcı eşsiz bir kaynak."),
        DashboardBook("Kalpsizler", "Marissa Meyer", "https://cdn.timas.com.tr/urun/kalpsizler-9786050847642.jpg", "Modern insanın duygusal yabancılaşmasını anlatan güçlü bir roman."),
        DashboardBook("Politik Bir Beden...", "Timaş Yayınları", "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?auto=format&fit=crop&q=80&w=300", "Siyaset ve insan bedeni üzerine derin bir inceleme."),
        DashboardBook("Tavuk Bacaklı Ev...", "Sophie Anderson", "https://satinal.timas.com.tr/tavuk-bacakli-ev-kaciyor-bilim-kurgu-ve-fantastik-genc-timas-sophie-anderson-37697-14-O.jpg", "Marinka kendini bildi bileli tavuk bacakları olan bir evde yaşadı. O ev sayesinde dünyayı dolaştı ve akla hayale sığmayacak maceralara atıldı. Ne var ki Marinka'nın tavuk bacaklı evi, eski neşesini kaybediyor gibiydi. Marinka, evin neden böyle davrandığını anlamaya çalışırken ev birden koşmaya başladı. Tavuk bacaklı ev kaçıyordu! Marinka ve arkadaşları, evin peşinden koşarlarken kendilerini hangi maceraların beklediğinden habersizlerdi. Tavuk bacaklı ev neden kaçıyordu? Acaba kaçmayı bırakıp Marinka ve arkadaşlarının kendisine yardım etmesine izin verecek miydi? Tavuk Bacaklı Ev Kaçıyor, serinin ilk kitabını sevenlere muhteşem bir macera daha vadediyor."),
        DashboardBook("Ağaçların Fısıltısı", "Murat Moroğlu", "https://satinal.timas.com.tr/agaclarin-fisiltisi-umutlu-kitaplar-timas-cocuk-murat-moroglu-37713-14-O.webp", "Çorak topraklardan yemyeşil masal diyarına uzanan bir yolculuk… Okul ödevi için mahallenin sessiz kahramanı Yılmaz amcanın kapısını çalan Ece, onu bekleyen sürprizden habersizdir. Yıllar önce bir çocuğun koca bir bozkırı ormana dönüştürme hayaliyle başlayan bu serüven, Ece’nin ellerinde yeniden canlanır. Bir insanın azmi dünyayı nasıl değiştirir? “Ağaçların Fısıltısı”, sizi vazgeçmeyen kalplerin yazdığı o yeşil mucizeye davet ediyor.")
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

@Composable
fun EtkinlikTakvimi(events: List<DashboardEvent>, onKatilClick: (String) -> Unit, listModifier: Modifier = Modifier) {
    var selectedEvent by remember { mutableStateOf<DashboardEvent?>(null) }

    Column {
        Text("Etkinlik Takvimi", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))
        
        // Etkinlikleri ikişerli gruplayarak grid şeklinde gösterelim
        val chunkedEvents = events.chunked(2)
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = listModifier
        ) {
            chunkedEvents.forEach { rowEvents ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min), 
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowEvents.forEach { event ->
                        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            EventCard(
                                event, 
                                onKatilClick, 
                                onDetayClick = { selectedEvent = event },
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    }
                    if (rowEvents.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

    selectedEvent?.let { event ->
        EventDetailDialog(
            event = event,
            onDismiss = { selectedEvent = null },
            onKatilClick = { 
                onKatilClick(it)
                selectedEvent = null
            }
        )
    }
}

@Composable
fun EventCard(
    event: DashboardEvent, 
    onKatilClick: (String) -> Unit, 
    onDetayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        border = borderStroke(Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = event.icon, contentDescription = null, tint = event.iconTint, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(event.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1E293B), textAlign = TextAlign.Center)
                Text(event.date, fontSize = 10.sp, color = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Detay", color = Color(0xFF4AC2E3), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onDetayClick() })
                Text("Katıl", color = Color(0xFFF26122), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onKatilClick(event.title) })
            }
        }
    }
}

@Composable
fun borderStroke(color: Color) = androidx.compose.foundation.BorderStroke(1.dp, color)

@Composable
fun BookDetailDialog(book: DashboardBook, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                AsyncImage(
                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                        .data(book.coverUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = book.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1E293B),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = book.author,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color(0xFFF26122),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = book.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF26122)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(48.dp)
                ) {
                    Text("Kapat", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun EventDetailDialog(event: DashboardEvent, onDismiss: () -> Unit, onKatilClick: (String) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top header with light background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF0E5))
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = event.icon,
                            contentDescription = null,
                            tint = event.iconTint,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = event.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1E293B),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                // Bottom details
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Event, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = event.date, color = Color.Gray, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = event.location, color = Color.Gray, fontSize = 14.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Etkinlik Detayları", fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = event.description, color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { onKatilClick(event.title) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF26122)),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Etkinliğe Katıl", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}


