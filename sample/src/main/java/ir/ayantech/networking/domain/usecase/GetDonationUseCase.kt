package ir.ayantech.networking.domain.usecase

import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.domain.model.DonationServiceDTO
import ir.ayantech.networking.domain.model.DonationServiceUIModel
import ir.ayantech.networking.domain.model.GetDonationRequestData
import ir.ayantech.networking.domain.model.ReferrerType
import kotlinx.coroutines.flow.Flow

interface GetDonationUseCase {
    suspend operator fun invoke(requestData: GetDonationRequestData):
            Flow<AyanAPIResult<List<ReferrerType>, ApiCallStatus, Exception>>
}