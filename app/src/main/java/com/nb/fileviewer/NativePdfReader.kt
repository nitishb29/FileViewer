package com.nb.fileviewer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.FileOutputStream

class NativePdfReader(context: Context, uri: Uri) {
    private var fileDescriptor: ParcelFileDescriptor? = null
    private var pdfRenderer: PdfRenderer? = null

    init {
        // Copy the secure URI content stream into a temporary cache file
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "current_viewed_doc.pdf")
        FileOutputStream(tempFile).use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        // Open the file descriptor required by PdfRenderer
        fileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
        fileDescriptor?.let {
            pdfRenderer = PdfRenderer(it)
        }
    }

    val pageCount: Int get() = pdfRenderer?.pageCount ?: 0

    fun renderPageToBitmap(pageIndex: Int): Bitmap? {
        val renderer = pdfRenderer ?: return null
        if (pageIndex < 0 || pageIndex >= renderer.pageCount) return null

        val page = renderer.openPage(pageIndex)
        // High resolution sizing matching device viewport scales
        val bitmap = Bitmap.createBitmap(page.width * 2, page.height * 2, Bitmap.Config.ARGB_8888)

        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()
        return bitmap
    }

    fun close() {
        pdfRenderer?.close()
        fileDescriptor?.close()
    }
}