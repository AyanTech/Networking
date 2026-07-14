package ir.ayantech.networking.data.source

import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.domain.model.DonationServiceDTO
import kotlinx.coroutines.flow.Flow

interface GetDonationRemoteDataSource {

    suspend fun fetchDonation(input: DonationServiceDTO.Input):
            Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Failure>>
}