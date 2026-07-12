package ir.ayantech.ayannetworking.v2.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
import java.net.Proxy
import kotlin.time.Duration

internal object NetworkClientBuilder {

    val gson: Gson by lazy {
        GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .setStrictness(Strictness.LENIENT)
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .create()
    }

    fun buildOkHttpClient(
        userAgent: String,
        timeout: Duration,
        setNoProxy: Boolean,
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.apply {
            callTimeout(timeout)
            connectTimeout(timeout)
            readTimeout(timeout)
            writeTimeout(timeout)
        }

        if (setNoProxy) {
            okHttpClientBuilder.proxy(Proxy.NO_PROXY)
        }
        okHttpClientBuilder.protocols(arrayListOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
        okHttpClientBuilder.addInterceptor { interceptorChain ->
            val userAgentRequest = interceptorChain
                .request()
                .newBuilder()
                .header("User-Agent", userAgent).build()
            interceptorChain.proceed(userAgentRequest)
        }
        return okHttpClientBuilder.build()
    }

    fun buildRetrofitClient(
        okHttpClient: OkHttpClient,
        defaultBaseUrl: String,
        gson: Gson,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(defaultBaseUrl)
            .client(okHttpClient)
            .build()
    }

}