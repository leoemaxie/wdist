package org.wdist.com.model

import kotlinx.serialization.Serializable

@Serializable
data class ScreenShot(
    val id: String,
    val url: String,
    val description: String,
    val timestamp: Long,
)