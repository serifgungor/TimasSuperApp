package com.timas.superapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class CurvedBottomNavShape : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height
            val curveRadius = density.run { 36.dp.toPx() }
            val dropDepth = density.run { 36.dp.toPx() }

            moveTo(0f, 0f)
            lineTo(width / 2 - curveRadius * 1.5f, 0f)
            cubicTo(
                width / 2 - curveRadius, 0f,
                width / 2 - curveRadius * 0.8f, dropDepth,
                width / 2, dropDepth
            )
            cubicTo(
                width / 2 + curveRadius * 0.8f, dropDepth,
                width / 2 + curveRadius, 0f,
                width / 2 + curveRadius * 1.5f, 0f
            )
            lineTo(width, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun SuperAppBottomNav(
    selectedIndex: Int = 0,
    onTabSelected: (Int) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onQrClick: () -> Unit = {}   // ← YENİ
) {
    val bottomNavColor = Color(0xFF1E293B)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        androidx.compose.material3.Surface(
            modifier = Modifier.fillMaxWidth(),
            color = bottomNavColor,
            shape = CurvedBottomNavShape(),
            shadowElevation = 8.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Home,
                            label = "Anasayfa",
                            isSelected = selectedIndex == 0,
                            onClick = { onTabSelected(0); onHomeClick() }
                        )
                        BottomNavItem(
                            icon = Icons.Default.Search,
                            label = "Arama",
                            isSelected = selectedIndex == 1,
                            onClick = { onTabSelected(1); onSearchClick() }
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.5f))

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.ShoppingCart,
                            label = "Sepetim",
                            isSelected = selectedIndex == 2,
                            onClick = { onTabSelected(2); onCartClick() }
                        )
                        BottomNavItem(
                            icon = Icons.Default.Menu,
                            label = "Menü",
                            isSelected = selectedIndex == 3,
                            onClick = { onTabSelected(3); onMenuClick() }
                        )
                    }
                }

                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }

        // QR FAB — onQrClick bağlandı
        FloatingActionButton(
            onClick = onQrClick,   // ← DEĞİŞTİ
            shape = CircleShape,
            containerColor = Color(0xFFF26122),
            contentColor = Color.White,
            modifier = Modifier.offset(y = (-40).dp)
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = "QR Oku",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(32.dp)
        )
    }
}
