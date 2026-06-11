package com.timas.superapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class TimasGame(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBgColor: Color
)

data class TimasVideo(
    val title: String,
    val duration: String,
    val thumbnailUrl: String
)

@Composable
fun GamesAndTvSection() {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    val games = listOf(
        TimasGame("Kelime Avı", "Harfleri birleştir, kelimeyi bul.", Icons.Default.Search, Color(0xFFE65100), Color(0xFFFFE0B2).copy(alpha = 0.5f)),
        TimasGame("Kim Bilmek İster", "Bilgi yarışmasında rekabet et.", Icons.Default.EmojiEvents, Color(0xFFF57F17), Color(0xFFFFF9C4).copy(alpha = 0.5f)),
        TimasGame("Harf Bulmaca", "Eksik harfleri tamamla.", Icons.Default.Extension, Color(0xFF00796B), Color(0xFFB2DFDB).copy(alpha = 0.5f)),
        TimasGame("Bilgi Yarışması", "Genel kültürünü sına.", Icons.Default.Psychology, Color(0xFF512DA8), Color(0xFFD1C4E9).copy(alpha = 0.5f))
    )

    val videos = listOf(
        TimasVideo("Levent ile Şirin Maceralar", "12:45", "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&q=80&w=300"),
        TimasVideo("Timaş Çocuk Hikayeleri", "08:30", "https://images.unsplash.com/photo-1544947950-fa07a98d237f?auto=format&fit=crop&q=80&w=300"),
        TimasVideo("Eğlenceli Bilgi Dünyası", "15:20", "https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?auto=format&fit=crop&q=80&w=300"),
        TimasVideo("Masal Saati", "10:15", "https://images.unsplash.com/photo-1516585427167-9f4af9627e6c?auto=format&fit=crop&q=80&w=300")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (isTablet) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    TimasOyunSection(games)
                }
                Column(modifier = Modifier.weight(1f)) {
                    TimasCocukTvSection(videos)
                }
            }
        } else {
            TimasOyunSection(games)
            Spacer(modifier = Modifier.height(24.dp))
            TimasCocukTvSection(videos)
        }
    }
}

@Composable
fun TimasOyunSection(games: List<TimasGame>) {
    Column {
        SectionHeader(title = "Timaş Oyun", actionText = "Oyun Gezegeni")
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(games) { game ->
                GameCard(game)
            }
        }
    }
}

@Composable
fun TimasCocukTvSection(videos: List<TimasVideo>) {
    Column {
        SectionHeader(title = "Timaş Çocuk TV", actionText = "Çizgi Film")
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(videos) { video ->
                VideoCard(video)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, actionText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            fontSize = 16.sp
        )
        Text(
            text = actionText,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00ACC1),
            fontSize = 12.sp,
            modifier = Modifier.clickable { /* action */ }
        )
    }
}

@Composable
fun GameCard(game: TimasGame) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(130.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(game.iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = game.icon,
                    contentDescription = game.title,
                    tint = game.iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = game.title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = game.subtitle,
                color = Color(0xFF64748B),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun VideoCard(video: TimasVideo) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(130.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AsyncImage(
                    model = video.thumbnailUrl,
                    contentDescription = video.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Play Button Overlay
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                // Duration Label
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = video.duration,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Title Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = video.title,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
