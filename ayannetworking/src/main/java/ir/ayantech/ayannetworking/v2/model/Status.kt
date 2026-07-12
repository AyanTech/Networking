package ir.ayantech.ayannetworking.v2.model

import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("Code")
    val code: String?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("Hint")
    val hint: String?,
    @SerializedName("IsFromCache")
    val isFromCache: Boolean?,
    @SerializedName("Retryable")
    val retryable: Boolean?,
    @SerializedName("Type")
    val type: String?
)
