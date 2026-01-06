package org.wdist.com.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.wdist.com.models.AIResponse

class ApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getResponse(image: ByteArray): AIResponse {
        val response = client.post("http://127.0.0.1:5001/get-response") {
            contentType(ContentType.Image.JPEG)
            setBody(image)
        }
        return response.body()
    }
}