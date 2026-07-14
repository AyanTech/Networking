package ir.ayantech.networking

import com.google.gson.annotations.SerializedName

class DonationServiceGerReferrerTypeList {

    class Input(
        val id: Int = 1
    )

    data class Output(
        @SerializedName("ReferrerTypeList")
        val referrerTypeList: List<ReferrerType>
    )

    class ReferrerType(
        @SerializedName("Name")
        val name: String,
        @SerializedName("ShowName")
        val showName: String

    )
}