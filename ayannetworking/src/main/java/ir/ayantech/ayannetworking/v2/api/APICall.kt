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
    suspend fun <Body, Response> post(
        body: Body,
        endPint: String
    ): Flow<AyanAPIResult<Response, ApiCallStatus, Failure>> {
        return flow {


            val url = buildString {
                append(requirements.baseUrl)
                append(endPint)
            }
            val identity = requirements.getUserToken?.invoke()
            val ayanRequest = AyanRequest(identity = identity, body)


            val response = apiInterface.postAPI(
                url = url,
                body = ayanRequest,
                headers = requirements.headers
            )
            //TODO use safeAPICall and emit response: state, success, error
        }

    }
}