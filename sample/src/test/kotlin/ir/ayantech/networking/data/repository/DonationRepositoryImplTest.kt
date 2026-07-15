package ir.ayantech.networking.data.repository

import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.networking.data.source.DonationRemoteDataSource
import ir.ayantech.networking.domain.model.DonationServiceDTO
import ir.ayantech.networking.domain.model.GetDonationRequestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class DonationRepositoryImplTest {

    @Test
    fun `fetchDonation delegates request and returns remote flow`() = runTest {
        val request = GetDonationRequestData(id = "99")
        val output = DonationServiceDTO.Output(emptyList())
        val remoteFlow = flowOf(AyanAPIResult.success(output))
        val remoteSource = FakeDonationRemoteDataSource(remoteFlow)
        val repository = DonationRepositoryImpl(remoteSource)

        val result = repository.fetchDonation(request)

        assertEquals(request, remoteSource.receivedRequest)
        assertSame(remoteFlow, result)
    }

    private class FakeDonationRemoteDataSource(
        private val result: Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Exception>>
    ) : DonationRemoteDataSource {
        var receivedRequest: GetDonationRequestData? = null

        override suspend fun fetchDonation(
            data: GetDonationRequestData
        ): Flow<AyanAPIResult<DonationServiceDTO.Output, ApiCallStatus, Exception>> {
            receivedRequest = data
            return result
        }
    }
}
