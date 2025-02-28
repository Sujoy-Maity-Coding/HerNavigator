package com.sujoy.hernavigatoradmin.Presentation.Utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

fun compressImage(context: Context, uri: Uri): File? {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    val file = File(context.cacheDir, "compressed_image.jpg")

    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)  // Reduce quality to 50%
    outputStream.flush()
    outputStream.close()

    return file
}
