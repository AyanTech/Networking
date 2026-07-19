package ir.ayantech.networking.v2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AyanResponse<T>(
    @SerialName("Parameters")
    var parameters: T?,
    @SerialName("Status")
    var status: Status
)
