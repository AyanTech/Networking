package ir.ayantech.ayannetworking.v2.api

import ir.ayantech.ayannetworking.api.GetUserToken
import ir.ayantech.ayannetworking.ayanModel.Language
import ir.ayantech.ayannetworking.ayanModel.LogLevel
import kotlin.time.Duration

data class AyanApiRequirements(
    val userAgent: String,
    val baseUrl: String,
    val getUserToken: GetUserToken?,
    val pathUrl: String?,
    val timeout: Duration,
    val headers: HashMap<String, String>,
    val stringParameters: Boolean,
    val setNoProxy: Boolean,
    val logLevel: LogLevel,
    val language: Language,
    val followRedirects: Boolean = true,
)