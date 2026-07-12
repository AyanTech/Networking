package ir.ayantech.ayannetworking.v2

import android.content.Context
import ir.ayantech.ayannetworking.api.GetUserToken
import ir.ayantech.ayannetworking.ayanModel.Failure
import ir.ayantech.ayannetworking.ayanModel.LogLevel
import ir.ayantech.ayannetworking.helper.AppSignatureHelper
import ir.ayantech.ayannetworking.v2.api.APICall
import ir.ayantech.ayannetworking.v2.api.ApiCallInterface
import ir.ayantech.ayannetworking.v2.api.AyanAPIResult
import ir.ayantech.ayannetworking.v2.api.AyanApiRequirements
import ir.ayantech.ayannetworking.v2.helpers.generateUserAgent
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


class AyanApi private constructor(private val requirements: AyanApiRequirements) :
    ApiCallInterface {

    private val apiCall: APICall by lazy {
        APICall(requirements)
    }

    override suspend fun <Body, Response> post(
        body: Body,
        endPint: String
    ): Flow<AyanAPIResult<Response, ApiCallStatus, Failure>> {
        return apiCall.post(body = body, endPint = endPint)
    }


    class Builder(
        private val context: Context,
        private val baseUrl: String,
    ) {

        private var _getUserToken: GetUserToken? = null
        private var _pathUrl: String = ""
        private var _timeout: Duration = 30.seconds
        private var _headers: HashMap<String, String> = HashMap()
        private var _stringParameters: Boolean = false
        private var _setNoProxy: Boolean = true
        private var _logLevel: LogLevel = LogLevel.LOG_ALL


        /*
         *TODO: It's bad way for read user token,
         *  it's better to use dependency injection or another solution like observer pattern.
         */
        fun setInvokeUserToken(getUserToken: () -> String): Builder {
            this._getUserToken = getUserToken
            return this
        }

        fun setNoProxyMode(noProxy: Boolean): Builder {
            this._setNoProxy = noProxy
            return this
        }

        fun setTimeOutDuration(timeOut: Duration): Builder {
            this._timeout = timeOut
            return this
        }

        fun setLogLevel(level: LogLevel): Builder {
            this._logLevel = level
            return this
        }

        fun setCustomHeaders(headers: HashMap<String, String>): Builder {
            this._headers = headers
            return this
        }

        fun setPathUrl(path: String): Builder {
            this._pathUrl = path
            return this
        }

        fun build(): AyanApi {
            val sign = AppSignatureHelper(context).appSignatures.firstOrNull().orEmpty()
            val userAgent = context.generateUserAgent(sign = sign)
            val requirements = AyanApiRequirements(
                userAgent,
                baseUrl,
                _getUserToken,
                _pathUrl,
                _timeout,
                _headers,
                _stringParameters,
                _setNoProxy,
                _logLevel
            )

            return AyanApi(requirements = requirements)
        }

    }
}


