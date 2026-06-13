import sys

file_path = r'C:\SuperApp\TimasSuperApp\app\src\main\java\com\timas\superapp\screens\BookDetailScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Fix the tab unselected text color to be darker (use textColor for all, or maybe textColor.copy(alpha=0.6f))
# The current is `color = if (selectedTabState == index) textColor else Color(0xFF64748B)`
# Let's change Color(0xFF64748B) to textColor.copy(alpha = 0.6f)
content = content.replace('color = if (selectedTabState == index) textColor else Color(0xFF64748B)',
                          'color = if (selectedTabState == index) textColor else textColor.copy(alpha = 0.6f)')

# 2. Fix preface text color (Color(0xFFE2E8F0) to textColor)
content = content.replace('color = Color(0xFFE2E8F0) // This is very light', 'color = textColor')
# There might be another instance without the comment
content = content.replace('color = Color(0xFFE2E8F0)', 'color = textColor')

# 3. Fix button text and icon colors to Color.White
content = content.replace('tint = textColor, modifier = Modifier.size(18.dp)', 'tint = Color.White, modifier = Modifier.size(18.dp)')
content = content.replace('color = textColor)', 'color = Color.White)')
content = content.replace('color = textColor\n', 'color = Color.White\n')
# Wait, let's be more precise for the buttons:
# E-Kitap Button
content = content.replace('Text("E-Kitap", fontWeight = FontWeight.Bold, color = textColor)',
                          'Text("E-Kitap", fontWeight = FontWeight.Bold, color = Color.White)')
# Sesli Dinle Button
content = content.replace('Text("Sesli Dinle", fontWeight = FontWeight.Bold, color = textColor)',
                          'Text("Sesli Dinle", fontWeight = FontWeight.Bold, color = Color.White)')

# 4. Fix card backgrounds (Color(0xFF261D1A) to cardBgColor)
# First define cardBgColor next to bgColor
content = content.replace('val textColor = if (isLightMode) Color(0xFF1E293B) else Color(0xFFF1F5F9)',
                          'val textColor = if (isLightMode) Color(0xFF1E293B) else Color(0xFFF1F5F9)\n    val cardBgColor = if (isLightMode) Color(0xFFFFF0E5) else Color(0xFF261D1A)')

content = content.replace('CardDefaults.cardColors(containerColor = Color(0xFF261D1A))',
                          'CardDefaults.cardColors(containerColor = cardBgColor)')

# There's also the Yorumlar author text which might be dark:
# Text(text = review.userName, color = Color(0xFF3B82F6)...
# The review text itself: Text(text = review.comment, color = Color(0xFFE2E8F0)...
content = content.replace('Text(text = review.comment, fontSize = 12.sp, color = Color(0xFFE2E8F0),',
                          'Text(text = review.comment, fontSize = 12.sp, color = textColor,')

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

print("Colors updated successfully")
