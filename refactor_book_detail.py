import sys

file_path = r'C:\SuperApp\TimasSuperApp\app\src\main\java\com\timas\superapp\screens\BookDetailScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Add isLightMode to the parameters
old_sig = """fun BookDetailScreen(
    book: Book,
    onBack: () -> Unit,
    onStartReading: (Book) -> Unit = {},
    onStartListening: (Book) -> Unit = {}
) {"""
new_sig = """fun BookDetailScreen(
    book: Book,
    onBack: () -> Unit,
    onStartReading: (Book) -> Unit = {},
    onStartListening: (Book) -> Unit = {},
    isLightMode: Boolean = false
) {
    val bgColor = if (isLightMode) Color.White else Color(0xFF120C0A)
    val surfaceColor = if (isLightMode) Color(0xFFF1F5F9) else Color(0xFF261D1A)
    val textColor = if (isLightMode) Color(0xFF1E293B) else Color.White
    val subtitleColor = if (isLightMode) Color(0xFF475569) else Color(0xFF94A3B8)
    val topBarColor = if (isLightMode) Color(0xFFF26122) else Color(0xFF261D1A)
"""
content = content.replace(old_sig, new_sig)

# 2. Replace hardcoded colors with the new variables.

content = content.replace('containerColor = Color(0xFF120C0A)', 'containerColor = bgColor')
content = content.replace('colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF261D1A))', 'colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor)')
content = content.replace('color = Color.White,\n                        fontFamily = FontFamily.Serif', 'color = textColor,\n                        fontFamily = FontFamily.Serif')
content = content.replace('color = Color(0xFF94A3B8)', 'color = subtitleColor')

content = content.replace('text = "${stats.rating} / 5.0",\n                            color = Color.White', 'text = "${stats.rating} / 5.0",\n                            color = textColor')

content = content.replace('background(Color(0xFF261D1A))', 'background(surfaceColor)')
content = content.replace('background(Color(0xFF332520))', 'background(surfaceColor)')
content = content.replace('containerColor = Color(0xFF120C0A),', 'containerColor = bgColor,')
content = content.replace('unselectedContentColor = Color(0xFF64748B)', 'unselectedContentColor = subtitleColor')
content = content.replace('color = Color.White,\n                            lineHeight = 24.sp', 'color = textColor,\n                            lineHeight = 24.sp')
content = content.replace('color = Color.White,\n                            textAlign = TextAlign.Justify', 'color = textColor,\n                            textAlign = TextAlign.Justify')
content = content.replace('color = Color.White,\n                                    fontWeight = FontWeight.Bold', 'color = textColor,\n                                    fontWeight = FontWeight.Bold')

# Additionally for the reviews divider
content = content.replace('color = Color(0xFF261D1A)', 'color = surfaceColor')

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

print("Refactoring complete.")
