package ir.ayantech.networking.data.source

import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.domain.model.DonationServiceDTO
import ir.ayantech.networking.domain.model.GetDonationRequestData
import kotlinx.coroutines.flow.Flow

interface DonationRemoteDataSource {
    suspend fun fetchDonation(data: GetDonationRequestData):
            Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Exception>>
}