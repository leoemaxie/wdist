package org.wdist.com

import androidx.compose.ui.graphics.ImageBitmap

expect fun toImageBitmap(bytes: ByteArray): ImageBitmap
