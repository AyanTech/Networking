package ir.ayantech.networking.v2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Status(
    @SerialName("Code")
    val code: String?,
    @SerialName("Description")
    val description: String?,
    @SerialName("Hint")
    val hint: String?,
    @SerialName("IsFromCache")
    val isFromCache: Boolean?,
    @SerialName("Retryable")
    val retryable: Boolean?,
    @SerialName("Type")
    val type: String?
)
