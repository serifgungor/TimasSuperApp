package com.timas.superapp.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ActionType {
    WEB_VIEW, NATIVE, GOOGLE_PLAY, CUSTOM_PAGE
}

data class QuickApp(
    val title: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBgColor: Color,
    val actionType: ActionType,
    val actionData: String = ""
)

@Composable
fun QuickAppsSection() {
    val context = LocalContext.current
    
    val quickApps = listOf(
        QuickApp("Kitap Satış", Icons.Default.ShoppingBag, Color(0xFFD84315), Color(0xFFFBE9E7), ActionType.WEB_VIEW),
        QuickApp("Timaş Okul", Icons.Default.School, Color(0xFF1565C0), Color(0xFFE3F2FD), ActionType.WEB_VIEW),
        QuickApp("Timaş Portal", Icons.Default.Public, Color(0xFF4527A0), Color(0xFFEDE7F6), ActionType.WEB_VIEW),
        QuickApp("Bayilik B2B", Icons.Default.Work, Color(0xFF6A1B9A), Color(0xFFF3E5F5), ActionType.WEB_VIEW),
        QuickApp("Kütüphanem", Icons.Default.MenuBook, Color(0xFF00695C), Color(0xFFE0F2F1), ActionType.NATIVE),
        QuickApp("Timaş Dijital", Icons.Default.Computer, Color(0xFF2E7D32), Color(0xFFE8F5E9), ActionType.NATIVE),
        QuickApp("Timaş Çocuk", Icons.Default.ChildCare, Color(0xFFEF6C00), Color(0xFFFFF3E0), ActionType.GOOGLE_PLAY),
        QuickApp("ZEKii", Icons.Default.Psychology, Color(0xFFC2185B), Color(0xFFFCE4EC), ActionType.WEB_VIEW),
        QuickApp("Okuma Kulübü", Icons.Default.LocalLibrary, Color(0xFF00838F), Color(0xFFE0F7FA), ActionType.NATIVE),
        QuickApp("E-Book", Icons.Default.Book, Color(0xFFD84315), Color(0xFFFBE9E7), ActionType.CUSTOM_PAGE),
        QuickApp("Sesli Kitap", Icons.Default.Headphones, Color(0xFF4527A0), Color(0xFFEDE7F6), ActionType.CUSTOM_PAGE)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F9FA))
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Hızlı Uygulamalar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 12.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(quickApps) { app ->
                QuickAppCard(app = app) {
                    val message = "Aksiyon: ${app.actionType} -> ${app.title}"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun QuickAppCard(app: QuickApp, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(88.dp)
            .height(108.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = app.iconBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = app.icon,
                contentDescription = app.title,
                tint = app.iconTint,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = app.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155),
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                maxLines = 2
            )
        }
    }
}
