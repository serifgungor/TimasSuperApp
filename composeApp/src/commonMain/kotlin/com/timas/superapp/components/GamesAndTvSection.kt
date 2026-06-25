package com.timas.superapp.components

import timas.composeapp.generated.resources.Res
import timas.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

import com.timas.superapp.openUrl
import com.timas.superapp.showToast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

data class TimasGame(
    val title: String,
    val subtitle: String,
    val url: String,
    val iconResId: org.jetbrains.compose.resources.DrawableResource,
    val gradientColors: List<Color>,
    val badgeText: String? = null
)

data class TimasVideo(
    val title: String,
    val duration: String,
    val thumbnailUrl: String,
    val youtubeId: String
)

@Composable
fun GamesAndTvSection() {
    var activeUrl by remember { mutableStateOf<String?>(null) }
    var activeTitle by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val isTablet = maxWidth >= 600.dp

        val games = listOf(
            TimasGame(
                title = "Kelime Oyunu",
                subtitle = "Harfleri birleştir, kelimeleri bul!",
                url = "https://lemoni.tr/KemileOyunu.html",
                iconResId = Res.drawable.game_kelime_oyunu,
                gradientColors = listOf(Color(0xFFFF8A65), Color(0xFFE64A19)),
                badgeText = "Yeni"
            ),
            TimasGame(
                title = "Kelime Avı",
                subtitle = "Gizlenmiş kelimeleri yakala!",
                url = "https://lemoni.tr/KelmieAvi.Html",
                iconResId = Res.drawable.game_kelime_avi,
                gradientColors = listOf(Color(0xFF4DB6AC), Color(0xFF00796B)),
                badgeText = "Popüler"
            ),
            TimasGame(
                title = "Milyoner",
                subtitle = "Soruları cevapla, zirveye ulaş!",
                url = "https://lemoni.tr/Milyoner.html",
                iconResId = Res.drawable.game_milyoner,
                gradientColors = listOf(Color(0xFF9575CD), Color(0xFF512DA8)),
                badgeText = "Efsane"
            ),
            TimasGame(
                title = "Kültür Merkezi",
                subtitle = "Genel kültür sorularıyla yarışın!",
                url = "https://lemoni.tr/3Te3.html",
                iconResId = Res.drawable.game_zekii_bulmaca,
                gradientColors = listOf(Color(0xFF29B6F6), Color(0xFF0288D1)),
                badgeText = "Yeni"
            )
        )

        val videos = listOf(
            TimasVideo("Duvarın Arkasında Ne Var?", "04:15", "https://img.youtube.com/vi/Q8oq-bUc3uM/hqdefault.jpg", "Q8oq-bUc3uM"),
            TimasVideo("Biri Diğeri Öteki Beriki", "02:30", "https://img.youtube.com/vi/nlsw8sskJks/hqdefault.jpg", "nlsw8sskJks"),
            TimasVideo("Kanatlarım Var Benim", "03:10", "https://img.youtube.com/vi/JoWT87W7YQg/hqdefault.jpg", "JoWT87W7YQg"),
            TimasVideo("Gel Bana Masal Anlat", "05:45", "https://img.youtube.com/vi/T2IWqVaXnuY/hqdefault.jpg", "T2IWqVaXnuY"),
            TimasVideo("Yolcu Oyunu", "06:20", "https://img.youtube.com/vi/tgPDWbF16yk/hqdefault.jpg", "tgPDWbF16yk")
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            if (isTablet) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        TimasOyunSection(games, isTablet = true, onGameClick = { game ->
                            if (game.badgeText == "Yakında" || game.url.isEmpty()) {
                                showToast("${game.title} yakında sizlerle!")
                            } else {
                                activeUrl = game.url
                                activeTitle = game.title
                            }
                        })
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        TimasCocukTvSection(videos, isTablet = true, onVideoClick = { video ->
                            activeUrl = "https://www.youtube.com/embed/${video.youtubeId}"
                            activeTitle = video.title
                        })
                    }
                }
            } else {
                TimasOyunSection(games, isTablet = false, onGameClick = { game ->
                    if (game.badgeText == "Yakında" || game.url.isEmpty()) {
                        showToast("${game.title} yakında sizlerle!")
                    } else {
                        activeUrl = game.url
                        activeTitle = game.title
                    }
                })
                Spacer(modifier = Modifier.height(24.dp))
                TimasCocukTvSection(videos, isTablet = false, onVideoClick = { video ->
                    activeUrl = "https://www.youtube.com/embed/${video.youtubeId}"
                    activeTitle = video.title
                })
            }
        }
    }

    if (activeUrl != null) {
        FullScreenWebViewDialog(
            title = activeTitle.orEmpty(),
            url = activeUrl!!,
            onDismiss = {
                activeUrl = null
                activeTitle = null
            }
        )
    }
}

@Composable
fun TimasOyunSection(games: List<TimasGame>, isTablet: Boolean, onGameClick: (TimasGame) -> Unit) {
    Column {
        SectionHeader(title = "Timaş Oyun", actionText = "Oyun Gezegeni")
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(games) { game ->
                GameCard(game, isTablet = isTablet, onClick = { onGameClick(game) })
            }
        }
    }
}

@Composable
fun TimasCocukTvSection(videos: List<TimasVideo>, isTablet: Boolean, onVideoClick: (TimasVideo) -> Unit) {
    Column {
        SectionHeader(title = "Timaş Çocuk TV", actionText = "Çizgi Film")
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(videos) { video ->
                VideoCard(video, isTablet = isTablet, onClick = { onVideoClick(video) })
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
fun GameCard(game: TimasGame, isTablet: Boolean, onClick: () -> Unit) {
    val cardWidth = if (isTablet) 240.dp else 180.dp
    val cardHeight = if (isTablet) 180.dp else 145.dp
    val iconSize = if (isTablet) 56.dp else 46.dp
    val titleFontSize = if (isTablet) 16.sp else 14.sp
    val subtitleFontSize = if (isTablet) 11.sp else 10.sp
    
    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(game.gradientColors))
                .padding(if (isTablet) 18.dp else 14.dp)
        ) {
            if (game.badgeText != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(0.5.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = game.badgeText,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(iconSize)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(game.iconResId),
                        contentDescription = game.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = game.title,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = titleFontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = game.subtitle,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = subtitleFontSize,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun VideoCard(video: TimasVideo, isTablet: Boolean, onClick: () -> Unit) {
    val cardWidth = if (isTablet) 280.dp else 220.dp
    val cardHeight = if (isTablet) 175.dp else 135.dp
    val titleFontSize = if (isTablet) 13.sp else 12.sp
    
    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
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
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                // Duration Label
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
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
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = video.title,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = titleFontSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
