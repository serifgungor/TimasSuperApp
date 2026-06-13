import sys

file_path = r'C:\SuperApp\TimasSuperApp\app\src\main\java\com\timas\superapp\screens\BookDetailScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Remove standard TopAppBar and Overview Row, and replace with Hero Image Structure
old_scaffold_start = '''    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kitap Detayı",
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor,
                        fontSize = 17.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor)
            )
        },
        containerColor = bgColor // Matching dark premium theme
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // 1. OVERVIEW ROW (COVER & BASIC INFO)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Book Cover Container
                Box(
                    modifier = Modifier
                        .size(130.dp, 190.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(12.dp, RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                ) {
                    SubcomposeAsyncImage(
                        model = currentBook.coverUrl,
                        contentDescription = currentBook.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loading = { LocalCoverFallback(book = currentBook) },
                        error = { LocalCoverFallback(book = currentBook) }
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Metadata details
                Column(modifier = Modifier.weight(1f)) {
                    // Type Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFF9F43).copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = currentBook.type,
                            color = Color(0xFFFF9F43),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentBook.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = currentBook.author,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = subtitleColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating stars
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB900), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${stats.rating} / 5.0",
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(140 Yorum)",
                            color = Color(0xFF64748B),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))'''


new_scaffold_start = '''    Scaffold(
        containerColor = bgColor // Matching dark premium theme
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. HERO IMAGE (COVER & BASIC INFO)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                SubcomposeAsyncImage(
                    model = currentBook.coverUrl,
                    contentDescription = currentBook.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = { LocalCoverFallback(book = currentBook) },
                    error = { LocalCoverFallback(book = currentBook) }
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                                startY = 200f
                            )
                        )
                )

                // Back button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.4f), androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White)
                }

                // Metadata details over image
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    // Type Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFF9F43).copy(alpha = 0.85f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = currentBook.type,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentBook.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 30.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentBook.author,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Rating stars
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB900), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${stats.rating} / 5.0",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(140 Yorum)",
                            color = Color(0xFFCBD5E1),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Rest of the content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {'''

content = content.replace(old_scaffold_start, new_scaffold_start)

# We must close the extra Column at the bottom
old_end = '''        }
    }
}'''

new_end = '''            }
        }
    }
}'''

# Replace the last occurrence of old_end
parts = content.rsplit(old_end, 1)
content = new_end.join(parts)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

print("BookDetailScreen refactored")
