package com.trust.ease_noting.classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.net.Uri
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile

class FileSaving(private val context: Context, private val activity: Activity) {

    // Function to initiate the directory selection process and save the text

    fun savePageToStorage(textToSave: String) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_DIRECTORY)

        // You can't directly save the file here, as you need the user to pick a directory first.
    }

    // Function to handle the result of directory selection and save the text

    fun handleDirectorySelectionResult(directoryUri: Uri, textToSave: String) {
        // Convert the directory URI to a DocumentFile
        val directory = DocumentFile.fromTreeUri(context, directoryUri)

        // Check if the selected directory is valid

        if (directory == null || !directory.isDirectory) {
            Toast.makeText(context, "Invalid directory", Toast.LENGTH_SHORT).show()
            return
        }

        // Define the file name and create the file within the selected directory

        val fileName = "page_content.txt"
        val file = directory.createFile("text/plain", fileName)

        // Open an output stream to write the text content to the file

        val fileUri = file?.uri ?: Uri.EMPTY // Provide a default Uri if file?.uri is null
        val fileOutputStream = context.contentResolver.openOutputStream(fileUri)

        try {
            // Write the text content to the file

            fileOutputStream?.write(textToSave.toByteArray())
            fileOutputStream?.close()
            Toast.makeText(context, "Page saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving the page", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REQUEST_CODE_PICK_DIRECTORY = 123
    }
}


