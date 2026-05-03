package com.nb.fileviewer


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FileViewerHomeScreen(innerPadding: PaddingValues, isDarkThemeEnabled: Boolean) {
    val invertMatrix = ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 255f, // Red
            0f, -1f, 0f, 0f, 255f, // Green
            0f, 0f, -1f, 0f, 255f, // Blue
            0f, 0f, 0f, 1f, 0f     // Alpha
        )
    )
    val documentIcons = hashMapOf<String, Int>()
    documentIcons["pdf"] = R.drawable.doc_pdf_icon
    documentIcons["word"] = R.drawable.doc_word_icon
    documentIcons["ppt"] = R.drawable.doc_ppt_icon
    documentIcons["excel"] = R.drawable.doc_excel_icon
    val openedDocumentDetails = remember { mutableStateListOf<FileHistoryDetails>() }
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Card(
                        shape = RoundedCornerShape(25.dp),
                        elevation = CardDefaults.elevatedCardElevation(15.dp),
                        modifier = Modifier.padding(15.dp)
                    ) {
                        IconButton(
                            onClick = {
                                openedDocumentDetails.add(
                                    FileHistoryDetails(
                                        "PDF_File",
                                        java.time.LocalDateTime.now(),
                                        documentIcons["pdf"]
                                    )
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(100.dp)
                        ) {
                            Image(
                                painter = painterResource(documentIcons.getValue("pdf")),
                                contentDescription = "PDF Document Icon",
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = invertedColorIcons(isDarkThemeEnabled, invertMatrix)
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(25.dp),
                        elevation = CardDefaults.elevatedCardElevation(15.dp),
                        modifier = Modifier.padding(15.dp)
                    ) {
                        IconButton(
                            onClick = {
                                openedDocumentDetails.add(
                                    FileHistoryDetails(
                                        "Word_File",
                                        java.time.LocalDateTime.now(),
                                        documentIcons["word"]
                                    )
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(100.dp)
                        ) {
                            Image(
                                painter = painterResource(documentIcons.getValue("word")),
                                contentDescription = "Word Document Icon",
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = invertedColorIcons(isDarkThemeEnabled, invertMatrix)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Card(
                        shape = RoundedCornerShape(25.dp),
                        elevation = CardDefaults.elevatedCardElevation(15.dp),
                        modifier = Modifier.padding(15.dp)
                    ) {
                        IconButton(
                            onClick = {
                                openedDocumentDetails.add(
                                    FileHistoryDetails(
                                        "Excel_File",
                                        java.time.LocalDateTime.now(),
                                        documentIcons["excel"]
                                    )
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(100.dp)
                        ) {
                            Image(
                                painter = painterResource(documentIcons.getValue("excel")),
                                contentDescription = "Excel Document Icon",
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = invertedColorIcons(isDarkThemeEnabled, invertMatrix)
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(25.dp),
                        elevation = CardDefaults.elevatedCardElevation(15.dp),
                        modifier = Modifier.padding(15.dp)
                    ) {
                        IconButton(
                            onClick = {
                                openedDocumentDetails.add(
                                    FileHistoryDetails(
                                        "PPT_File",
                                        java.time.LocalDateTime.now(),
                                        documentIcons.getValue("ppt")
                                    )
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(100.dp)
                        ) {
                            Image(
                                painter = painterResource(documentIcons.getValue("ppt")),
                                contentDescription = "PPT Document Icon",
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = invertedColorIcons(isDarkThemeEnabled, invertMatrix)
                            )
                        }
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "History", fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.padding(2.dp))
        if (!openedDocumentDetails.isEmpty()) {
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(openedDocumentDetails.size) { index ->
                        Card(
                            shape = RoundedCornerShape(5.dp),
                            elevation = CardDefaults.elevatedCardElevation(15.dp),
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxSize(),
                            border = BorderStroke(1.dp, Color.DarkGray.copy(0.3f))
                        ) {
                            Row {
                                Column {
                                    Image(
                                        painter = painterResource(
                                            openedDocumentDetails[index].iconID!!
                                        ), contentDescription = "fileIcon",
                                        colorFilter = invertedColorIcons(
                                            isDarkThemeEnabled,
                                            invertMatrix
                                        ),
                                        modifier = Modifier
                                            .size(60.dp)
                                            .padding(10.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = openedDocumentDetails[index].fileName,
                                        fontSize = 25.sp,
                                        color = if (isDarkThemeEnabled) {
                                            Color.LightGray
                                        } else {
                                            Color.Black
                                        },
                                        modifier = Modifier.padding(2.dp)
                                    )
                                    Text(
                                        text = openedDocumentDetails[index].timeOpened.format(
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                        ),
                                        color = if (isDarkThemeEnabled) {
                                            Color.Gray
                                        } else {
                                            Color.DarkGray
                                        },
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.End,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class FileHistoryDetails(
    val fileName: String,
    val timeOpened: java.time.LocalDateTime,
    val iconID: Int?

)

fun invertedColorIcons(isDarkThemeEnabled: Boolean, invertMatrix: ColorMatrix): ColorFilter? {
    return if (isDarkThemeEnabled) {
        ColorFilter.colorMatrix(invertMatrix)
    } else {
        null
    }
}