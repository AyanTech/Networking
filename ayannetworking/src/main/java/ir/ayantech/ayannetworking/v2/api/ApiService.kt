package ir.ayantech.ayannetworking.v2.api

import ir.ayantech.ayannetworking.v2.model.AyanRequest
import ir.ayantech.ayannetworking.v2.model.AyanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiInterface {
    @POST
    @JvmSuppressWildcards
    suspend fun <R> postAPI(
        @Url url: String?,
        @Body request: AyanRequest<*>?,
        @HeaderMap headers: MutableMap<String, String>
    ): Response<AyanResponse<R>>

}


