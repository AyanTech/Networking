package ir.ayantech.ayannetworking.v2.model

import com.google.gson.annotations.SerializedName

data class AyanRequest<T>(
    @SerializedName("Identity")
    var identity: String?,
    @SerializedName("Parameters")
    var parameters: T?
)