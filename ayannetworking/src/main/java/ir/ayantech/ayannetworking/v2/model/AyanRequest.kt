package ir.ayantech.ayannetworking.v2.model

import com.google.gson.annotations.SerializedName

data class AyanRequest<T>(
    @SerializedName("Identity")
    var identity: Any?,
    @SerializedName("Parameters")
    var parameters: T?
)
