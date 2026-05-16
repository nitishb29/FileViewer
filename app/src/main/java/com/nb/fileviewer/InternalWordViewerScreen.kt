package com.nb.fileviewer

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun InternalWordViewerScreen(fileUri: Uri, innerPadding: PaddingValues, onBackPress: () -> Unit) {
    val context = LocalContext.current

    // Handle system back button
    BackHandler {
        onBackPress()
    }

    var parsedText by remember { mutableStateOf("Loading...") }
    var isLoading by remember { mutableStateOf(true) }

    // Execute the parsing operation asynchronously off the main thread
    LaunchedEffect(fileUri) {
        withContext(Dispatchers.IO) {
            val textResult = OpenWordFileText(context, fileUri)
            withContext(Dispatchers.Main) {
                parsedText = textResult
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = parsedText, fontSize = 16.sp)
            }
        }
    }
}