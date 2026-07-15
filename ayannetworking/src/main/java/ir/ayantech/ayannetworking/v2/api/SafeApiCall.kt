package ir.ayantech.ayannetworking.v2.api

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import ir.ayantech.ayannetworking.ayanModel.FailureRepository
import ir.ayantech.ayannetworking.ayanModel.FailureType
import ir.ayantech.ayannetworking.ayanModel.Language
import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import ir.ayantech.ayannetworking.v2.model.AyanResponse
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

const val TAG = "TAG_NET_ERROR"

suspend inline fun <reified T> safeApiCall(
    language: Language = Language.PERSIAN,
    request: suspend () -> HttpResponse
): AyanAPIResult<T, ApiCallStatus, Exception> {
    return try {
        val response = request.invoke()

        if (response.status == HttpStatusCode.OK) {
            val data = response.body<AyanResponse<T>>()
            val failureCode = data.status.code ?: response.status.value.toString()


            Log.d("TAG_NETWORKING", "safeApiCall body: $data")

            if (data.parameters == null) {
                val failure = Failure(
                    failureRepository = FailureRepository.REMOTE,
                    failureType = FailureType.UNKNOWN,
                    failureCode = failureCode,
                    reCallApi = null,
                    language = Language.PERSIAN,
                    failureStatus = null,
                )
                return AyanAPIResult.error(failure)
            }


            when (data.status.code) {
                "G00000" if data.parameters != null -> {
                    return AyanAPIResult.success(data.parameters!!)
                }

                "G00002" -> {
                    val failure = Failure(
                        failureRepository = FailureRepository.REMOTE,
                        failureType = FailureType.LOGIN_REQUIRED,
                        failureCode = failureCode,
                        reCallApi = null,
                        language = Language.PERSIAN,
                        failureStatus = data.status,
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
                        failureStatus = data.status,
                        failureMessage = data.status.description.orEmpty()
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

fun Throwable.toFailure(language: Language): Failure {

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