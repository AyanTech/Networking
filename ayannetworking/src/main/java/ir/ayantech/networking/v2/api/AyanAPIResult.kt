package ir.ayantech.networking.v2.api

import ir.ayantech.networking.v2.model.ApiCallStatus

sealed class AyanAPIResult<out T, J, K> {
    data class Success<T>(val value: T) : AyanAPIResult<T, ApiCallStatus, Exception>()
    data class ChangeState<T>(val state: ApiCallStatus) : AyanAPIResult<T, ApiCallStatus, Exception>()
    data class Error<T>(val ayanFailure: Exception) :
        AyanAPIResult<T, ApiCallStatus, Exception>()

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    val isChangeState: Boolean
        get() = this is ChangeState


    companion object {
        fun <T> success(value: T): Success<T> {
            return Success(value)
        }

        fun <T> error(
            failure: Exception,
        ): AyanAPIResult<T, ApiCallStatus, Exception> {
            return Error(ayanFailure = failure)
        }

        fun <T> changeState(state: ApiCallStatus): AyanAPIResult<T, ApiCallStatus, Exception> {
            return ChangeState(state)
        }
    }

}

inline fun <T, R> AyanAPIResult<T, ApiCallStatus, Exception>.fold(
    onSuccess: (T) -> R,
    onChangeState: (ApiCallStatus) -> R,
    onError: (failure: Exception?) -> R,
): R {
    return when (this) {
        is AyanAPIResult.Success -> onSuccess(value)

        is AyanAPIResult.ChangeState -> onChangeState(state)

        is AyanAPIResult.Error -> onError(ayanFailure)
    }
}

inline fun <T, J, K> AyanAPIResult<T, J, K>.onSuccess(action: (t: T) -> Unit): AyanAPIResult<T, J, K> {
    if (isSuccess) {
        (this as? AyanAPIResult.Success)?.let {
            action.invoke(value)
        }
    }
    return this
}

inline fun <T, J, K> AyanAPIResult<T, J, K>.onFailure(action: (ayanFailure: Exception) -> Unit): AyanAPIResult<T, J, K> {
    if (isError) {
        (this as? AyanAPIResult.Error)?.let {
            action.invoke(ayanFailure)
        }
    }
    return this
}

inline fun <T, J, K> AyanAPIResult<T, J, K>.onChangeState(action: (state: ApiCallStatus) -> Unit): AyanAPIResult<T, J, K> {
    if (isChangeState) {
        (this as? AyanAPIResult.ChangeState)?.let {
            action.invoke(state)
        }
    }
    return this
}