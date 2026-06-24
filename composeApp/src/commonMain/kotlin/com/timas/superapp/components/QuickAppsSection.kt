package com.timas.superapp.components

import com.timas.superapp.openUrl

import com.timas.superapp.showToast

import timas.composeapp.generated.resources.Res
import timas.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

import coil3.compose.LocalPlatformContext

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import com.timas.superapp.Library3DScreen

fun Modifier.dropShadow(
    color: Color = Color.Black.copy(alpha = 0.1f),
    borderRadius: Dp = 16.dp,
    blurRadius: Dp = 10.dp,
    offsetY: Dp = 6.dp,
    offsetX: Dp = 4.dp,
    spread: Dp = 0.dp
) = this.shadow(
    elevation = blurRadius,
    shape = RoundedCornerShape(borderRadius),
    clip = false
)

enum class ActionType {
    WEB_VIEW, NATIVE, GOOGLE_PLAY, CUSTOM_PAGE
}

data class QuickApp(
    val title: String,
    val imageRes: org.jetbrains.compose.resources.DrawableResource,
    val actionType: ActionType,
    val actionData: String = ""
)

@Composable
fun QuickAppsSection(
    onNavigateToZekii: () -> Unit = {},
    onNavigateToSesliKitap: () -> Unit = {},
    onNavigateToEBook: () -> Unit = {},
    onNavigateToOkumaKulubu: () -> Unit = {}
) {
    val context = LocalPlatformContext.current
    var selectedApp by remember { mutableStateOf<QuickApp?>(null) }
    var showLibrary by remember { mutableStateOf(false) }
    
    val quickApps = listOf(
        QuickApp("Kitap Satış", Res.drawable.resim1, ActionType.WEB_VIEW, "https://timas.com.tr"),
        QuickApp("Timaş Okul", Res.drawable.resim2, ActionType.WEB_VIEW, "https://www.timasokul.com"),
        QuickApp("Timaş Portal", Res.drawable.resim3, ActionType.WEB_VIEW, "https://portal.timas.com.tr"),
        QuickApp("Bayilik B2B", Res.drawable.resim4, ActionType.WEB_VIEW, "https://timasdagitim.com/"),
        QuickApp("Kütüphanem", Res.drawable.resim5, ActionType.NATIVE),
        QuickApp("Timaş Çocuk", Res.drawable.resim12, ActionType.GOOGLE_PLAY, "https://play.google.com/store/apps/details?id=com.ageofkids.timascocuk.app&pcampaignid=web_share"),
        QuickApp("ZEKii", Res.drawable.resim11, ActionType.NATIVE),
        QuickApp("Okuma Kulübü", Res.drawable.resim10, ActionType.NATIVE),
        QuickApp("E-Book", Res.drawable.resim9, ActionType.CUSTOM_PAGE),
        QuickApp("Sesli Kitap", Res.drawable.resim8, ActionType.CUSTOM_PAGE)
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
                    val titleLower = app.title.lowercase()
                    val isZekii = titleLower.contains("zek")
                    val isSesliKitap = titleLower.contains("sesli")
                    val isEBook = titleLower.contains("book")
                    val isOkumaKulubu = titleLower.contains("okuma")
                    
                    if (app.actionType == ActionType.WEB_VIEW) {
                        try {
                            openUrl(app.actionData)
                        } catch (e: Exception) {
                            selectedApp = app
                        }
                    } else if (app.title == "Kütüphanem") {
                        showLibrary = true
                    } else if (isZekii) {
                        onNavigateToZekii()
                    } else if (isSesliKitap) {
                        onNavigateToSesliKitap()
                    } else if (isEBook) {
                        onNavigateToEBook()
                    } else if (isOkumaKulubu) {
                        onNavigateToOkumaKulubu()
                    } else if (app.actionType == ActionType.GOOGLE_PLAY && app.actionData.isNotEmpty()) {
                        try {
                            openUrl(app.actionData)
                        } catch (e: Exception) {
                            openUrl(app.actionData)
                        }
                    } else {
                        showToast("${app.title} (Action: ${app.actionType})")
                    }
                }
            }
        }
    }

    // WebView dialog
    selectedApp?.let { app ->
        FullScreenWebViewDialog(
            title = app.title,
            url = app.actionData.ifEmpty { "https://www.google.com" },
            onDismiss = { selectedApp = null }
        )
    }

    // Library3D tam ekran dialog
    if (showLibrary) {
        Dialog(
            onDismissRequest = { showLibrary = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF18100C))
                    .systemBarsPadding()
            ) {
                // Üst bar - geri git butonu
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Color(0xFF261D1A))
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Geri",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { showLibrary = false },
                        tint = Color(0xFFFF9F43)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "📚 Okuma Kozası",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF1F5F9)
                    )
                }
                // İnce modern ayırıcı çizgi
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1E293B)))
                // Library ekranı
                Box(modifier = Modifier.weight(1f)) {
                    Library3DScreen()
                }
            }
        }
    }
}

@Composable
fun QuickAppCard(app: QuickApp, onClick: () -> Unit) {
    Image(
        painter = painterResource(app.imageRes),
        contentDescription = app.title,
        modifier = Modifier
            .width(132.dp)
            .height(140.dp)
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


