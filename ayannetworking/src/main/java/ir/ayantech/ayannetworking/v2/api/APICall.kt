package ir.ayantech.ayannetworking.v2.api

import ir.ayantech.ayannetworking.ayanModel.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.ayannetworking.v2.model.AyanRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit


internal class APICall(private val requirements: AyanApiRequirements) : ApiCallInterface {

    private val okHttpClient: OkHttpClient by lazy {
        NetworkClientBuilder.buildOkHttpClient(
            userAgent = requirements.userAgent,
            timeout = requirements.timeout,
            setNoProxy = requirements.setNoProxy,
        )
    }

    private val retrofitClient: Retrofit by lazy {
        NetworkClientBuilder.buildRetrofitClient(
            okHttpClient = okHttpClient,
            defaultBaseUrl = requirements.baseUrl,
            gson = NetworkClientBuilder.gson
        )
    }

    private val apiInterface: ApiInterface by lazy {
        retrofitClient.create(ApiInterface::class.java)
    }


    override
    suspend fun <T, R> post(
        body: T,
        endPint: String
    ): Flow<AyanAPIResult<R, ApiCallStatus, Failure>> {
        return flow {
            emit(AyanAPIResult.changeState(ApiCallStatus.LOADING))
            val url = buildString {
                append(requirements.baseUrl)
                append(endPint)
            }
            val identity = requirements.getUserToken?.invoke()
            val ayanRequest = AyanRequest<T>(identity = identity, parameters = body)

            val response = safeApiCall {
                apiInterface.postAPI<R>(
                    url = url,
                    request = ayanRequest,
                    headers = requirements.headers,
                )
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
}