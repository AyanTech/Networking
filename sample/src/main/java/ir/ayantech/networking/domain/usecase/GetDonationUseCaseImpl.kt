package ir.ayantech.networking.domain.usecase

import ir.ayantech.ayannetworking.ayanModel.FailureRepository
import ir.ayantech.ayannetworking.ayanModel.FailureType
import ir.ayantech.ayannetworking.ayanModel.Language
import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.api.onChangeState
import ir.ayantech.ayannetworking.v2.api.onFailure
import ir.ayantech.ayannetworking.v2.api.onSuccess
import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.domain.model.DonationServiceDTO
import ir.ayantech.networking.domain.model.GetDonationRequestData
import ir.ayantech.networking.domain.model.ReferrerType
import ir.ayantech.networking.domain.repository.DonationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

class GetDonationUseCaseImpl(
    private val repository: DonationRepository,
    private val coroutineContext: CoroutineContext
) : GetDonationUseCase {

    override suspend fun invoke(requestData: GetDonationRequestData): Flow<AyanAPIResult<List<ReferrerType>, ApiCallStatus, Exception>> {
        return flow {
            repository.fetchDonation(requestData).collect { result ->

                result.onSuccess { donationServiceDTO ->
                    //It can be checked business logics, validations etc ...
                    if (donationServiceDTO.referrerTypeList == null) {
                        val failure = Failure(
                            failureRepository = FailureRepository.LOCAL,
                            failureType = FailureType.NOT_200,
                            failureStatus = null,
                            failureCode = Failure.NO_CODE_SERVER_ERROR_CODE,
                            reCallApi = null,
                            language = Language.PERSIAN,
                        )
                        emit(AyanAPIResult.error(failure))
                    } else {
                        emit(AyanAPIResult.success(donationServiceDTO.toUIModel()))
                    }
                }

                result.onFailure { failure ->
                    emit(AyanAPIResult.error(failure))
                }

                result.onChangeState { state ->
                    emit(AyanAPIResult.changeState(state))
                }
            }
        }.flowOn(context = coroutineContext)
    }

    private fun DonationServiceDTO.Output.toUIModel(): List<ReferrerType> {
        val items = mutableListOf<ReferrerType>()
        this.referrerTypeList?.forEach { dto ->
            items.add(ReferrerType(name = dto.name, showName = dto.showName))
        }
        return items
    }
}