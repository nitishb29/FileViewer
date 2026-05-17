package com.nb.fileviewer

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileViewerTopBar(scrollBehavior: TopAppBarScrollBehavior, premiumDarkGradient: Brush) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                ""
            )
        },
        actions = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0E0801),          // Idle state background
            scrolledContainerColor = Color.Transparent.copy(0.5f),  // Background color while user is actively scrolling
            titleContentColor = Color.White.copy(0.7f)
        ),
        scrollBehavior = scrollBehavior
    )
}