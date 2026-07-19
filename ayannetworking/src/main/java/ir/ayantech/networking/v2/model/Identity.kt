package ir.ayantech.networking.v2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Identity(
    @SerialName("Token")
    var token: String?
)
