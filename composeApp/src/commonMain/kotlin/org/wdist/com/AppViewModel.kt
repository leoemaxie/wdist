package org.wdist.com

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse(val label: String, val score: Float)

class AppViewModel : ViewModel() {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    var state by mutableStateOf(UiState())
        private set

    fun onImageSelected(bytes: ByteArray) {
        state = state.copy(image = bytes)
    }

    fun onSend() {
        state.image?.let { image ->
            viewModelScope.launch {
                state = state.copy(isLoading = true, error = null)
                try {
                    val response = httpClient.post("http://127.0.0.1:5001/get-response") {
                        contentType(ContentType.Image.Any)
                        setBody(image)
                    }.body<ServerResponse>()
                    state = state.copy(isLoading = false, result = response.label)
                } catch (e: Exception) {
                    state = state.copy(isLoading = false, error = e.message)
                }
            }
        }
    }
}


data class UiState(
    val image: ByteArray? = null,
    val isLoading: Boolean = false,
    val result: String? = null,
    val error: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UiState

        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (isLoading != other.isLoading) return false
        if (result != other.result) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = image?.contentHashCode() ?: 0
        result1 = 31 * result1 + isLoading.hashCode()
        result1 = 31 * result1 + (result ?: 0).hashCode()
        result1 = 31 * result1 + (error ?: 0).hashCode()
        return result1
    }
}