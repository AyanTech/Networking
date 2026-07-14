package ir.ayantech.ayannetworking.v2.api

import ir.ayantech.ayannetworking.v2.helpers.Failure
import ir.ayantech.ayannetworking.v2.model.ApiCallStatus

sealed class AyanAPIResult<out T, J, K> {
    data class Success<T>(val value: T) : AyanAPIResult<T, ApiCallStatus, Failure>()
    data class ChangeState<T>(val state: ApiCallStatus) : AyanAPIResult<T, ApiCallStatus, Failure>()
    data class Error<T>(val ayanFailure: Failure) :
        AyanAPIResult<T, ApiCallStatus, Failure>()

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
            failure: Failure,
        ): AyanAPIResult<T, ApiCallStatus, Failure> {
            return Error(ayanFailure = failure)
        }

        fun <T> changeState(state: ApiCallStatus): AyanAPIResult<T, ApiCallStatus, Failure> {
            return ChangeState(state)
        }
    }

}

inline fun <T, R> AyanAPIResult<T, ApiCallStatus, Failure>.fold(
    onSuccess: (T) -> R,
    onChangeState: (ApiCallStatus) -> R,
    onError: (failure: Failure?) -> R,
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

inline fun <T, J, K> AyanAPIResult<T, J, K>.onFailure(action: (ayanFailure: Failure) -> Unit): AyanAPIResult<T, J, K> {
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