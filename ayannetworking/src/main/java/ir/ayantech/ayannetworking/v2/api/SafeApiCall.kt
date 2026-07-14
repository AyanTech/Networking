package ir.ayantech.ayannetworking.v2.api


import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import ir.ayantech.ayannetworking.ayanModel.Failure
import ir.ayantech.ayannetworking.ayanModel.FailureRepository
import ir.ayantech.ayannetworking.ayanModel.FailureType
import ir.ayantech.ayannetworking.ayanModel.Language
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.ayannetworking.v2.model.AyanResponse
import retrofit2.Response
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

const val TAG = "TAG_NET_ERROR"


suspend fun <T> safeApiCall(
    language: Language = Language.PERSIAN,
    apiCall: suspend () -> Response<AyanResponse<T>>
): AyanAPIResult<T, ApiCallStatus, Failure> {
    return try {
        val response = apiCall()

        if (response.isSuccessful) {
            val body = response.body()
            val bodyString = response.raw().body.string()

            Log.d("TAG_NETWORKING", "safeApiCall body: $bodyString")

            if (body == null) {
                val failure = Failure(
                    failureRepository = FailureRepository.REMOTE,
                    failureType = FailureType.UNKNOWN,
                    failureCode = response.code().toString(),
                    reCallApi = null,
                    language = Language.PERSIAN,
                    failureStatus = null,
                )
                return AyanAPIResult.error(failure)
            }

            val jsonObject = JsonParser.parseString(bodyString).asJsonObject
            val jsonElementParameters = jsonObject.get("Parameters")
            val failureCode = body.status.Code ?: response.code().toString()

            when (body.status.Code) {
                "G00000" if body.parameters != null -> {
                    return AyanAPIResult.success(body.parameters!!)
                }

                "G00002" -> {
                    val failure = Failure(
                        failureRepository = FailureRepository.REMOTE,
                        failureType = FailureType.LOGIN_REQUIRED,
                        failureCode = failureCode,
                        reCallApi = null,
                        language = Language.PERSIAN,
                        failureStatus = body.status,
                    )
                    return AyanAPIResult.error(failure)
                }

                else -> {
                    val failure = Failure(
                        failureRepository = FailureRepository.REMOTE,
                        failureType = FailureType.UNKNOWN,
                        failureCode = failureCode,
                        reCallApi = null,
                        language = Language.PERSIAN,
                        failureStatus = body.status,
                        failureMessage = body.status.Description.orEmpty()
                    )
                    return AyanAPIResult.error(failure)
                }
            }

        } else {
            val failure = Failure(
                failureRepository = FailureRepository.REMOTE,
                failureType = FailureType.NOT_200,
                failureCode = Failure.APP_INTERNAL_ERROR_CODE,
                reCallApi = null,
                language = Language.PERSIAN,
                failureStatus = null,
            )
            AyanAPIResult.error(failure)
        }
    } catch (throwable: Throwable) {
        val failure = throwable.toFailure(language = language)
        AyanAPIResult.error(failure)
    }
}

private fun <T> parseServerError(
    response: Response<T>
): Failure {

    val rawError = runCatching {
        response.errorBody()?.string()
    }.getOrNull()

    Log.d("TAG_NETWORKING", "createHttpError raw error: $rawError")

    return Failure(
        failureRepository = FailureRepository.LOCAL,
        failureType = FailureType.UNKNOWN,
        failureCode = Failure.NO_CODE_SERVER_ERROR_CODE,
        reCallApi = { },
        language = Language.PERSIAN,
        failureStatus = null,
    )
}

private fun Throwable.toFailure(language: Language): Failure {

    Log.d(TAG, "toFailure message: ${this.message}")

    return when {
        this is UnknownHostException -> Failure(
            failureRepository = FailureRepository.LOCAL,
            failureType = FailureType.NO_INTERNET_CONNECTION,
            failureCode = Failure.APP_INTERNAL_ERROR_CODE,
            reCallApi = null,
            language = language,
            failureStatus = null
        )

        this is TimeoutException -> Failure(
            failureRepository = FailureRepository.LOCAL,
            failureType = FailureType.TIMEOUT,
            failureCode = Failure.APP_INTERNAL_ERROR_CODE,
            reCallApi = null,
            language = language,
            failureStatus = null
        )

        this is SocketTimeoutException -> Failure(
            failureRepository = FailureRepository.LOCAL,
            failureType = FailureType.TIMEOUT,
            failureCode = Failure.APP_INTERNAL_ERROR_CODE,
            reCallApi = null,
            language = language,
            failureStatus = null
        )

        this is InterruptedIOException && this.message == "timeout" -> Failure(
            failureRepository = FailureRepository.LOCAL,
            failureType = FailureType.TIMEOUT,
            failureCode = Failure.APP_INTERNAL_ERROR_CODE,
            reCallApi = null,
            language = language,
            failureStatus = null
        )

        (this is IOException && this.message == "canceled") -> Failure(
            failureRepository = FailureRepository.LOCAL,
            failureType = FailureType.TIMEOUT,
            failureCode = Failure.APP_INTERNAL_ERROR_CODE,
            reCallApi = null,
            language = language,
            failureStatus = null
        )

        else -> Failure(
            failureRepository = FailureRepository.LOCAL,
            failureType = FailureType.UNKNOWN,
            failureCode = Failure.NO_CODE_SERVER_ERROR_CODE,
            reCallApi = null,
            language = language,
            failureStatus = null
        )
    }

}