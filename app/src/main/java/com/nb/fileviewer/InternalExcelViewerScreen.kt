package com.nb.fileviewer

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun InternalExcelViewerScreen(fileUri: Uri, innerPadding: PaddingValues, onBackPress: () -> Unit) {
    val context = LocalContext.current

    // Handle system back button
    BackHandler {
        onBackPress()
    }

    var spreadsheetRows by remember { mutableStateOf<List<List<String>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(fileUri) {
        withContext(Dispatchers.IO) {
            val dataResult = readExcelFile(context, fileUri)
            withContext(Dispatchers.Main) {
                spreadsheetRows = dataResult
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            // Horizontal scroll container in case sheets are wide
            Box(modifier = Modifier.fillMaxSize().horizontalScroll(rememberScrollState())) {
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(spreadsheetRows) { rowCells ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            rowCells.forEach { cellText ->
                                Text(
                                    text = cellText,
                                    modifier = Modifier
                                        .width(120.dp)
                                        .padding(4.dp),
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}