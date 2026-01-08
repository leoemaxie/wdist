package org.wdist.com

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AIResponse(
    @SerialName("content_type")
    val contentType: ContentType,
    val intent: IntentType,
    val action: ActionType,
    @SerialName("action_details")
    val actionDetails: ActionDetails,
)

@Serializable
data class ActionDetails(
    val title: String? = null,
    val datetime: String? = null,
    val note: String? = null,
)

@Serializable
enum class ContentType {
    @SerialName("screenshot")
    SCREENSHOT,
    @SerialName("text")
    TEXT,
    @SerialName("other")
    OTHER,
}

@Serializable
enum class IntentType {
    @SerialName("reminder")
    REMINDER,
    @SerialName("calendar_event")
    CALENDAR_EVENT,
    @SerialName("note")
    NOTE,
    @SerialName("delete")
    DELETE,
    @SerialName("unknown")
    UNKNOWN,
}

@Serializable
enum class ActionType {
    @SerialName("add_to_calendar")
    ADD_TO_CALENDAR,
    @SerialName("set_reminder")
    SET_REMINDER,
    @SerialName("save_as_note")
    SAVE_AS_NOTE,
    @SerialName("delete")
    DELETE,
    @SerialName("none")
    NONE,
}