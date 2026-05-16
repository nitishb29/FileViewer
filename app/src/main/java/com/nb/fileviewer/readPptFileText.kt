package com.nb.fileviewer

import android.content.Context
import android.net.Uri
import org.apache.poi.extractor.ExtractorFactory

/**
 * Robustly reads text from both .ppt (HSLF) and .pptx (XSLF) files.
 * Catches Throwable to prevent crashes from missing java.awt classes on Android.
 */
fun readPptFileText(context: Context, fileUri: Uri): List<String> {
    try {
        context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
            // ExtractorFactory is the entry point for text extraction.
            // We catch Throwable because it might throw NoClassDefFoundError (java.awt).
            val extractor = try {
                ExtractorFactory.createExtractor(inputStream)
            } catch (t: Throwable) {
                t.printStackTrace()
                return listOf("Error: This PowerPoint format requires system libraries (java.awt) not available on Android.")
            }

            extractor.use { ext ->
                val fullText = try {
                    ext.text
                } catch (t: Throwable) {
                    t.printStackTrace()
                    "Error: Could not extract text from this presentation due to missing system classes."
                }

                if (!fullText.isNullOrBlank()) {
                    // Form feed (\u000c) is the standard slide separator in POI extractors
                    return fullText.split("\u000c")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return listOf("Error: ${e.localizedMessage ?: "Failed to open file"}")
    } catch (t: Throwable) {
        // Final safety net for Errors like NoClassDefFoundError
        t.printStackTrace()
        return listOf("Critical Error: Missing system components to read this file.")
    }

    return listOf("[No text content found]")
}
