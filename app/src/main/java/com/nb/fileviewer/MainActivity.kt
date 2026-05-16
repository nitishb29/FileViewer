package com.nb.fileviewer

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nb.fileviewer.ui.theme.FileViewerTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
            var currentViewingUri by rememberSaveable { mutableStateOf<Uri?>(incomingUri) }
            var currentFileType by rememberSaveable { mutableStateOf<String?>(incomingType) }
            FileViewerTheme {
                val isDarkThemeEnabled = isSystemInDarkTheme()
                Scaffold(
                    modifier = Modifier,
                    topBar = { FileViewerTopBar(isDarkThemeEnabled) }
                ) { innerPadding ->
                    if (currentViewingUri != null && currentFileType != null) {
                        when (currentFileType) {
                            "pdf" -> {
                                InternalPdfViewerScreen(
                                    fileUri = currentViewingUri!!,
                                    onBackPress = {
                                        currentViewingUri = null
                                        currentFileType = null
                                    }
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
                        FileViewerHomeScreen(innerPadding, isDarkThemeEnabled) { uri ->
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
    }
}