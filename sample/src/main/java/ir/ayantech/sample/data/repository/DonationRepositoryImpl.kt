package ir.ayantech.sample.data.repository

import ir.ayantech.networking.v2.api.AyanAPIResult
import ir.ayantech.networking.v2.model.ApiCallStatus
import ir.ayantech.sample.data.source.DonationRemoteDataSource
import ir.ayantech.sample.domain.model.DonationServiceDTO
import ir.ayantech.sample.domain.model.GetDonationRequestData
import ir.ayantech.sample.domain.repository.DonationRepository
import kotlinx.coroutines.flow.Flow

class DonationRepositoryImpl(private val remoteSource: DonationRemoteDataSource) :
    DonationRepository {

    override suspend fun fetchDonation(data: GetDonationRequestData):
            Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Exception>> {
        return remoteSource.fetchDonation(data)
    }
}