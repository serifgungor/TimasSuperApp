package com.timas.superapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton

class CurvedBottomNavShape : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height
            val curveRadius = density.run { 32.dp.toPx() } // Radius for the cutout
            val dropDepth = density.run { 24.dp.toPx() }   // Depth of the cutout
            
            moveTo(0f, 0f)
            lineTo(width / 2 - curveRadius * 1.5f, 0f)
            
            // Cubic bezier curve for a smooth cutout
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
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val bottomNavColor = Color(0xFF1E293B) // Dark slate blue
    val contentColor = Color.White
    
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        // We use a Surface with a custom shape to create the cutout look
        androidx.compose.material3.Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp), // Pushed down 32dp. FAB will overlap by 24dp
            color = bottomNavColor,
            shape = CurvedBottomNavShape(),
            shadowElevation = 8.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left items
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Home,
                            label = "Anasayfa",
                            contentColor = contentColor,
                            onClick = onHomeClick
                        )
                        BottomNavItem(
                            icon = Icons.Default.Search,
                            label = "Arama",
                            contentColor = contentColor,
                            onClick = onSearchClick
                        )
                    }
                    
                    // Spacer for the center FAB
                    Spacer(modifier = Modifier.weight(0.5f))
                    
                    // Right items
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.ShoppingCart,
                            label = "Sepetim",
                            contentColor = contentColor,
                            onClick = onCartClick
                        )
                        BottomNavItem(
                            icon = Icons.Default.Menu,
                            label = "Menü",
                            contentColor = contentColor,
                            onClick = onMenuClick
                        )
                    }
                }
                
                // This will extend the dark blue background behind the system navigation bar!
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
        
        // Floating Action Button perfectly placed in the cutout
        FloatingActionButton(
            onClick = { /* Handle QR scan */ },
            shape = CircleShape,
            containerColor = Color(0xFFF26122), // Orange color
            contentColor = Color.White,
            modifier = Modifier.padding(top = 0.dp) // Starts exactly at the top of the Box
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
    contentColor: Color,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = contentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
