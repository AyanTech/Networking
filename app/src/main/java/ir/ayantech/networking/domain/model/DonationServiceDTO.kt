package ir.ayantech.networking.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class DonationServiceDTO {

    @Serializable
    data class Input(
        val id: Int = 1
    )

    @Serializable
    data class Output(
        @SerialName("ReferrerTypeList")
        val referrerTypeList: List<ReferrerType>
    )

    @Serializable
    data class ReferrerType(
        @SerialName("Name")
        val name: String,
        @SerialName("ShowName")
        val showName: String

    )
}