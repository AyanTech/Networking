package ir.ayantech.ayannetworking.v2.api


import android.util.Log
import java.util.concurrent.CancellationException
import ir.ayantech.ayannetworking.ayanModel.Failure
import ir.ayantech.ayannetworking.ayanModel.FailureRepository
import ir.ayantech.ayannetworking.ayanModel.FailureType
import ir.ayantech.ayannetworking.ayanModel.Language
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus
import retrofit2.Response
import java.io.IOException


suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> Response<T>
): AyanAPIResult<T, ApiCallStatus, Failure> {
    try {
        val response = apiCall()

        if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                TODO()
            } else {
                TODO()

            }
        } else {
            createHttpError(response)
            TODO()
        }
    } catch (exception: CancellationException) {
        TODO()
    } catch (exception: IOException) {
        TODO()
    } catch (exception: Exception) {
        TODO()
    }
}

private fun <T> createHttpError(
    response: Response<T>
): AyanAPIResult.Error<Failure> {

    val rawError = runCatching {
        response.errorBody()?.string()
    }.getOrNull()

    Log.d("TAG_NETWORKING", "createHttpError raw error: ${rawError}")

    return AyanAPIResult.Error(
        Failure(
            failureRepository = FailureRepository.LOCAL,
            failureType = FailureType.UNKNOWN,
            failureCode = Failure.NO_CODE_SERVER_ERROR_CODE,
            reCallApi = { },
            language = Language.PERSIAN,
            failureStatus = null,
        )
    )

}