package com.nb.fileviewer

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun InternalPdfViewerScreen(fileUri: Uri, onBackPress: () -> Unit, innerPadding: PaddingValues) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    BackHandler { onBackPress() }

    var pdfReader by remember { mutableStateOf<OpenPDFFile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val pagesList = remember { mutableStateListOf<Bitmap>() }

    // --- ZOOM STATES ---
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
        // Constrain scaling bounds between 1x and 4x zoom
        scale = (scale * zoomChange).coerceIn(1f, 4f)

        // Only allow panning offsets when zoomed in
        if (scale > 1f) {
            offsetX += offsetChange.x * scale
            offsetY += offsetChange.y * scale
        } else {
            offsetX = 0f
            offsetY = 0f
        }
    }

    LaunchedEffect(fileUri) {
        withContext(Dispatchers.IO) {
            try {
                val reader = OpenPDFFile(context, fileUri)
                pdfReader = reader
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

    DisposableEffect(Unit) {
        onDispose {
            pdfReader?.close()
            pagesList.clear()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1B1F)) // Neutral off-black backdrop background
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (pagesList.isEmpty()) {
            Text("Failed to render PDF file.", color = Color.White, modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    // Apply real-time hardware graphic layer transformations
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
                    // Connect gestures listener to transformable state handler
                    .transformable(state = transformState),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pagesList.size) { index ->
                    Card (
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            bitmap = pagesList[index].asImageBitmap(),
                            contentDescription = "Page ${index + 1}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                    }
                }
            }
        }
    }
}