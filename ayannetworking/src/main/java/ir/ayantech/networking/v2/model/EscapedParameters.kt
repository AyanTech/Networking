package ir.ayantech.networking.v2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EscapedParameters(
    @SerialName("Params")
    val params: String,
    @SerialName("MethodName")
    var methodName: String? = null
)
