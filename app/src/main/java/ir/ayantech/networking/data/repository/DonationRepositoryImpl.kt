package ir.ayantech.networking.data.repository

import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.domain.model.DonationServiceDTO
import ir.ayantech.networking.data.source.GetDonationRemoteDataSource
import ir.ayantech.networking.domain.repository.DonationRepository
import kotlinx.coroutines.flow.Flow

class DonationRepositoryImpl(private val repository: GetDonationRemoteDataSource) : DonationRepository {

    override suspend fun fetchDonation(input: DonationServiceDTO.Input):
            Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Failure>> {
        return repository.fetchDonation(input)
    }
}