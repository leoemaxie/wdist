package org.wdist.com.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun rememberImagePicker(onImageSelected: (ByteArray?) -> Unit): () -> Unit {
    return remember {
        {
            val dialog = FileDialog(null as Frame?, "Select File", FileDialog.LOAD)
            dialog.isVisible = true
            val file = dialog.file
            val dir = dialog.directory
            if (file != null && dir != null) {
                onImageSelected(File(dir, file).readBytes())
            }
        }
    }
}