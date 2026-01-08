package org.wdist.com

import androidx.compose.runtime.Composable
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun ImagePicker(
    show: Boolean,
    onImageSelected: (ByteArray?) -> Unit
) {
    if (show) {
        val dialog = FileDialog(null as Frame?, "Select Image", FileDialog.LOAD)
        dialog.isVisible = true
        val file = dialog.file
        val dir = dialog.directory
        if (file != null && dir != null) {
            onImageSelected(File(dir, file).readBytes())
        } else {
            onImageSelected(null)
        }
    }
}
