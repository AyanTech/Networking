package ir.ayantech.networking.domain.usecase

import ir.ayantech.ayannetworking.ayanModel.FailureRepository
import ir.ayantech.ayannetworking.ayanModel.FailureType
import ir.ayantech.ayannetworking.ayanModel.Language
import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.domain.model.DonationServiceDTO
import ir.ayantech.networking.domain.model.GetDonationRequestData
import ir.ayantech.networking.domain.model.ReferrerType
import ir.ayantech.networking.domain.repository.DonationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class GetDonationUseCaseImplTest {

    private val request = GetDonationRequestData(id = "42")

    @Test
    fun `maps repository DTOs to UI models`() = runTest {
        val output = DonationServiceDTO.Output(
            referrerTypeList = listOf(
                DonationServiceDTO.ReferrerType("friend", "Friend"),
                DonationServiceDTO.ReferrerType("web", "Website")
            )
        )
        val repository = FakeDonationRepository(flowOf(AyanAPIResult.success(output)))
        val useCase = GetDonationUseCaseImpl(repository, Dispatchers.Unconfined)

        val results = useCase(request).toList()

        assertEquals(request, repository.receivedRequest)
        assertEquals(
            listOf(
                ReferrerType("friend", "Friend"),
                ReferrerType("web", "Website")
            ),
            (results.single() as AyanAPIResult.Success).value
        )
    }

    @Test
    fun `turns a null DTO list into a local failure`() = runTest {
        val repository = FakeDonationRepository(
            flowOf(AyanAPIResult.success(DonationServiceDTO.Output(null)))
        )
        val useCase = GetDonationUseCaseImpl(repository, Dispatchers.Unconfined)

        val error = useCase(request).toList().single() as AyanAPIResult.Error
        val failure = error.ayanFailure as Failure

        assertEquals(FailureRepository.LOCAL, failure.failureRepository)
        assertEquals(FailureType.NOT_200, failure.failureType)
        assertEquals(Failure.NO_CODE_SERVER_ERROR_CODE, failure.failureCode)
    }

    @Test
    fun `forwards repository failures unchanged`() = runTest {
        val failure = testFailure()
        val repository = FakeDonationRepository(flowOf(AyanAPIResult.error(failure)))
        val useCase = GetDonationUseCaseImpl(repository, Dispatchers.Unconfined)

        val result = useCase(request).toList().single() as AyanAPIResult.Error

        assertSame(failure, result.ayanFailure)
    }

    @Test
    fun `forwards repository status changes`() = runTest {
        val repository = FakeDonationRepository(
            flowOf(AyanAPIResult.changeState(ApiCallStatus.LOADING))
        )
        val useCase = GetDonationUseCaseImpl(repository, Dispatchers.Unconfined)

        val result = useCase(request).toList().single()

        assertTrue(result is AyanAPIResult.ChangeState)
        assertEquals(ApiCallStatus.LOADING, (result as AyanAPIResult.ChangeState).state)
    }

    private class FakeDonationRepository(
        private val results: Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Exception>>
    ) : DonationRepository {
        var receivedRequest: GetDonationRequestData? = null

        override suspend fun fetchDonation(
            data: GetDonationRequestData
        ): Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Exception>> {
            receivedRequest = data
            return results
        }
    }

    private fun testFailure() = Failure(
        failureRepository = FailureRepository.REMOTE,
        failureType = FailureType.TIMEOUT,
        failureCode = "TIMEOUT",
        language = Language.ENGLISH,
        failureStatus = null
    )
}
