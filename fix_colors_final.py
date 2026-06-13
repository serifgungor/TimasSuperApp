import sys

file_path = r'C:\SuperApp\TimasSuperApp\app\src\main\java\com\timas\superapp\screens\BookDetailScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Change surfaceColor to match EventDetailDialog top header (0xFFFFF0E5)
content = content.replace('Color(0xFFF1F5F9)', 'Color(0xFFFFF0E5)')

# Change subtitleColor to match EventDetailDialog (Color.Gray)
content = content.replace('Color(0xFF475569)', 'Color.Gray')

# Fix favorite button empty heart color
content = content.replace('tint = if (isLiked) Color(0xFFEF4444) else Color.White,', 'tint = if (isLiked) Color(0xFFEF4444) else textColor,')

# Fix "Tüm Yorumları Gör" text color
content = content.replace('Text("Tüm Yorumları Gör", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)', 'Text("Tüm Yorumları Gör", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)')

# Fix Similar book title text
content = content.replace('''Text(
                                    text = simBook.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )''', '''Text(
                                    text = simBook.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )''')

# Fix LocalCoverFallback text colors
content = content.replace('color = Color.White,', 'color = textColor,')
content = content.replace('color = Color.White.copy(alpha = 0.7f),', 'color = subtitleColor,')

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

print("Fixes applied.")
