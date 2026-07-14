package ir.ayantech.ayannetworking.v2.api

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.ayannetworking.v2.model.AyanRequest
import ir.ayantech.ayannetworking.v2.model.Identity
import ir.ayantech.ayannetworking.v2.network.KtorClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class APICall(val requirements: AyanApiRequirements) {

    val httpClient: HttpClient by lazy { KtorClient.getClient(requirements) }

     inline fun <reified T, reified R> post(
        body: T,
        apiPath: String,
        baseUrl: String? = null
    ): Flow<AyanAPIResult<R, ApiCallStatus, Failure>> = flow {
            emit(AyanAPIResult.changeState(ApiCallStatus.LOADING))
            val url = buildString {
                append(baseUrl ?: requirements.baseUrl)
                append(apiPath)
            }
            Log.d(TAG, "post: $url")
            val identity = Identity(token = requirements.getUserToken?.invoke())
            val ayanRequest = AyanRequest(identity = identity, parameters = body)

            val response = safeApiCall<R> {
                httpClient.post(url) {
                    setBody(ayanRequest)
                }
            }

            emit(response)

            if (response.isError) {
                emit(AyanAPIResult.changeState(ApiCallStatus.FAILED))
            } else if (response.isSuccess) {
                emit(AyanAPIResult.changeState(ApiCallStatus.SUCCESSFUL))
            }

            emit(AyanAPIResult.changeState(ApiCallStatus.IDLE))
        }
}
