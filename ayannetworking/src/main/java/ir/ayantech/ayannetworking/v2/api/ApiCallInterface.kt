package ir.ayantech.ayannetworking.v2.api

import ir.ayantech.ayannetworking.ayanModel.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import kotlinx.coroutines.flow.Flow

interface ApiCallInterface {
    suspend fun <T, R> post(
        body: T,
        endPint: String
    ): Flow<AyanAPIResult<R, ApiCallStatus, Failure>>
}