package com.nb.fileviewer


import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FileViewerHomeScreen(
    innerPadding: PaddingValues,
    premiumDarkGradient: Brush,
    onFileSelected: (Uri) -> Unit
) {
    val documentIcons = hashMapOf<String, Int>()
    documentIcons["pdf"] = R.drawable.aio_pdf_button_icon
    documentIcons["word"] = R.drawable.aio_word_button_icon
    documentIcons["ppt"] = R.drawable.aio_ppt_button_icon
    documentIcons["excel"] = R.drawable.aio_excel_button_icon

    //Gradient Content Color
    val iconGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFE52B50), // Soft vibrant mint
            Color(0xFF4DB6AC)  // Muted clean teal
        )
    )
    val context = LocalContext.current
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (null != uri) {
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
            .background(Color.Black)
            .padding(innerPadding)
            .padding(10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // The border stroke color to give it that crisp definition
        val cardBorderColor = Color(0xFF2C2C2E).copy(alpha = 0.05f)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
        ) {
            Card(
                modifier = Modifier
                    .background(premiumDarkGradient)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .size(120.dp)
                    .clickable(enabled = true, onClick = {
                        filePickerLauncher.launch(
                        arrayOf("application/pdf")
                    )}),
                elevation = CardDefaults.elevatedCardElevation(20.dp),
                border = BorderStroke(1.dp, cardBorderColor),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White.copy(0.85f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .graphicsLayer(alpha = 0.99f) // 2. Crucial: Isolates the blending layer
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent() // 3. Draws the standard white/default icon outline

                                // 4. Blends your gradient brush strictly over the icon's pixels
                                drawRect(
                                    brush = iconGradient,
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            painter = painterResource(documentIcons.getValue("pdf")),
                            contentDescription = "PDF",
                            modifier = Modifier.size(100.dp),
                            tint = Color.Unspecified
                        )
                    }
                    Column(modifier = Modifier.align(Alignment.CenterVertically).padding(10.dp))
                    {
                        Text(
                            text = "Open PDF",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            fontSize = 40.sp,
                            color = Color.White.copy(1f)
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .background(premiumDarkGradient, RoundedCornerShape(25.dp))
                    .fillMaxWidth()
                    .padding(10.dp)
                    .size(120.dp)
                    .clickable(enabled = true, onClick = {
                        filePickerLauncher.launch(
                            arrayOf(
                                "application/msword",
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                            )
                        )
                    }),
                elevation = CardDefaults.elevatedCardElevation(20.dp),
                border = BorderStroke(1.dp, cardBorderColor),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFE52B50)
                )
            ) {
                Row (modifier = Modifier
                    .graphicsLayer(alpha = 0.99f) // 2. Crucial: Isolates the blending layer
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent() // 3. Draws the standard white/default icon outline

                            // 4. Blends your gradient brush strictly over the icon's pixels
                            drawRect(
                                brush = iconGradient,
                                blendMode = BlendMode.SrcAtop
                            )
                        }
                    }){
                    Column(modifier = Modifier.padding(10.dp)
                    ) {
                        Icon(
                            painter = painterResource(documentIcons.getValue("word")),
                            contentDescription = "Word",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    Column(modifier = Modifier.align(Alignment.CenterVertically).padding(10.dp)) {
                        Text(
                            text = "Word",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            fontSize = 40.sp
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .background(premiumDarkGradient, RoundedCornerShape(25.dp))
                    .fillMaxWidth()
                    .padding(10.dp)
                    .size(120.dp)
                    .clickable(enabled = true, onClick = {
                        filePickerLauncher.launch(
                            arrayOf(
                                "application/vnd.ms-excel",
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            )
                        )
                    }),
                elevation = CardDefaults.elevatedCardElevation(20.dp),
                border = BorderStroke(1.dp, cardBorderColor),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFE52B50)
                )
            ) {

                Row(
                    modifier = Modifier.graphicsLayer(alpha = 0.99f) // 2. Crucial: Isolates the blending layer
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent() // 3. Draws the standard white/default icon outline

                                // 4. Blends your gradient brush strictly over the icon's pixels
                                drawRect(
                                    brush = iconGradient,
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            painter = painterResource(documentIcons.getValue("excel")),
                            contentDescription = "Excel",
                            modifier = Modifier.size(100.dp)
                                .graphicsLayer(alpha = 0.99f) // 2. Crucial: Isolates the blending layer
                                .drawWithCache {
                                    onDrawWithContent {
                                        drawContent() // 3. Draws the standard white/default icon outline

                                        // 4. Blends your gradient brush strictly over the icon's pixels
                                        drawRect(
                                            brush = iconGradient,
                                            blendMode = BlendMode.SrcAtop
                                        )
                                    }
                                },
                            tint = Color.Unspecified
                        )
                    }
                    Column(
                        modifier = Modifier.align(Alignment.CenterVertically).padding(10.dp)
                    ) {
                        Text(
                            text = "Open Excel",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            fontSize = 40.sp
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .background(premiumDarkGradient, RoundedCornerShape(25.dp))
                    .fillMaxWidth()
                    .padding(10.dp)
                    .size(120.dp)
                    .clickable(enabled = false, onClick = {
                        filePickerLauncher.launch(
                            arrayOf(
                                "application/vnd.ms-powerpoint",
                                "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                            )
                        )
                    }),
                elevation = CardDefaults.elevatedCardElevation(20.dp),
                border = BorderStroke(1.dp, cardBorderColor),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.DarkGray
                )
            ) {
                Row(
                    modifier = Modifier
                        /*.graphicsLayer(alpha = 0.99f) // 2. Crucial: Isolates the blending layer
                        .drawWithCache() {
                            onDrawWithContent {
                                drawContent() // 3. Draws the standard white/default icon outline

                                // 4. Blends your gradient brush strictly over the icon's pixels
                                drawRect(
                                    brush = iconGradient,
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }*/
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            painter = painterResource(documentIcons.getValue("ppt")),
                            contentDescription = "PPT",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    Column(modifier = Modifier.align(Alignment.CenterVertically).padding(10.dp)) {
                        Text(
                            text = "PPT",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            fontSize = 40.sp
                        )
                        Text(
                            text = "Work in Progress!!",
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Default,
                            fontSize = 25.sp,
                            color = Color.White.copy(0.4f)
                        )
                    }
                }
            }
        }
    }
}

/*
* Row (
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
                    Card (
                        shape = RoundedCornerShape(25.dp),
                        elevation = CardDefaults.elevatedCardElevation(15.dp),
                        modifier = Modifier.padding(15.dp)
                    ) {
                        IconButton (
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
                                colorFilter = invertedColorIcons(true, invertMatrix)
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
                                colorFilter = invertedColorIcons(true, invertMatrix)
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
                                colorFilter = invertedColorIcons(true, invertMatrix)
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
                                colorFilter = invertedColorIcons(true, invertMatrix)
                            )
                        }
                    }
                }
            }
        }*/