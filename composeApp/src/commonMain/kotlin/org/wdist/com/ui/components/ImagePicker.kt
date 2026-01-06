package org.wdist.com.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePicker(onImageSelected: (ByteArray?) -> Unit): () -> Unit