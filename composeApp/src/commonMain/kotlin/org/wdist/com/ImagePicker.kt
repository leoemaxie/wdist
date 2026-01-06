package org.wdist.com

import androidx.compose.runtime.Composable

@Composable
expect fun ImagePicker(
    show: Boolean,
    onImageSelected: (ByteArray?) -> Unit
)
