package ir.ayantech.networking.v2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AyanRequest<T>(
    @SerialName("Identity")
    var identity: Identity?,
    @SerialName("Parameters")
    var parameters: T?
)