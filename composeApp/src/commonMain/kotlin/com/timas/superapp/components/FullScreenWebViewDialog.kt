package com.timas.superapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.timas.superapp.openUrl

@Composable
fun FullScreenWebViewDialog(
    title: String,
    url: String,
    onDismiss: () -> Unit
) {
    LaunchedEffect(url) {
        if (url.isNotEmpty()) {
            openUrl(url)
            onDismiss()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = Color(0xFFF26122),
                    modifier = Modifier.size(64.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Dış Bağlantı Açılıyor",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "\"$title\" adresi yeni sekmede açılacaktır. Eğer açılmadıysa aşağıdaki butona tıklayabilirsiniz.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { openUrl(url) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF26122))
                ) {
                    Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Yeni Sekmede Aç")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(onClick = onDismiss) {
                    Text("Kapat", color = Color.Gray)
                }
            }
        }
    }
}
