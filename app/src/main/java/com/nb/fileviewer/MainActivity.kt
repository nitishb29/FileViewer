package com.nb.fileviewer

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.nb.fileviewer.ui.theme.FileViewerTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FileViewerTheme {
                val isDarkThemeEnabled = isSystemInDarkTheme()
                Scaffold(
                    modifier = Modifier,
                    topBar = { FileViewerTopBar(isDarkThemeEnabled) }
                ) { innerPadding ->
                    FileViewerHomeScreen(innerPadding, isDarkThemeEnabled)
                }

            }
        }
    }
}