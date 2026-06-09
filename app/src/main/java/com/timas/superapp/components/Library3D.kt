@file:OptIn(ExperimentalMaterial3Api::class)

package com.timas.superapp

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

// ---------------- DATA ----------------

data class Book(
    val title: String,
    val author: String,
    val color: Color
)

// ---------------- ROOT SCREEN ----------------

@Composable
fun Library3DScreen() {

    val books = listOf(
        Book("Clean Code", "Robert Martin", Color(0xFF9C1B1B)),
        Book("Dune", "Frank Herbert", Color(0xFF1F4EA3)),
        Book("Sapiens", "Yuval Harari", Color(0xFF2F7D32)),
        Book("1984", "George Orwell", Color(0xFF6D2DBD)),
        Book("Nutuk", "M. Kemal Atatürk", Color(0xFFD35400))
    )

    var selectedBook by remember { mutableStateOf<Book?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE9E1D6))
            .padding(16.dp)
    ) {

        Column {

            Text(
                "📚 Kütüphane",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B2A1A)
            )

            Spacer(Modifier.height(20.dp))

            Shelf(books) { book ->
                selectedBook = book
            }
        }

        if (selectedBook != null) {
            BookModal(
                book = selectedBook!!,
                onDismiss = { selectedBook = null }
            )
        }
    }
}

// ---------------- SHELF ----------------

@Composable
fun Shelf(
    books: List<Book>,
    onBookClick: (Book) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF7A5234), Color(0xFF5C3D24))
                )
            )
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            books.forEach { book ->
                BookItem(book, onBookClick)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(20.dp)
                .background(Color(0xFF3E2817))
        )
    }
}

// ---------------- BOOK ITEM ----------------

@Composable
fun BookItem(
    book: Book,
    onClick: (Book) -> Unit
) {

    Box(
        modifier = Modifier
            .width(52.dp)
            .height(220.dp)
            .graphicsLayer {
                rotationZ = 2.5f
                shadowElevation = 12f
                clip = false
            }
            .background(book.color)
            .clickable { onClick(book) }
    ) {

        // sayfa kenarı (ince ve doğal)
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(Color(0xFFF6F1E7))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = book.title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.rotate(90f)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = book.author,
                color = Color.White.copy(0.8f),
                fontSize = 9.sp,
                modifier = Modifier.rotate(90f)
            )
        }
    }
}

// ---------------- MODAL ----------------

@Composable
fun BookModal(
    book: Book,
    onDismiss: () -> Unit
) {

    var open by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(120)
        open = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {

        Scene3D(book, open)
    }
}

// ---------------- 3D BOOK ----------------

@Composable
fun Scene3D(book: Book, open: Boolean) {

    val rotation by animateFloatAsState(
        targetValue = if (open) -150f else 0f,
        animationSpec = tween(900),
        label = "bookRotation"
    )

    Box(
        modifier = Modifier
            .width(240.dp)
            .height(320.dp)
            .graphicsLayer {
                cameraDistance = 14f * density
            }
    ) {

        // pages
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(6.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFFFDF7), Color(0xFFF2EADB))
                    )
                )
        )

        // cover (3D flip)
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0f, 0.5f)
                    rotationY = rotation
                    cameraDistance = 14f * density
                }
                .clip(RoundedCornerShape(6.dp))
                .background(
                    Brush.linearGradient(
                        listOf(book.color, book.color.copy(alpha = 0.85f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    book.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    book.author,
                    color = Color.White.copy(0.8f),
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    "Kapağa tıkla → Aç",
                    color = Color.White.copy(0.6f),
                    fontSize = 11.sp
                )
            }
        }
    }
}