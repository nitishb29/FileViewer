package com.nb.fileviewer

import android.content.Context
import android.net.Uri
import org.apache.poi.ss.extractor.ExcelExtractor
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.extractor.XSSFExcelExtractor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.extractor.ExcelExtractor as HSSFExcelExtractor

fun readExcelFile(context: Context, fileUri: Uri): List<List<String>> {
    val sheetData = mutableListOf<List<String>>()

    try {
        context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
            // WorkbookFactory is used to detect and open the workbook
            WorkbookFactory.create(inputStream).use { workbook ->
                // Using an Extractor is often safer on Android to avoid AWT-related layout code
                val extractor = when (workbook) {
                    is HSSFWorkbook -> HSSFExcelExtractor(workbook)
                    is XSSFWorkbook -> XSSFExcelExtractor(workbook)
                    else -> null
                }

                if (extractor != null) {
                    val text = extractor.text
                    // Simple parsing of the extracted text into rows for the UI
                    text.split("\n").forEach { line ->
                        if (line.isNotBlank()) {
                            sheetData.add(line.split("\t").map { it.trim() })
                        }
                    }
                } else {
                    // Fallback to manual iteration if extractor fails
                    val sheet = workbook.getSheetAt(0)
                    for (row in sheet) {
                        val rowData = mutableListOf<String>()
                        for (cell in row) {
                            rowData.add(cell.toString())
                        }
                        sheetData.add(rowData)
                    }
                }
            }
        }
    } catch (t: Throwable) {
        t.printStackTrace()
        sheetData.add(listOf("Error reading Excel: ${t.localizedMessage ?: "Missing system libraries (java.awt)"}"))
    }
    return sheetData
}
