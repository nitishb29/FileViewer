package com.nb.fileviewer


import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FileViewerHomeScreen(
    innerPadding: PaddingValues,
    premiumDarkGradient: Brush,
    onFileSelected: (Uri) -> Unit
) {
    val documentIcons = hashMapOf<String, Int>()
    documentIcons["pdf"] = R.drawable.aio_pdf_icon
    documentIcons["word"] = R.drawable.aio_word_icon
    documentIcons["ppt"] = R.drawable.aio_ppt_icon
    documentIcons["excel"] = R.drawable.aio_xls_icon

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
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
            .padding(innerPadding)
            .fillMaxSize()
            .background(premiumDarkGradient),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val glassColor = Color.White.copy(alpha = 0.4f)
        val darkGlassColor = Color.Black.copy(0.4f)
        var isPdfButtonClicked by remember { mutableStateOf(false) }
        var isWordButtonClicked by remember { mutableStateOf(false) }
        var isExcelButtonClicked by remember { mutableStateOf(false) }
        var isPptButtonClicked by remember { mutableStateOf(false) }

        // 2. Automatically animate the corner size based on the click state
        val pdfAnimatedCornerRadius = animateDpAsState(
            targetValue = if (isPdfButtonClicked) 70.dp else 24.dp, // Morphs between 24dp and 8dp
            animationSpec = tween(durationMillis = 300) // 300ms smooth transition duration
        )
        val wordAnimatedCornerRadius = animateDpAsState(
            targetValue = if (isWordButtonClicked) 70.dp else 24.dp, // Morphs between 24dp and 8dp
            animationSpec = tween(durationMillis = 300) // 300ms smooth transition duration
        )
        val excelAnimatedCornerRadius = animateDpAsState(
            targetValue = if (isExcelButtonClicked) 70.dp else 24.dp, // Morphs between 24dp and 8dp
            animationSpec = tween(durationMillis = 300) // 300ms smooth transition duration
        )
        val pptAnimatedCornerRadius = animateDpAsState(
            targetValue = if (isPptButtonClicked) 70.dp else 24.dp, // Morphs between 24dp and 8dp
            animationSpec = tween(durationMillis = 300) // 300ms smooth transition duration
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                //.border(BorderStroke(2.dp, glassBorder))
                .background(glassColor.copy(0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .blur(16.dp)
            )
            Text(
                "AIO File Reader",
                fontWeight = FontWeight.SemiBold,
                fontSize = 35.sp,
                color = Color.White.copy(0.6f),
                modifier = Modifier.padding(10.dp)
            )
        }
        Box(
            modifier = Modifier
                .padding(20.dp)
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(glassColor.copy(0.1f)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        onClick = {
                            if (!isPdfButtonClicked) {
                                coroutineScope.launch {
                                    isPdfButtonClicked = true
                                    delay(150)
                                    isPdfButtonClicked = false
                                    delay(50)
                                    filePickerLauncher.launch(
                                        arrayOf("application/pdf")
                                    )
                                }
                            }


                        },
                        modifier = Modifier.size(135.dp),
                        colors = CardDefaults.cardColors(darkGlassColor.copy(0.2f)),
                        border = ButtonDefaults.outlinedButtonBorder(true),
                        shape = RoundedCornerShape(pdfAnimatedCornerRadius.value)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(documentIcons.getValue("pdf")),
                                contentDescription = "PDF",
                                modifier = Modifier.size(80.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Text(
                                "Open Word",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(0.6f)
                            )
                        }
                    }
                    Card(
                        onClick = {
                            if (!isWordButtonClicked) {
                                coroutineScope.launch {
                                    isWordButtonClicked = true
                                    delay(150)
                                    isWordButtonClicked = false
                                    delay(50)
                                    //file Picker code
                                    filePickerLauncher.launch(
                                        arrayOf(
                                            "application/msword",
                                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier.size(135.dp),
                        colors = CardDefaults.cardColors(darkGlassColor.copy(0.2f)),
                        border = ButtonDefaults.outlinedButtonBorder(true),
                        shape = RoundedCornerShape(wordAnimatedCornerRadius.value)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(documentIcons.getValue("word")),
                                contentDescription = "Word",
                                modifier = Modifier.size(80.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Text(
                                "Open Word",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(0.6f)
                            )
                        }
                    }

                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Card(
                        onClick = {
                            if (!isExcelButtonClicked) {
                                coroutineScope.launch {
                                    isExcelButtonClicked = true
                                    delay(150)
                                    isExcelButtonClicked = false
                                    delay(50)
                                    //file Picker code
                                    filePickerLauncher.launch(
                                        arrayOf(
                                            "application/vnd.ms-excel",
                                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                                        )
                                    )
                                }

                            }

                        },
                        modifier = Modifier.size(135.dp),
                        colors = CardDefaults.cardColors(darkGlassColor.copy(0.2f)),
                        border = ButtonDefaults.outlinedButtonBorder(true),
                        shape = RoundedCornerShape(excelAnimatedCornerRadius.value)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(documentIcons.getValue("excel")),
                                contentDescription = "Excel",
                                modifier = Modifier.size(80.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Text(
                                "Open Excel",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(0.6f)
                            )
                        }
                    }
                    Card(
                        onClick = {
                            if (!isPptButtonClicked) {
                                coroutineScope.launch {
                                    isPptButtonClicked = true
                                    delay(150)
                                    isPptButtonClicked = false
                                    delay(50)
                                    //file Picker code
                                    filePickerLauncher.launch(
                                        arrayOf(
                                            "application/vnd.ms-powerpoint",
                                            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                                        )
                                    )
                                }

                            }

                        },
                        modifier = Modifier.size(135.dp),
                        colors = CardDefaults.cardColors(darkGlassColor.copy(0.2f)),
                        border = ButtonDefaults.outlinedButtonBorder(true),
                        shape = RoundedCornerShape(pptAnimatedCornerRadius.value)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(documentIcons.getValue("ppt")),
                                contentDescription = "PPT",
                                modifier = Modifier.size(80.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.padding(2.dp))
                            Text(
                                "Open PPT",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(0.6f)
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(glassColor.copy(0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .blur(16.dp)
            )
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        "Icons Credits:-",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default,
                        fontSize = 25.sp,
                        color = Color.White.copy(0.60f),
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        "Roman Káčerek",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        fontSize = 30.sp,
                        color = Color.White.copy(0.80f),
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}