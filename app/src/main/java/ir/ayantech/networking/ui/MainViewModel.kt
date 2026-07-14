package ir.ayantech.networking.ui

import androidx.lifecycle.ViewModel
import ir.ayantech.networking.domain.usecase.GetDonationUseCase

class MainViewModel(private val donationUseCase: GetDonationUseCase): ViewModel() {


   suspend fun getDonation(){

    }
}