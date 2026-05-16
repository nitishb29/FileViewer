package com.nb.fileviewer


import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FileViewerHomeScreen(innerPadding: PaddingValues, isDarkThemeEnabled: Boolean, onFileSelected: (Uri) -> Unit) {
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
    val context = LocalContext.current
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if(null != uri) {
                try {
                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val mimeType = context.contentResolver.getType(uri) ?: ""
                if (mimeType != "") {
                    onFileSelected(uri)
                }
            }
        }
    )
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
                                filePickerLauncher.launch(
                                    arrayOf("application/pdf")
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(150.dp)
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
                                filePickerLauncher.launch(
                                    arrayOf(
                                        "application/msword",
                                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                    )
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(150.dp)
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
                                filePickerLauncher.launch(
                                    arrayOf(
                                        "application/vnd.ms-excel",
                                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                                    )
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(150.dp)
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
                                filePickerLauncher.launch(
                                    arrayOf(
                                        "application/vnd.ms-powerpoint",
                                        "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                                    )
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(150.dp)
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
    }
}

fun invertedColorIcons(isDarkThemeEnabled: Boolean, invertMatrix: ColorMatrix): ColorFilter? {
    return if (isDarkThemeEnabled) {
        ColorFilter.colorMatrix(invertMatrix)
    } else {
        null
    }
}
