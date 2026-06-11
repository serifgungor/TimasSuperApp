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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
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
    val thumbnailUrl: String,
    val youtubeId: String
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
        TimasVideo("Levent ile Şirin Maceralar", "12:45", "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&q=80&w=300", "i-sM_O0xW1g"),
        TimasVideo("Timaş Çocuk Hikayeleri", "08:30", "https://images.unsplash.com/photo-1544947950-fa07a98d237f?auto=format&fit=crop&q=80&w=300", "i-sM_O0xW1g"),
        TimasVideo("Eğlenceli Bilgi Dünyası", "15:20", "https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?auto=format&fit=crop&q=80&w=300", "i-sM_O0xW1g"),
        TimasVideo("Masal Saati", "10:15", "https://images.unsplash.com/photo-1516585427167-9f4af9627e6c?auto=format&fit=crop&q=80&w=300", "i-sM_O0xW1g")
    )
    
    val (selectedVideo, setSelectedVideo) = remember { mutableStateOf<TimasVideo?>(null) }

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
                    TimasCocukTvSection(videos, onVideoClick = { setSelectedVideo(it) })
                }
            }
        } else {
            TimasOyunSection(games)
            Spacer(modifier = Modifier.height(24.dp))
            TimasCocukTvSection(videos, onVideoClick = { setSelectedVideo(it) })
        }
    }

    if (selectedVideo != null) {
        VideoPlayerDialog(
            video = selectedVideo!!,
            onDismiss = { setSelectedVideo(null) }
        )
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
fun TimasCocukTvSection(videos: List<TimasVideo>, onVideoClick: (TimasVideo) -> Unit) {
    Column {
        SectionHeader(title = "Timaş Çocuk TV", actionText = "Çizgi Film")
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(videos) { video ->
                VideoCard(video, onClick = { onVideoClick(video) })
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
fun VideoCard(video: TimasVideo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(130.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clickable { onClick() },
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

@Composable
fun VideoPlayerDialog(video: TimasVideo, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = android.view.ViewGroup.LayoutParams(
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(android.graphics.Color.BLACK)
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mediaPlaybackRequiresUserGesture = false
                        settings.useWideViewPort = true
                        settings.loadWithOverviewMode = true
                        webChromeClient = WebChromeClient()
                        webViewClient = object : WebViewClient() {
                            override fun onReceivedError(
                                view: WebView?,
                                request: android.webkit.WebResourceRequest?,
                                error: android.webkit.WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                if (request?.url?.toString()?.contains("youtube") == true || request?.isForMainFrame == true) {
                                    val errorHtml = """
                                        <!DOCTYPE html>
                                        <html>
                                        <head>
                                            <meta name="viewport" content="width=device-width, initial-scale=1">
                                            <style>
                                                body { background-color: #F8FAFC; color: #1E293B; font-family: sans-serif; display: flex; flex-direction: column; justify-content: center; align-items: center; height: 100vh; margin: 0; text-align: center; padding: 20px; box-sizing: border-box; }
                                                h2 { margin-bottom: 8px; color: #0F172A; }
                                                p { color: #64748B; margin-top: 0; font-size: 14px; }
                                                svg { width: 64px; height: 64px; fill: #94A3B8; margin-bottom: 16px; }
                                            </style>
                                        </head>
                                        <body>
                                            <svg viewBox="0 0 24 24"><path d="M11 15h2v2h-2zm0-8h2v6zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z"/></svg>
                                            <h2>Bağlantı Hatası</h2>
                                            <p>Video yüklenirken bir sorun oluştu.<br/>Lütfen internet bağlantınızı kontrol edip tekrar deneyin.</p>
                                        </body>
                                        </html>
                                    """.trimIndent()
                                    view?.loadDataWithBaseURL(null, errorHtml, "text/html", "utf-8", null)
                                }
                            }
                        }
                        
                        val html = """
                            <!DOCTYPE html>
                            <html>
                            <body style="margin:0;padding:0;background-color:black;">
                                <iframe width="100%" height="100%" src="https://www.youtube.com/embed/${video.youtubeId}?autoplay=1&rel=0&modestbranding=1" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                            </body>
                            </html>
                        """.trimIndent()
                        
                        loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Close Button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Kapat",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
