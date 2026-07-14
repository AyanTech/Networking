package ir.ayantech.ayannetworking.v2.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import ir.ayantech.ayannetworking.ayanModel.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import ir.ayantech.ayannetworking.v2.api.AyanApiRequirements
import kotlinx.serialization.json.Json

object KtorClient {

    fun getClient(requirements: AyanApiRequirements): HttpClient {
        return HttpClient(OkHttp) {
            expectSuccess = true
            followRedirects = true

            defaultRequest {
                url(requirements.baseUrl)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                headers.apply {
                    append(
                        HttpHeaders.UserAgent,
                        requirements.userAgent
                    )
                    append(
                        HttpHeaders.AcceptLanguage,
                        requirements.language.title
                    )

                    append(
                        HttpHeaders.Authorization,
                        requirements.getUserToken?.invoke().orEmpty()
                    )
                }

            }

            install(HttpTimeout) {
                requestTimeoutMillis = requirements.timeout.inWholeMilliseconds
                requestTimeoutMillis = requirements.timeout.inWholeMilliseconds
                socketTimeoutMillis = requirements.timeout.inWholeMilliseconds
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        explicitNulls = false
                        encodeDefaults = true
                        prettyPrint = true
                    }
                )
            }

            if (requirements.logLevel == LogLevel.LOG_ALL) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = io.ktor.client.plugins.logging.LogLevel.ALL

                    sanitizeHeader { header ->
                        header.equals(
                            HttpHeaders.Authorization,
                            ignoreCase = true
                        ) || header.equals(
                            HttpHeaders.ProxyAuthorization,
                            ignoreCase = true
                        ) || header.equals(
                            HttpHeaders.Cookie,
                            ignoreCase = true
                        ) || header.equals(
                            HttpHeaders.SetCookie,
                            ignoreCase = true
                        )
                    }
                }
            }
        }
    }
}