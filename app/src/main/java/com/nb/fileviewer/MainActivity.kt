package com.nb.fileviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(0),
            navigationBarStyle = SystemBarStyle.dark(0)
        )

        // Check if the activity was started with a file URI (Intent.ACTION_VIEW)
        val incomingUri = intent?.data
        val incomingType = incomingUri?.let { uri ->
            val mimeType = contentResolver.getType(uri) ?: ""
            when {
                mimeType.contains("pdf") -> "pdf"
                mimeType.contains("word") || mimeType.contains("msword") || mimeType.contains("officedocument.wordprocessingml") -> "word"
                mimeType.contains("excel") || mimeType.contains("spreadsheet") || mimeType.contains("officedocument.spreadsheetml") -> "excel"
                mimeType.contains("ppt") || mimeType.contains("powerpoint") || mimeType.contains("officedocument.presentationml") -> "ppt"
                else -> null
            }
        }

        setContent {
            var currentViewingUri by rememberSaveable { mutableStateOf(incomingUri) }
            var currentFileType by rememberSaveable { mutableStateOf(incomingType) }

            val darkBackgroundBase = Color(0xFF0D0E10)  // Deep near-black for the bottom/base
            val darkBackgroundMid = Color(0xFF16181C)   // Muted dark gray for middle transitions
            val darkBackgroundTop = Color(0xFF22252A)
            val premiumDarkGradient = Brush.linearGradient(
                colors = listOf(
                    darkBackgroundTop,
                    darkBackgroundMid,
                    darkBackgroundBase
                ),
                start = Offset(Float.POSITIVE_INFINITY, 0f), // Top Right
                end = Offset(0f, Float.POSITIVE_INFINITY)    // Bottom Left
            )
            // 1. Initialize the correct scroll contract behavior state at the root level
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            //FileViewerTheme {
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = { FileViewerTopBar(scrollBehavior) },
                    containerColor = Color.Transparent
                ) { innerPadding ->
                    if (currentViewingUri != null && currentFileType != null) {
                        when (currentFileType) {
                            "pdf" -> {
                                InternalPdfViewerScreen(
                                    fileUri = currentViewingUri!!,
                                    onBackPress = {
                                        currentViewingUri = null
                                        currentFileType = null
                                    },
                                    innerPadding
                                )
                            }
                            "word" -> {
                                InternalWordViewerScreen(
                                    fileUri = currentViewingUri!!,
                                    innerPadding = innerPadding,
                                    onBackPress = {
                                        currentViewingUri = null
                                        currentFileType = null
                                    }
                                )
                            }
                            "excel" -> {
                                InternalExcelViewerScreen(
                                    fileUri = currentViewingUri!!,
                                    innerPadding = innerPadding,
                                    onBackPress = {
                                        currentViewingUri = null
                                        currentFileType = null
                                    }
                                )
                            }
                            "ppt" ->{
                                InternalPptViewerScreen(
                                    fileUri = currentViewingUri!!,
                                    innerPadding = innerPadding,
                                    onBackPress = {
                                        currentViewingUri = null
                                        currentFileType = null
                                    }
                                )
                            }
                        }
                    } else {
                        // Pass both URI and Type update selections back from the home screen grid click
                        FileViewerHomeScreen(innerPadding, premiumDarkGradient) { uri ->
                            val mimeType = contentResolver.getType(uri) ?: ""
                            currentViewingUri = uri
                            currentFileType = when {
                                mimeType.contains("pdf") -> "pdf"
                                mimeType.contains("word") || mimeType.contains("msword") || mimeType.contains("officedocument.wordprocessingml") -> "word"
                                mimeType.contains("excel") || mimeType.contains("spreadsheet") || mimeType.contains("officedocument.spreadsheetml") -> "excel"
                                mimeType.contains("ppt") || mimeType.contains("powerpoint") || mimeType.contains("officedocument.presentationml") -> "ppt"
                                else -> "pdf" // Default fallback
                            }
                        }
                    }
                }

            }
        }
   // }
}