package org.wdist.com

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.Bitmap
import org.wdist.com.networking.ApiService
import org.wdist.com.networking.MainViewModel
import org.wdist.com.ui.components.rememberImagePicker
import org.wdist.com.ui.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        val coroutineScope = rememberCoroutineScope()
        val viewModel = MainViewModel(ApiService(), coroutineScope)
        val state = viewModel.state
        val imagePicker = rememberImagePicker(onImageSelected = viewModel::onImageSelected)

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.image != null) {
                Image(
                    bitmap = remember(state.image) { Bitmap.makeFromImage(org.jetbrains.skia.Image.makeFromEncoded(state.image)).asImageBitmap() },
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(onClick = imagePicker) {
                Text("Select Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = viewModel::getResponse, enabled = state.image != null && !state.isLoading) {
                Text("Get Response")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Text(state.error, color = MaterialTheme.colorScheme.error)
            } else if (state.response != null) {
                Text(state.response.toString())
            }
        }
    }
}