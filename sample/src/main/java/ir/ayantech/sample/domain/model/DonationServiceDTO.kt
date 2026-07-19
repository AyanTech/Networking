package ir.ayantech.sample.domain.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
class DonationServiceDTO {

    @Serializable
    data class Output(
        @SerialName("ReferrerTypeList")
        val referrerTypeList: List<ReferrerType>?
    )

    @Serializable
    data class ReferrerType(
        @SerialName("Name")
        val name: String,
        @SerialName("ShowName")
        val showName: String

    )
}