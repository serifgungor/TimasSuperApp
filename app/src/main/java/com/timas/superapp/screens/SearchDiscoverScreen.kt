package com.timas.superapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// Veri modelleri
data class DiscoverBook(
    val title: String,
    val author: String,
    val imageUrl: String,
    val price: String,
    val category: String
)

data class DiscoverCategory(val name: String, val color: Color)

@Composable
fun SearchDiscoverScreen(searchQuery: String) {
    val discoverBooks = listOf(
        DiscoverBook("Kalpsizler", "Marissa Meyer", "https://cdn.timas.com.tr/urun/kalpsizler-9786050847642.jpg", "150 TL", "Roman"),
        DiscoverBook("Beyaz Diş", "Jack London", "https://cdn.timas.com.tr/urun/beyaz-dis-9786050845341.jpg", "180 TL", "Klasik"),
        DiscoverBook("Mutluluğun İnşası", "Mecit Ömür Öztürk", "https://cdn.timas.com.tr/urun/mutlulugun-insasi-9786050849745.jpg", "90 TL", "Psikoloji"),
        DiscoverBook("Dilin Afetleri", "İmam Gazzâlî", "https://cdn.timas.com.tr/urun/dilin-afetleri-9786259445182.jpg", "120 TL", "İslam"),
        DiscoverBook("Kur'an Atlası", "Timaş Yayınları", "https://cdn.timas.com.tr/urun/kuran-atlasi-9786256360525.jpg", "110 TL", "İslam"),
        DiscoverBook("Kelimeler Adası", "Timaş Çocuk", "https://cdn.timas.com.tr/urun/kelimeler-adasi-9786050849646.jpg", "60 TL", "Çocuk")
    )

    val discoverCategories = listOf(
        DiscoverCategory("Tarih", Color(0xFF795548)),
        DiscoverCategory("Roman", Color(0xFFE65100)),
        DiscoverCategory("Bilim Kurgu", Color(0xFF455A64)),
        DiscoverCategory("Çocuk", Color(0xFFF57F17)),
        DiscoverCategory("İslam", Color(0xFF3949AB)),
        DiscoverCategory("Psikoloji", Color(0xFF8D6E63)),
        DiscoverCategory("Edebiyat", Color(0xFF2E7D32)),
        DiscoverCategory("Klasikler", Color(0xFF1565C0)),
        DiscoverCategory("Felsefe", Color(0xFF7E57C2)),
        DiscoverCategory("Biyografi", Color(0xFF00897B))
    )

    val isSearching = searchQuery.trim().isNotEmpty()

    if (isSearching) {
        val searchResults = discoverBooks.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.author.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true)
        }
        
        SearchResultsView(query = searchQuery, results = searchResults)
    } else {
        DiscoverView(discoverBooks = discoverBooks, categories = discoverCategories)
    }
}

@Composable
fun DiscoverView(discoverBooks: List<DiscoverBook>, categories: List<DiscoverCategory>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Keşfet",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        item {
            Text(
                text = "Trend Olanlar",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(discoverBooks) { book ->
                    DiscoverBookCard(book = book)
                }
            }
        }

        item {
            Text(
                text = "Kategorilere Göre",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // Grid benzeri görünüm için satırlara bölelim (her satırda 2 kategori)
            val chunkedCategories = categories.chunked(2)
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                chunkedCategories.forEach { rowCategories ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowCategories.forEach { category ->
                            CategoryChip(
                                category = category,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Eğer son satırda 1 tane kaldıysa boşluk bırak
                        if (rowCategories.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultsView(query: String, results: List<DiscoverBook>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "'$query' için sonuçlar (${results.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        if (results.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aramanızla eşleşen sonuç bulunamadı.",
                        color = Color(0xFF94A3B8),
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            items(results) { book ->
                SearchResultItem(book = book)
            }
        }
    }
}

@Composable
fun DiscoverBookCard(book: DiscoverBook) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { /* Detaya git */ }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.65f)
                .shadow(4.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        ) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF1E293B),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = book.author,
            fontSize = 12.sp,
            color = Color(0xFF64748B),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = book.price,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            color = Color(0xFFF26122)
        )
    }
}

@Composable
fun CategoryChip(category: DiscoverCategory, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(category.color.copy(alpha = 0.15f))
            .border(1.dp, category.color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clickable { /* Kategori seçimi */ },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = null,
                tint = category.color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = category.name,
                fontWeight = FontWeight.Bold,
                color = category.color,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun SearchResultItem(book: DiscoverBook) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
            .clickable { /* Detaya git */ }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(70.dp)
                .aspectRatio(0.65f)
                .clip(RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = book.category,
                fontSize = 11.sp,
                color = Color(0xFFF26122),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color(0xFFF26122).copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = book.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1E293B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.author,
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = book.price,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = Color(0xFF1E293B)
            )
        }
    }
}
