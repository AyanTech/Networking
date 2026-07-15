package ir.ayantech.networking.data.repository

import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.data.source.DonationRemoteDataSource
import ir.ayantech.networking.domain.model.DonationServiceDTO
import ir.ayantech.networking.domain.model.GetDonationRequestData
import ir.ayantech.networking.domain.repository.DonationRepository
import kotlinx.coroutines.flow.Flow

class DonationRepositoryImpl(private val remoteSource: DonationRemoteDataSource) :
    DonationRepository {

    override suspend fun fetchDonation(data: GetDonationRequestData):
            Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Exception>> {
        return remoteSource.fetchDonation(data)
    }
}