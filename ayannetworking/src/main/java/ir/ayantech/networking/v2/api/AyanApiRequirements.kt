package ir.ayantech.networking.v2.api

import ir.ayantech.networking.ayanModel.Language
import ir.ayantech.networking.ayanModel.LogLevel
import kotlin.time.Duration

data class AyanApiRequirements(
    val userAgent: String,
    val baseUrl: String,
    val getUserToken: (() -> String)?,
    val pathUrl: String?,
    val timeout: Duration,
    val headers: HashMap<String, String>,
    val stringParameters: Boolean,
    val setNoProxy: Boolean,
    val logLevel: LogLevel,
    val language: Language,
    val followRedirects: Boolean = true,
)