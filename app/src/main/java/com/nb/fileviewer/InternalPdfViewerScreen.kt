package com.nb.fileviewer

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun InternalPdfViewerScreen(fileUri: Uri, onBackPress: () -> Unit) {
    val context = LocalContext.current

    // Handle system back button
    BackHandler {
        onBackPress()
    }

    var pdfReader by remember { mutableStateOf<NativePdfReader?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val pagesList = remember { mutableStateListOf<Bitmap>() }

    // Safely load file on a background coroutine thread
    LaunchedEffect(fileUri) {
        withContext(Dispatchers.IO) {
            try {
                val reader = NativePdfReader(context, fileUri)
                pdfReader = reader

                // Render pages sequentially into memory bitmaps
                for (i in 0 until reader.pageCount) {
                    reader.renderPageToBitmap(i)?.let { pagesList.add(it) }
                }
                isLoading = false
            } catch (e: Exception) {
                e.printStackTrace()
                isLoading = false
            }
        }
    }

    // Clean up memory space when user closes screen
    DisposableEffect(Unit) {
        onDispose {
            pdfReader?.close()
            pagesList.clear()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (pagesList.isEmpty()) {
            Text("Failed to render PDF file.", color = Color.White, modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pagesList.size) { index ->
                    Image(
                        bitmap = pagesList[index].asImageBitmap(),
                        contentDescription = "Page ${index + 1}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(Color.White)
                    )
                }
            }
        }
    }
}