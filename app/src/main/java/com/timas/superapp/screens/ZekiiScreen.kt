package com.timas.superapp.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Message(
    val id: String,
    val text: String,
    val isFromBot: Boolean,
    val isTyping: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZekiiScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val maxBubbleWidth = if (isTablet) {
        (configuration.screenWidthDp * 0.5f).dp
    } else {
        (configuration.screenWidthDp * 0.8f).dp
    }

    // Mesaj Listesi Başlangıç Değerleri
    val messages = remember {
        mutableStateListOf(
            Message(
                id = "1",
                text = "Merhaba, ben Zekiii. Timaş Dijital'in geliştirdiği bir yapay zekayım. Size nasıl yardımcı olabilirim?",
                isFromBot = true
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    var scrollTrigger by remember { mutableStateOf(0) }

    // Mood listesi
    val moods = listOf(
        "Stresliyim",
        "İlham arıyorum",
        "Sıkıldım",
        "Mutluyum",
        "Yeni bir şey öğrenmek istiyorum"
    )

    // Yeni mesaj eklendiğinde veya aksiyon gerçekleştiğinde otomatik aşağı kaydır
    LaunchedEffect(scrollTrigger) {
        if (scrollTrigger > 0 && messages.isNotEmpty()) {
            delay(100) // Render işleminin tamamlanması için küçük bir gecikme
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Bot Cevap Mantığı
    fun handleBotResponse(userMsg: String) {
        coroutineScope.launch {
            // "Yazıyor..." durumunu ekle
            val typingId = System.currentTimeMillis().toString()
            messages.add(
                Message(
                    id = typingId,
                    text = "",
                    isFromBot = true,
                    isTyping = true
                )
            )
            scrollTrigger++
            
            // 1 saniye simüle et
            delay(1200)

            // "Yazıyor..." mesajını kaldır
            messages.removeAll { it.id == typingId }

            // Cevabı belirle
            val botText = when (userMsg.trim().lowercase()) {
                "ilham arıyorum" -> {
                    "“İlham arıyorum” moduna uygun harika bir kitap buldum! 📚\n\n📖 Önerim: Düşünme Gücü\nYazar: Ahmet Şerif İzgören"
                }
                "stresliyim" -> {
                    "“Stresliyim” moduna uygun harika bir kitap buldum! 📚\n\n📖 Önerim: İnsan İnsana\nYazar: Doğan Cüceloğlu"
                }
                "sıkıldım" -> {
                    "“Sıkıldım” moduna uygun harika bir kitap buldum! 📚\n\n📖 Önerim: Beyaz Diş\nYazar: Jack London"
                }
                "mutluyum" -> {
                    "“Mutluyum” moduna uygun harika bir kitap buldum! 📚\n\n📖 Önerim: Küçük Prens\nYazar: Antoine de Saint-Exupéry"
                }
                "yeni bir şey öğrenmek istiyorum" -> {
                    "“Yeni bir şey öğrenmek istiyorum” moduna uygun harika bir kitap buldum! 📚\n\n📖 Önerim: Sapiens\nYazar: Yuval Noah Harari"
                }
                else -> {
                    "Şu an demo modunda çalışmaktayım, sadece belirli sorulara cevap verebiliyorum. 😊 Yukarıdaki mod butonlarını kullanarak hızlıca harika kitap önerileri alabilirsin! 📚✨"
                }
            }

            // Bot cevabını ekle
            messages.add(
                Message(
                    id = System.currentTimeMillis().toString(),
                    text = botText,
                    isFromBot = true
                )
            )
            scrollTrigger++
        }
    }

    // Kullanıcı Mesaj Gönderme
    fun sendMessage(text: String) {
        if (text.trim().isEmpty()) return
        messages.add(
            Message(
                id = System.currentTimeMillis().toString(),
                text = text,
                isFromBot = false
            )
        )
        inputText = ""
        scrollTrigger++
        handleBotResponse(text)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            // ZEKİİ Custom Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Geri",
                            tint = Color(0xFF1E293B),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = painterResource(id = com.timas.superapp.R.drawable.zekii_chatbot_logo),
                        contentDescription = "ZEKİİ Logo",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0xFFCBD5E1), CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "ZEKİİ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(Color(0xFFF1F5F9)) // Hafif grimsi/mavi arka plan
        ) {
            // Mod Çipleri Satırı (Header'ın hemen altında)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                moods.forEach { mood ->
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(20.dp))
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(20.dp))
                            .clickable { sendMessage(mood) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = mood,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF475569)
                        )
                    }
                }
            }

            // Sohbet Mesajları Alanı (WhatsApp tarzı desenli arka planlı Box)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFFE8E2D9)) // WhatsApp beji arka plan rengi
                    .drawWithCache {
                        val patternEmojis = listOf("📚", "📖", "✏️", "✨", "💡", "🎓", "🖋️")
                        val emojiPaint = android.graphics.Paint().apply {
                            textSize = 34.dp.toPx() // Daha büyük boyut
                            textAlign = android.graphics.Paint.Align.CENTER
                            alpha = (255 * 0.12f).toInt() // Daha belirgin (%12 opaklık)
                        }
                        
                        onDrawBehind {
                            val stepX = 100.dp.toPx() // Emojilerin çakışmaması için daha geniş aralık
                            val stepY = 100.dp.toPx()
                            val width = size.width
                            val height = size.height
                            
                            var rowIndex = 0
                            var y = 25.dp.toPx()
                            while (y < height) {
                                val offsetX = if (rowIndex % 2 == 0) 0f else stepX / 2f
                                var x = offsetX + 25.dp.toPx()
                                var colIndex = 0
                                while (x < width) {
                                    val emojiIndex = (rowIndex * 7 + colIndex) % patternEmojis.size
                                    val emoji = patternEmojis[emojiIndex]
                                    val rotation = ((rowIndex * 3 + colIndex * 5) % 40) - 20f
                                    
                                    drawContext.canvas.nativeCanvas.save()
                                    drawContext.canvas.nativeCanvas.rotate(rotation, x, y)
                                    drawContext.canvas.nativeCanvas.drawText(emoji, x, y, emojiPaint)
                                    drawContext.canvas.nativeCanvas.restore()
                                    
                                    x += stepX
                                    colIndex++
                                }
                                y += stepY
                                rowIndex++
                            }
                        }
                    }
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                items(messages) { message ->
                    if (message.isFromBot) {
                        // Zekii Mesaj Balonu
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .widthIn(max = maxBubbleWidth)
                                    .shadow(2.dp, RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                                    .background(Color.White, RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                                    .padding(16.dp)
                            ) {
                                if (message.isTyping) {
                                    // Yazıyor animasyonu
                                    TypingIndicator()
                                } else {
                                    Text(
                                        text = message.text,
                                        color = Color(0xFF1E293B),
                                        fontSize = 15.sp,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Kullanıcı Mesaj Balonu
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .widthIn(max = maxBubbleWidth)
                                    .shadow(2.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 0.dp))
                                    .background(Color(0xFFF26122), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 0.dp))
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = message.text,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

            // Alt Mesaj Giriş Alanı
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Mesaj yazma kutusu
                    BasicTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(26.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(26.dp))
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (inputText.isNotBlank()) {
                                    sendMessage(inputText)
                                }
                            }
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = Color(0xFF1E293B),
                            fontSize = 13.sp
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (inputText.isEmpty()) {
                                    Text(
                                        text = "Mesajınızı yazın...",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 13.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Yuvarlak Turuncu Gönderme Butonu
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(2.dp, CircleShape)
                            .background(Color(0xFFF26122), CircleShape)
                            .clickable {
                                if (inputText.isNotBlank()) {
                                    sendMessage(inputText)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Gönder",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

// Üç hareketli nokta içeren "yazıyor..." animasyonu
@Composable
fun TypingIndicator() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        TypingIndicatorDot(delay = 0)
        TypingIndicatorDot(delay = 200)
        TypingIndicatorDot(delay = 400)
    }
}

@Composable
fun TypingIndicatorDot(delay: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val translationY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(delay)
        ),
        label = "dot"
    )

    Box(
        modifier = Modifier
            .size(8.dp)
            .graphicsLayer {
                this.translationY = translationY
            }
            .clip(CircleShape)
            .background(Color(0xFF94A3B8))
    )
}


