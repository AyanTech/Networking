package ir.ayantech.sample.domain.usecase

import ir.ayantech.networking.v2.api.AyanAPIResult
import ir.ayantech.networking.v2.model.ApiCallStatus
import ir.ayantech.sample.domain.model.GetDonationRequestData
import ir.ayantech.sample.domain.model.ReferrerTypeUIModel
import kotlinx.coroutines.flow.Flow

interface GetDonationUseCase {
    suspend operator fun invoke(requestData: GetDonationRequestData):
            Flow<AyanAPIResult<List<ReferrerTypeUIModel>, ApiCallStatus, Exception>>
}