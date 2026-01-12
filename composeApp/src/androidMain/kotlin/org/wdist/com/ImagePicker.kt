package org.wdist.com

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun ImagePicker(
    show: Boolean,
    onImageSelected: (ByteArray?) -> Unit
) {
    if (!show) return
    
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                context.contentResolver.openInputStream(it)?.use {
                    onImageSelected(it.readBytes())
                }
            }
        }
    )
    
    remember {
        launcher.launch("image/*")
    }
}
