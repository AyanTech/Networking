package ir.ayantech.sample.ui

import androidx.lifecycle.ViewModel
import ir.ayantech.networking.v2.api.onChangeState
import ir.ayantech.networking.v2.api.onFailure
import ir.ayantech.networking.v2.api.onSuccess
import ir.ayantech.networking.v2.helpers.Failure
import ir.ayantech.sample.domain.model.GetDonationRequestData
import ir.ayantech.sample.domain.usecase.GetDonationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(private val donationUseCase: GetDonationUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUIState>(MainUIState.IDLE)
    val uiState = _uiState.asStateFlow()

    suspend fun getDonation() {
        val requestData = GetDonationRequestData(id = "1234")
        donationUseCase.invoke(requestData).collect { ayanResult ->

            ayanResult.onSuccess { data ->
                _uiState.emit(MainUIState.Success(data))

            }
            ayanResult.onChangeState { state ->
                _uiState.emit(MainUIState.ApiStatus(state))
            }

            ayanResult.onFailure { failure ->
                if (failure is Failure) {
                    _uiState.emit(MainUIState.Error(failure))
                }
            }
        }
    }
}