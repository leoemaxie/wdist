package org.wdist.com.networking

import org.wdist.com.AIResponse

data class AIState(
    val isLoading: Boolean = false,
    val response: AIResponse? = null,
    val error: String? = null,
    val image: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AIState

        if (isLoading != other.isLoading) return false
        if (response != other.response) return false
        if (error != other.error) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isLoading.hashCode()
        result = 31 * result + (response?.hashCode() ?: 0)
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}