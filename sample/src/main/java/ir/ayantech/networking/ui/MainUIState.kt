package ir.ayantech.networking.ui

import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.domain.model.ReferrerType

sealed interface MainUIState {
    object IDLE : MainUIState
    data class Success(val data: List<ReferrerType>) : MainUIState
    data class Error(val failure: Failure) : MainUIState
    data class ApiStatus(val state: ApiCallStatus) : MainUIState
}
