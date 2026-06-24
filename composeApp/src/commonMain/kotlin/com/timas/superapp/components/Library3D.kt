package com.timas.superapp

import com.timas.superapp.showToast

import coil3.compose.LocalPlatformContext

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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.SubcomposeAsyncImage
import kotlinx.coroutines.delay

// ─── Renk Paleti (Vintage Library Theme) ──────────────────────
private val Orange       = Color(0xFFF26122)
private val LightOrange  = Color(0xFFFFF0EB)
private val CardBg       = Color(0xFFFDFBF7) // Parchment paper container color
private val TextMain     = Color(0xFF0F172A)
private val TextMuted    = Color(0xFF64748B)
private val BorderClr    = Color(0xFFEDE6D6) // Parchment dark border/divider
private val StatPanelBg  = Color(0xFFF4EDE0) // Warm stats panel background

// ---------------- ROOT SCREEN ----------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Library3DScreen() {
    val context = LocalPlatformContext.current

    // Mutable state list containing the entire database of 23 verified books
    val libraryBooks = LibraryDatabase.books

    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    // Dialog state controllers
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var activeEBookReader by remember { mutableStateOf<Book?>(null) }
    var activeAudioPlayer by remember { mutableStateOf<Book?>(null) }
    var bookToPurchase by remember { mutableStateOf<Book?>(null) } // Purchase dialog state



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

    val recommendedBooks = remember(filteredBooks) {
        filteredBooks.filter { it.category == "Önerilenler" }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2C1A10), // Rich dark walnut
                        Color(0xFF1D0E07)  // Deep mahogany shadow
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .drawBehind {
                    // Draw continuous vertical board seams on the backing wood paneling
                    val plankWidth = 85.dp.toPx()
                    var currentX = 0f
                    while (currentX < size.width) {
                        // Vertical plank gap line
                        drawLine(
                            color = Color(0xFF160A05),
                            start = androidx.compose.ui.geometry.Offset(currentX, 0f),
                            end = androidx.compose.ui.geometry.Offset(currentX, size.height),
                            strokeWidth = 2.dp.toPx()
                        )
                        // Beveled highlight line next to the gap for 3D depth
                        drawLine(
                            color = Color(0xFF3E281C),
                            start = androidx.compose.ui.geometry.Offset(currentX + 2.dp.toPx(), 0f),
                            end = androidx.compose.ui.geometry.Offset(currentX + 2.dp.toPx(), size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                        currentX += plankWidth
                    }

                    // Soft ambient library lighting lamp glow (radial gradient from center top)
                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF5D4037).copy(alpha = 0.5f), Color.Transparent),
                            center = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height / 4f),
                            radius = size.width
                        )
                    )
                }
        ) {
            // --- 2. SEARCH INTERFACE ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 42.dp)
                    .padding(top = 28.dp, bottom = 16.dp)
            ) {
                if (isSearchActive) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Seri, kitap veya yazar ara...", color = Color(0xFFE5D5C5).copy(alpha = 0.5f), fontSize = 15.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFC5A059)) },
                        trailingIcon = {
                            IconButton(onClick = {
                                searchQuery = ""
                                isSearchActive = false
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Kapat", tint = Color(0xFFE5D5C5).copy(alpha = 0.6f))
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(fontSize = 15.sp, color = Color(0xFFE5D5C5)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFFE5D5C5),
                            unfocusedTextColor = Color(0xFFE5D5C5),
                            focusedContainerColor = Color(0xFF1E110A),
                            unfocusedContainerColor = Color(0xFF1E110A),
                            focusedBorderColor = Color(0xFFC5A059), // Brass gold highlight
                            unfocusedBorderColor = Color(0xFF4E3629)
                        ),
                        singleLine = true
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E110A), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF4E3629), RoundedCornerShape(12.dp))
                            .clickable { isSearchActive = true }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Ara",
                            tint = Color(0xFFC5A059),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Kitap veya yazar ara...",
                            color = Color(0xFFE5D5C5).copy(alpha = 0.5f),
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // --- 2.5. MY BOOKS (Kitaplarım Shelf) ---
            if (standaloneBooks.isNotEmpty()) {
                BookshelfSection(
                    seriesName = "Kitaplarım",
                    books = standaloneBooks,
                    onBookClick = { book ->
                        selectedBook = book
                    }
                )
            }

            // --- 3. BOOK SERIES SHELVES ---
            seriesList.forEach { (seriesName, books) ->
                if (books.isNotEmpty()) {
                    BookshelfSection(
                        seriesName = seriesName,
                        books = books,
                        onBookClick = { book ->
                            if (book.isOwned) {
                                selectedBook = book
                            } else {
                                bookToPurchase = book
                            }
                        }
                    )
                }
            }

            // --- 3.5. RECOMMENDED BOOKS SHELF ---
            if (recommendedBooks.isNotEmpty()) {
                BookshelfSection(
                    seriesName = "Önerilenler",
                    books = recommendedBooks,
                    onBookClick = { book ->
                        if (book.isOwned) {
                            selectedBook = book
                        } else {
                            bookToPurchase = book
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }

        // --- 5. OVERLAYS, DIALOGS AND PURCHASE SYSTEM RESOLVERS ---

        // A. Book Details Dialog Overlay (For owned books)
        if (selectedBook != null) {
            val dialogWidth = 0.95f
            Dialog(
                onDismissRequest = { selectedBook = null },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(dialogWidth)
                        .fillMaxHeight(0.85f)
                        .clip(RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF18100C)
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
                        },
                        isLightMode = false
                    )
                }
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
                                showToast("${book.title} satın alındı! Kütüphanenize eklendi.")
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

        // Left sideboard vertical wood column (Fixed overlay on the screen edges!)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(22.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF422F25),
                            Color(0xFF5A4031),
                            Color(0xFF35241B)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF6E503E), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(0.dp)
                )
        )
        // Left sideboard inward gradient shadow
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 22.dp)
                .width(18.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Right sideboard vertical wood column (Fixed overlay!)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(22.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF35241B),
                            Color(0xFF5A4031),
                            Color(0xFF422F25)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF6E503E))
                    ),
                    shape = RoundedCornerShape(0.dp)
                )
        )
        // Right sideboard inward gradient shadow
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 22.dp)
                .width(18.dp)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
        )
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
                .background(LightOrange),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Orange,
                modifier = Modifier.size(16.dp)
            )
        }
        Column {
            Text(text = label, fontSize = 9.sp, color = TextMuted)
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMain)
        }
    }
}

