package ir.ayantech.networking.data.source

import ir.ayantech.ayannetworking.v2.AyanApi
import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.data.APIs
import ir.ayantech.networking.domain.model.DonationServiceDTO
import kotlinx.coroutines.flow.Flow

class GetDonationRemoteDataSourceImpl(private val ayanApi: AyanApi) : GetDonationRemoteDataSource {
    override suspend fun fetchDonation(input: DonationServiceDTO.Input):
            Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Failure>> {
        return ayanApi.post<DonationServiceDTO.Input, DonationServiceDTO.Output>(
            body = input,
            endPint = APIs.DONATION_SERVICE,
        )
    }
}