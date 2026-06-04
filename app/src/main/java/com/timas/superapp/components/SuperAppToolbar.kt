package com.timas.superapp.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.timas.superapp.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SuperAppToolbar(
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Side: Logo + Expandable Search Bar
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Super App Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(40.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Center: Search Bar (Animated)
                var isSearchActive by remember { mutableStateOf(searchQuery.isNotEmpty()) }
                val searchModifier = if (isSearchActive) Modifier.weight(1f) else Modifier.wrapContentWidth()
                val focusRequester = remember { FocusRequester() }

                LaunchedEffect(isSearchActive) {
                    if (isSearchActive) {
                        focusRequester.requestFocus()
                    }
                }

                Box(
                    modifier = searchModifier
                        .fillMaxHeight(0.75f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { isSearchActive = true }
                        .animateContentSize()
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        
                        if (isSearchActive) {
                            var hasFocus by remember { mutableStateOf(false) }

                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = onSearchQueryChange,
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequester)
                                    .onFocusChanged {
                                        if (it.isFocused) {
                                            hasFocus = true
                                        } else if (hasFocus) {
                                            isSearchActive = false
                                            hasFocus = false
                                        }
                                    },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Light
                                ),
                                decorationBox = { innerTextField ->
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Ara...",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Light,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                            if (searchQuery.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Temizle",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            onSearchQueryChange("")
                                            isSearchActive = false
                                        }
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Arama",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right: Notification and Profile Icons
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuperAppToolbarPreview() {
    MaterialTheme {
        SuperAppToolbar()
    }
}
