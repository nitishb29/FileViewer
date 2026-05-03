package com.nb.fileviewer

import android.content.Context
import android.net.Uri

fun determineFileType(context : Context, uri: Uri) : String{
    if(null != context && null != uri) {
        val parsedFile = context.contentResolver.getType(uri)
        return when {
            parsedFile == "application/pdf" -> "PDF"
            parsedFile?.contains("spreadsheet" ) == true -> "Sheet"
            parsedFile?.contains("word") == true -> "Word"
            else -> "Un"
        }
    }
    return ""
}