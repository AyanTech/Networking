package ir.ayantech.networking.domain.model

data class DonationServiceUIModel(val referrerTypeList: List<ReferrerType>)

data class ReferrerType(
    val name: String,
    val showName: String

)

