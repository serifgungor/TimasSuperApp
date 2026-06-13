import sys

file_path = r'C:\SuperApp\TimasSuperApp\app\src\main\java\com\timas\superapp\screens\BookDetailScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Change topBarColor
content = content.replace('val topBarColor = if (isLightMode) Color(0xFFF26122) else Color(0xFF261D1A)', 'val topBarColor = if (isLightMode) Color(0xFFFFF0E5) else Color(0xFF261D1A)')

# Change back button color in TopAppBar
content = content.replace('Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White)', 'Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri", tint = textColor)')

# Ensure TopAppBar title text is textColor
content = content.replace('''fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 17.sp''', '''fontWeight = FontWeight.ExtraBold,
                        color = textColor,
                        fontSize = 17.sp''')

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

print('Done.')
