package com.nb.fileviewer

import android.content.Context
import android.net.Uri
import org.apache.poi.extractor.ExtractorFactory

/**
 * Robustly reads text from both .doc and .docx files.
 * Catches Throwable to prevent crashes from missing AWT classes.
 */
fun OpenWordFileText(context: Context, fileUri: Uri): String {
    try {
        context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
            val extractor = try {
                ExtractorFactory.createExtractor(inputStream)
            } catch (t: Throwable) {
                t.printStackTrace()
                return "Error: This Word format requires system libraries (java.awt) not available on Android."
            }

            extractor.use { ext ->
                return try {
                    ext.text ?: "[No text content found]"
                } catch (t: Throwable) {
                    t.printStackTrace()
                    "Error: Text extraction failed due to missing system classes."
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return "Error: ${e.localizedMessage ?: "Failed to open file"}"
    } catch (t: Throwable) {
        t.printStackTrace()
        return "Critical Error: Could not read Word document."
    }
    return "Error: Document could not be processed."
}
