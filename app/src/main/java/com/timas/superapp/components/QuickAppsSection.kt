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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.timas.superapp.R

fun Modifier.dropShadow(
    color: Color = Color.Black.copy(alpha = 0.1f),
    borderRadius: Dp = 16.dp,
    blurRadius: Dp = 10.dp,
    offsetY: Dp = 6.dp,
    offsetX: Dp = 4.dp,
    spread: Dp = 0.dp
) = this.drawBehind {
    this.drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        val spreadPixel = spread.toPx()
        val leftPixel = offsetX.toPx() - spreadPixel
        val topPixel = offsetY.toPx() - spreadPixel
        val rightPixel = this.size.width + spreadPixel + offsetX.toPx()
        val bottomPixel = this.size.height + spreadPixel + offsetY.toPx()

        if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter = android.graphics.BlurMaskFilter(blurRadius.toPx(), android.graphics.BlurMaskFilter.Blur.NORMAL)
        }

        frameworkPaint.color = color.toArgb()
        
        canvas.nativeCanvas.drawRoundRect(
            leftPixel,
            topPixel,
            rightPixel,
            bottomPixel,
            borderRadius.toPx(),
            borderRadius.toPx(),
            frameworkPaint
        )
    }
}

enum class ActionType {
    WEB_VIEW, NATIVE, GOOGLE_PLAY, CUSTOM_PAGE
}

data class QuickApp(
    val title: String,
    val imageRes: Int,
    val actionType: ActionType,
    val actionData: String = ""
)

@Composable
fun QuickAppsSection() {
    val context = LocalContext.current
    var selectedApp by remember { mutableStateOf<QuickApp?>(null) }
    
    val quickApps = listOf(
        QuickApp("Kitap Satış", R.drawable.resim1, ActionType.WEB_VIEW, "https://www.timasyayingrubu.com"), // LİNKİ BURADAN DEĞİŞTİREBİLİRSİNİZ
        QuickApp("Timaş Okul", R.drawable.resim2, ActionType.WEB_VIEW, "https://www.timasokul.com"), // LİNKİ BURADAN DEĞİŞTİREBİLİRSİNİZ
        QuickApp("Timaş Portal", R.drawable.resim3, ActionType.WEB_VIEW, "https://portal.timas.com.tr"), // LİNKİ BURADAN DEĞİŞTİREBİLİRSİNİZ
        QuickApp("Bayilik B2B", R.drawable.resim4, ActionType.WEB_VIEW, "https://bayi.timas.com.tr"), // LİNKİ BURADAN DEĞİŞTİREBİLİRSİNİZ
        QuickApp("Kütüphanem", R.drawable.resim5, ActionType.NATIVE),
        QuickApp("Timaş Dijital", R.drawable.resim6, ActionType.NATIVE),
        QuickApp("Timaş Çocuk", R.drawable.resim7, ActionType.GOOGLE_PLAY),
        QuickApp("ZEKii", R.drawable.resim8, ActionType.WEB_VIEW, "https://www.zekii.com.tr"), // LİNKİ BURADAN DEĞİŞTİREBİLİRSİNİZ
        QuickApp("Okuma Kulübü", R.drawable.resim9, ActionType.NATIVE),
        QuickApp("E-Book", R.drawable.resim10, ActionType.CUSTOM_PAGE),
        QuickApp("Sesli Kitap", R.drawable.resim11, ActionType.CUSTOM_PAGE)
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
                    if (app.actionType == ActionType.WEB_VIEW) {
                        selectedApp = app
                    } else {
                        Toast.makeText(context, "${app.title} (Action: ${app.actionType})", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    selectedApp?.let { app ->
        FullScreenWebViewDialog(
            title = app.title,
            url = app.actionData.ifEmpty { "https://www.google.com" },
            onDismiss = { selectedApp = null }
        )
    }
}

@Composable
fun QuickAppCard(app: QuickApp, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = app.imageRes),
        contentDescription = app.title,
        modifier = Modifier
            .width(116.dp)
            .height(124.dp)
            .dropShadow(
                color = Color.Black.copy(alpha = 0.15f),
                borderRadius = 16.dp,
                blurRadius = 10.dp,
                offsetY = 4.dp,
                offsetX = 2.dp
            )
            .clickable(onClick = onClick),
        contentScale = ContentScale.Fit
    )
}
