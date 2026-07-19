package ir.ayantech.networking.v2.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ir.ayantech.networking.v2.model.ApiCallStatus
import ir.ayantech.networking.v2.model.AyanRequest
import ir.ayantech.networking.v2.model.Identity
import ir.ayantech.networking.v2.network.KtorClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class APICall(val requirements: AyanApiRequirements) {

    val httpClient: HttpClient by lazy { KtorClient.getClient(requirements) }

     inline fun <reified T, reified R> post(
        body: T,
        apiPath: String,
        baseUrl: String? = null
    ): Flow<AyanAPIResult<R, ApiCallStatus, Exception>> = flow {
            emit(AyanAPIResult.changeState(ApiCallStatus.LOADING))
            val url = buildString {
                append(baseUrl ?: requirements.baseUrl)
                append(apiPath)
            }
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
