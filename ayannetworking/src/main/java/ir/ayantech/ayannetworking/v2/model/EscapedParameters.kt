package ir.ayantech.ayannetworking.v2.model

import com.google.gson.annotations.SerializedName

data class EscapedParameters(
    @SerializedName("Params")
    val params: String,
    @SerializedName("MethodName")
    var methodName: String? = null
)
