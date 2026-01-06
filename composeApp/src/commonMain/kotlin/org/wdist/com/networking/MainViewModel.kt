package org.wdist.com.networking

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainViewModel(private val apiService: ApiService, private val coroutineScope: CoroutineScope) {

    var state by mutableStateOf(AIState())
        private set


    fun getResponse() {
        coroutineScope.launch {
            state = state.copy(isLoading = true)
            try {
                val response = apiService.getResponse(state.image!!)
                state = state.copy(isLoading = false, response = response)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun onImageSelected(image: ByteArray?) {
        state = state.copy(image = image)
    }
}