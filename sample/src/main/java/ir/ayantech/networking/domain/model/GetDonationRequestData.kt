package ir.ayantech.networking.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GetDonationRequestData(
    val id: String
)
