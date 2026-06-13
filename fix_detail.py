import sys

file_path = r'C:\SuperApp\TimasSuperApp\app\src\main\java\com\timas\superapp\screens\BookDetailScreen.kt'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

old_sig = '''@Composable
private fun DetailTileItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {'''

new_sig = '''@Composable
private fun DetailTileItem(
    label: String,
    value: String,
    icon: ImageVector,
    textColor: Color,
    subtitleColor: Color,
    modifier: Modifier = Modifier
) {'''

content = content.replace(old_sig, new_sig)

old_usage1 = '''DetailTileItem(
                    label = "Sayfa",
                    value = "${stats.pages} s.",
                    icon = Icons.Default.Description,
                    modifier = Modifier.weight(1f)
                )'''

new_usage1 = '''DetailTileItem(
                    label = "Sayfa",
                    value = "${stats.pages} s.",
                    icon = Icons.Default.Description,
                    textColor = textColor,
                    subtitleColor = subtitleColor,
                    modifier = Modifier.weight(1f)
                )'''

old_usage2 = '''DetailTileItem(
                    label = "Süre",
                    value = stats.audioDuration,
                    icon = Icons.Default.Headphones,
                    modifier = Modifier.weight(1f)
                )'''

new_usage2 = '''DetailTileItem(
                    label = "Süre",
                    value = stats.audioDuration,
                    icon = Icons.Default.Headphones,
                    textColor = textColor,
                    subtitleColor = subtitleColor,
                    modifier = Modifier.weight(1f)
                )'''

old_usage3 = '''DetailTileItem(
                    label = "Yıl",
                    value = stats.year,
                    icon = Icons.Default.CalendarToday,
                    modifier = Modifier.weight(1f)
                )'''

new_usage3 = '''DetailTileItem(
                    label = "Yıl",
                    value = stats.year,
                    icon = Icons.Default.CalendarToday,
                    textColor = textColor,
                    subtitleColor = subtitleColor,
                    modifier = Modifier.weight(1f)
                )'''

old_usage4 = '''DetailTileItem(
                    label = "Dil",
                    value = "Türkçe",
                    icon = Icons.Default.Language,
                    modifier = Modifier.weight(1f)
                )'''

new_usage4 = '''DetailTileItem(
                    label = "Dil",
                    value = "Türkçe",
                    icon = Icons.Default.Language,
                    textColor = textColor,
                    subtitleColor = subtitleColor,
                    modifier = Modifier.weight(1f)
                )'''

content = content.replace(old_usage1, new_usage1)
content = content.replace(old_usage2, new_usage2)
content = content.replace(old_usage3, new_usage3)
content = content.replace(old_usage4, new_usage4)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

print("Fixes applied.")
