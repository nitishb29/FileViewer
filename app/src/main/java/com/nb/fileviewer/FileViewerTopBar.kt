package com.nb.fileviewer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileViewerTopBar(isDarkThemeEnabled: Boolean) {
    val fileName = "AIO File Reader"
    val contentColor = if (isDarkThemeEnabled) {
        Color.White
    } else {
        Color.Black
    }
    val containerColor = if (isDarkThemeEnabled) {
        MaterialTheme.colorScheme.background
    } else {
        MaterialTheme.colorScheme.background
    }
    TopAppBar(
        title = {
            Text(
                fileName,
                fontWeight = FontWeight.SemiBold
            )
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = if (isDarkThemeEnabled) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor
        )
    )
}