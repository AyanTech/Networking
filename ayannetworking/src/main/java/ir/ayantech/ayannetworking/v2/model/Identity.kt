package ir.ayantech.ayannetworking.v2.model

import com.google.gson.annotations.SerializedName

data class Identity(
    @SerializedName("Token")
    var token: String?
)
