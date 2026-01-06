package org.wdist.com.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement

@Composable
actual fun rememberImagePicker(onImageSelected: (ByteArray?) -> Unit): () -> Unit {
    return remember {
        {
            val input = document.createElement("input") as HTMLInputElement
            input.type = "file"
            input.accept = "image/*"
            input.onchange = {
                val file = input.files?.get(0)
                file?.let {
                    val reader = org.w3c.files.FileReader()
                    reader.onload = {
                        val result = reader.result.asDynamic()
                        val byteArray = js("new Uint8Array(result)") as ByteArray
                        onImageSelected(byteArray)
                    }
                    reader.readAsArrayBuffer(it)
                }
            }
            input.click()
        }
    }
}