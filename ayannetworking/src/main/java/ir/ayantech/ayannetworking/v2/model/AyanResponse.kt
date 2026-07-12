package ir.ayantech.ayannetworking.v2.model

import com.google.gson.annotations.SerializedName
import ir.ayantech.ayannetworking.ayanModel.Status

data class AyanResponse<T>(
    @SerializedName("Parameters")
    var parameters: T?,
    @SerializedName("Status")
    var status: Status
)
