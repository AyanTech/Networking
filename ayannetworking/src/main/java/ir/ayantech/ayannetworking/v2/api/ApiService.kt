package ir.ayantech.ayannetworking.v2.api

import ir.ayantech.ayannetworking.v2.model.AyanRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiInterface {
    @POST
    suspend fun postAPI(
        @Url url: String?,
        @Body body: AyanRequest<*>?,
        @HeaderMap headers: MutableMap<String, String>
    ): Response<ResponseBody?>?

}