@Composable
private fun ShelfBookItem(
    book: Book,
    index: Int,
    totalCount: Int,
    onClick: () -> Unit
) {
    // Variations in size based on book title length
    val sizeSeed = book.title.length
    val bookHeight = 150.dp + ((sizeSeed % 3) * 4).dp  // varies: 150.dp, 154.dp, 158.dp
    val bookWidth = 100.dp + ((sizeSeed % 2) * 5).dp   // varies: 100.dp, 105.dp

    // Leaning angle at boundaries
    val rotationAngle = when {
        totalCount > 1 && index == 0 -> -5.5f // Leans left
        totalCount > 1 && index == totalCount - 1 -> 5.5f // Leans right
        else -> 0f
    }

    Column(
        modifier = Modifier
            .width(bookWidth + 12.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Book Title (Printed cleanly above the cover)
        Text(
            text = book.title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE5D5C5), // Elegant parchment cream text
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Book Cover Wrapper
        Box(
            modifier = Modifier
                .width(bookWidth)
                .height(bookHeight)
                .graphicsLayer {
                    rotationZ = rotationAngle
                    transformOrigin = TransformOrigin(0.5f, 1.0f)
                }
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 2.dp, bottomEnd = 2.dp)
                )
                .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 2.dp, bottomEnd = 2.dp))
        ) {
            if (book.isOwned) {
                // Colored cover
                SubcomposeAsyncImage(
                    model = book.coverUrl,
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = { BookCoverFallback(book = book) },
                    error = { BookCoverFallback(book = book) }
                )

                // 3D book shine overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.12f),
                                    Color.White.copy(alpha = 0.05f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.1f)
                                )
                            )
                        )
                )

                // Owned badges: Ebook + Audio
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 3.dp, vertical = 1.5.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Color(0xFF81C784),
                        modifier = Modifier.size(8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = Color(0xFFFFB74D),
                        modifier = Modifier.size(8.dp)
                    )
                }

                // Silk Ribbon Bookmark for in-progress books
                if (book.progress > 0) {
                    Canvas(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 12.dp)
                            .offset(y = (-4).dp)
                            .width(8.dp)
                            .height(20.dp)
                    ) {
                        val path = Path().apply {
                            moveTo(0f, 0f)
                            lineTo(size.width, 0f)
                            lineTo(size.width, size.height)
                            lineTo(size.width / 2f, size.height - 3.dp.toPx())
                            lineTo(0f, size.height)
                            close()
                        }
                        drawPath(
                            path = path,
                            color = Orange
                        )
                    }

                    // Gold progress sticker
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "%${book.progress}",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFF2AC)
                        )
                    }
                } else {
                    // "Yeni" green tag if not started
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF2E7D32))
                            .padding(horizontal = 4.dp, vertical = 1.5.dp)
                    ) {
                        Text(
                            text = "YENİ",
                            fontSize = 7.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            } else {
                // Locked cover (blurred + grayscale)
                SubcomposeAsyncImage(
                    model = book.coverUrl,
                    contentDescription = book.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(5.dp),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.1f) }),
                    alpha = 0.45f,
                    loading = { BookCoverFallback(book = book) },
                    error = { BookCoverFallback(book = book) }
                )

                // Frosted lock overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.2f), Color.Black.copy(alpha = 0.5f))
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // red Eksik Cilt banner
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(Color(0xFFE63946).copy(alpha = 0.85f))
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "EKSİK CİLT",
                        color = Color.White,
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Hinge crease shadow
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.25f),
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.2f)
                            )
                        )
                    )
            )

            // Volume label (bottom left)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (book.isOwned) Orange else Color(0xFF475569))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Cilt ${book.volumeNumber}",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun BookshelfSection(
    seriesName: String,
    books: List<Book>,
    onBookClick: (Book) -> Unit
) {
    val ownedInSeries = books.count { it.isOwned }
    val totalInSeries = books.size
    val isSeries = seriesName != "Kitaplarım" && seriesName != "Önerilenler"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Shelf title, subtitle and badge above the shelf
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 42.dp, vertical = 4.dp), // indent slightly to clear sideboards (22.dp + padding)
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = seriesName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = if (isSeries) "Eksik Ciltleri Tamamla" else "Kitaplığımdaki Tüm Eserlerim",
                    fontSize = 10.5.sp,
                    color = if (isSeries) Color(0xFFFF9F43) else Color(0xFF81C784),
                    fontWeight = FontWeight.Bold
                )
            }

            // Right-aligned badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFF9F43).copy(alpha = 0.12f))
                    .border(1.dp, Color(0xFFFF9F43).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isSeries) "Cilt: $ownedInSeries/$totalInSeries" else "$totalInSeries Eser",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9F43)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Shelf & Books Box
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Shadow behind the books on the shelf backing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 18.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f))
                        )
                    )
            )

            // Books LazyRow
            LazyRow(
                contentPadding = PaddingValues(start = 38.dp, end = 38.dp, top = 16.dp, bottom = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(books) { index, book ->
                    ShelfBookItem(
                        book = book,
                        index = index,
                        totalCount = books.size,
                        onClick = { onBookClick(book) }
                    )
                }
            }

            // Wooden shelf plank at the bottom of the Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                // Shelf top surface
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF7E5C46), // Highlight edge
                                    Color(0xFF5A4031)  // Main wood surface
                                )
                            )
                        )
                )

                // Shelf front edge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF422F25), // Bevel shadow
                                    Color(0xFF35241B)  // Front dark mahogany edge
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Brass plaque
                    Row(
                        modifier = Modifier
                            .heightIn(min = 20.dp)
                            .shadow(3.dp, RoundedCornerShape(2.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFE5C158),
                                        Color(0xFFFFF2AC),
                                        Color(0xFFC5A059),
                                        Color(0xFFFFF2AC),
                                        Color(0xFFB38F43)
                                    )
                                ),
                                shape = RoundedCornerShape(2.dp)
                            )
                            .border(0.5.dp, Color(0xFF8C6D31), RoundedCornerShape(2.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Screw rivets
                        Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color(0xFF5A4010)))
                        Text(
                            text = seriesName.uppercase(),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF261C08),
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 0.8.sp
                        )
                        Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color(0xFF5A4010)))
                    }
                }
            }
        }

        // Soft shadow underneath the shelf
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.45f), Color.Transparent)
                    )
                )
        )
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
        book.getBookPages()
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
    val context = LocalPlatformContext.current
    val totalSeconds = book.totalSeconds
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
                    text = "${minsCurrent.toString().padStart(2, '0')}:${secsCurrent.toString().padStart(2, '0')}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )

                val minsTotal = totalSeconds / 60
                val secsTotal = totalSeconds % 60
                Text(
                    text = "${minsTotal.toString().padStart(2, '0')}:${secsTotal.toString().padStart(2, '0')}",
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
                        showToast("Ses seviyesi optimize edildi.")
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
