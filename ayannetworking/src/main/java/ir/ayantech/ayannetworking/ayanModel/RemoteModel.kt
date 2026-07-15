package ir.ayantech.ayannetworking.ayanModel

@Deprecated("Use v2 models")
data class Identity(var Token: String?)

@Deprecated("Use v2 models")
data class Status(
    val Code: String?,
    val Description: String?,
    val Hint: String?,
    val IsFromCache: Boolean?,
    val Retryable: Boolean?,
    val Type: String?
)

@Deprecated("Use v2 models")
data class AyanRequest<T>(var Identity: Any?, var Parameters: T?)

@Deprecated("Use v2 models")
data class AyanResponse<T>(var Parameters: T?, var Status: Status)

@Deprecated("Use v2 models")
data class EscapedParameters(val Params: String, var MethodName: String? = null)