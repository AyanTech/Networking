package ir.ayantech.sample.ui

import ir.ayantech.networking.v2.helpers.Failure
import ir.ayantech.networking.v2.model.ApiCallStatus
import ir.ayantech.sample.domain.model.ReferrerTypeUIModel

sealed interface MainUIState {
    object IDLE : MainUIState
    data class Success(val data: List<ReferrerTypeUIModel>) : MainUIState
    data class Error(val failure: Failure) : MainUIState
    data class ApiStatus(val state: ApiCallStatus) : MainUIState
}
