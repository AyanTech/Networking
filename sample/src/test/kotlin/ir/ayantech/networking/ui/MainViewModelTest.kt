package ir.ayantech.networking.ui

import ir.ayantech.ayannetworking.ayanModel.FailureRepository
import ir.ayantech.ayannetworking.ayanModel.FailureType
import ir.ayantech.ayannetworking.ayanModel.Language
import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.domain.model.GetDonationRequestData
import ir.ayantech.networking.domain.model.ReferrerType
import ir.ayantech.networking.domain.usecase.GetDonationUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class MainViewModelTest {

    @Test
    fun `initial state is idle`() {
        val viewModel = MainViewModel(FakeGetDonationUseCase(emptyList()))

        assertSame(MainUIState.IDLE, viewModel.uiState.value)
    }

    @Test
    fun `getDonation sends fixed request and exposes successful data`() = runTest {
        val data = listOf(ReferrerType(name = "friend", showName = "Friend"))
        val useCase = FakeGetDonationUseCase(listOf(AyanAPIResult.success(data)))
        val viewModel = MainViewModel(useCase)

        viewModel.getDonation()

        assertEquals(GetDonationRequestData(id = "1234"), useCase.receivedRequest)
        assertEquals(MainUIState.Success(data), viewModel.uiState.value)
    }

    @Test
    fun `getDonation exposes api status`() = runTest {
        val useCase = FakeGetDonationUseCase(
            listOf(AyanAPIResult.changeState(ApiCallStatus.LOADING))
        )
        val viewModel = MainViewModel(useCase)

        viewModel.getDonation()

        assertEquals(MainUIState.ApiStatus(ApiCallStatus.LOADING), viewModel.uiState.value)
    }

    @Test
    fun `getDonation exposes networking failure`() = runTest {
        val failure = testFailure()
        val useCase = FakeGetDonationUseCase(listOf(AyanAPIResult.error(failure)))
        val viewModel = MainViewModel(useCase)

        viewModel.getDonation()

        assertEquals(MainUIState.Error(failure), viewModel.uiState.value)
    }

    @Test
    fun `getDonation ignores errors that are not networking failures`() = runTest {
        val useCase = FakeGetDonationUseCase(
            listOf(AyanAPIResult.error(IllegalStateException("unexpected")))
        )
        val viewModel = MainViewModel(useCase)

        viewModel.getDonation()

        assertSame(MainUIState.IDLE, viewModel.uiState.value)
    }

    private class FakeGetDonationUseCase(
        private val results: List<AyanAPIResult<List<ReferrerType>, ApiCallStatus, Exception>>
    ) : GetDonationUseCase {
        var receivedRequest: GetDonationRequestData? = null

        override suspend fun invoke(
            requestData: GetDonationRequestData
        ): Flow<AyanAPIResult<List<ReferrerType>, ApiCallStatus, Exception>> {
            receivedRequest = requestData
            return flowOf(*results.toTypedArray())
        }
    }

    private fun testFailure() = Failure(
        failureRepository = FailureRepository.REMOTE,
        failureType = FailureType.UNKNOWN,
        failureCode = "TEST",
        language = Language.ENGLISH,
        failureStatus = null
    )
}
