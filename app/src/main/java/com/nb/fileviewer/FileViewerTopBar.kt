package com.nb.fileviewer

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileViewerTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    val fileName = "AIO File Reader"
    CenterAlignedTopAppBar(
        title = {
            Text(
                fileName,
                fontWeight = FontWeight.SemiBold,
                color =  Color.White.copy(0.7f),
                fontSize = 40.sp
            )
        },
        actions = {},
        // FIX: Explicitly set every scroll state container color slot to transparent
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,          // Idle state background
            scrolledContainerColor = Color.Transparent.copy(0.5f),  // Background color while user is actively scrolling
            titleContentColor = Color.White.copy(0.7f)
        ),
        scrollBehavior = scrollBehavior
    )
}