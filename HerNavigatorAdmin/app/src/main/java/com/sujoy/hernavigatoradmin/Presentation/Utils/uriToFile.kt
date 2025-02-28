package com.sujoy.hernavigatoradmin.Presentation.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

// Convert URI to File (Handles New Android Versions)
fun uriToFile(context: Context, uri: Uri): File? {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val bitmap = BitmapFactory.decodeStream(inputStream)

    val compressedFile = File(context.cacheDir, "compressed_upload.jpg")
    val outputStream = FileOutputStream(compressedFile)

    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)  // Compress to 80% quality
    outputStream.flush()
    outputStream.close()

    return compressedFile
}
