package ir.ayantech.sample.data.source

import ir.ayantech.networking.v2.api.AyanAPIResult
import ir.ayantech.networking.v2.model.ApiCallStatus
import ir.ayantech.sample.domain.model.DonationServiceDTO
import ir.ayantech.sample.domain.model.GetDonationRequestData
import kotlinx.coroutines.flow.Flow

interface DonationRemoteDataSource {
    suspend fun fetchDonation(data: GetDonationRequestData):
            Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Exception>>
}