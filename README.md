# Ayan Networking v2

`v2` is the Ktor-based API client in the `ayannetworking` Android library. It sends
serializable request models inside the Ayan request envelope and exposes responses,
errors, and request-state changes as a Kotlin `Flow`.

This repository-level document covers only the implementation under
`ayannetworking/src/main/java/ir/ayantech/ayannetworking/v2`. It does not document
the legacy networking implementation.

## Requirements

- Android API 21 or newer
- Kotlin coroutines and `Flow`
- Kotlin serialization
- Ktor client with the OkHttp engine

When using the module from this repository, add it to the consuming Android module:

```kotlin
dependencies {
    implementation(project(":ayannetworking"))
}
```

Request and response DTOs must be serializable:

```kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRequest(
    @SerialName("UserId") val userId: String,
)

@Serializable
data class ProfileResponse(
    @SerialName("DisplayName") val displayName: String,
)
```

## Create a client

Create one `AyanApi` instance and reuse it. The base URL and endpoint are concatenated
as provided, so include any required `/` separator in one of them.

```kotlin
import ir.ayantech.ayannetworking.ayanModel.Language
import ir.ayantech.ayannetworking.ayanModel.LogLevel
import ir.ayantech.ayannetworking.v2.AyanApi
import kotlin.time.Duration.Companion.seconds

val ayanApi = AyanApi.Builder(
    context = applicationContext,
    baseUrl = "https://api.example.com/",
)
    .setTimeOutDuration(20.seconds)
    .setAcceptLanguage(Language.ENGLISH)
    .setLogLevel(LogLevel.LOG_ALL)
    .setInvokeUserToken { tokenStore.currentToken.orEmpty() }
    .build()
```

The token callback is evaluated when requests are created, allowing it to return the
latest token. The client sends it in both the Ayan `Identity.Token` field and the HTTP
`Authorization` header.

### Builder defaults

| Setting | Default |
| --- | --- |
| Timeout | 30 seconds |
| Language | `Language.PERSIAN` |
| Logging | `LogLevel.LOG_ALL` |
| No-proxy mode | `true` |
| Follow redirects | `true` |
| Token | Not set |

`LOG_ALL` logs request and response details through Ktor. Authorization, proxy
authorization, and cookie headers are sanitized. Disable full logging for production
if payloads may contain sensitive information.

## Make a POST request

`post` is currently the public request operation:

```kotlin
val results = ayanApi.post<ProfileRequest, ProfileResponse>(
    body = ProfileRequest(userId = "1234"),
    endPoint = "GetProfile",
)
```

Use the optional `baseUrl` argument to override the configured base URL for one call:

```kotlin
val results = ayanApi.post<ProfileRequest, ProfileResponse>(
    body = ProfileRequest(userId = "1234"),
    endPoint = "GetProfile",
    baseUrl = "https://alternate.example.com/",
)
```

## Collect results

Each call returns a `Flow<AyanAPIResult<Response, ApiCallStatus, Exception>>`. A normal
call emits events in this order:

1. `ChangeState(LOADING)`
2. `Success(data)` or `Error(exception)`
3. `ChangeState(SUCCESSFUL)` or `ChangeState(FAILED)`
4. `ChangeState(IDLE)`

Handle events with the provided extension functions:

```kotlin
import ir.ayantech.ayannetworking.v2.api.onChangeState
import ir.ayantech.ayannetworking.v2.api.onFailure
import ir.ayantech.ayannetworking.v2.api.onSuccess
import ir.ayantech.ayannetworking.v2.helpers.Failure

results.collect { result ->
    result
        .onSuccess { profile ->
            showProfile(profile)
        }
        .onChangeState { status ->
            updateLoadingState(status)
        }
        .onFailure { exception ->
            val failure = exception as? Failure
            showError(failure?.failureMessage ?: exception.message.orEmpty())
        }
}
```

Alternatively, use `fold` when every event should produce one value:

```kotlin
val uiEvent = result.fold(
    onSuccess = { UiEvent.ProfileLoaded(it) },
    onChangeState = { UiEvent.RequestStateChanged(it) },
    onError = { UiEvent.RequestFailed(it?.message.orEmpty()) },
)
```

The available request states are `IDLE`, `LOADING`, `FAILED`, and `SUCCESSFUL`.

## Wire format

v2 wraps every request body automatically:

```json
{
  "Identity": {
    "Token": "user-token"
  },
  "Parameters": {
    "UserId": "1234"
  }
}
```

The server response must use this envelope:

```json
{
  "Parameters": {
    "DisplayName": "Example User"
  },
  "Status": {
    "Code": "G00000",
    "Description": null,
    "Hint": null,
    "IsFromCache": false,
    "Retryable": false,
    "Type": null
  }
}
```

A response is successful only when all the following are true:

- The HTTP status is `200 OK`.
- `Status.Code` is `G00000`.
- `Parameters` is not `null`.

`G00002` is mapped to `LOGIN_REQUIRED`. Other service codes are mapped to an unknown
remote failure and preserve `Status.Description` as the failure message.

## Failures

API errors are emitted as `AyanAPIResult.Error`. The exception is normally the v2
`Failure` type and includes:

- `failureRepository`: whether the problem is local or remote
- `failureType`: categorized cause
- `failureCode`: server code or an internal fallback code
- `failureMessage`: localized, user-readable message
- `failureStatus`: the server's status object when available

Recognized failure types include no internet connection, timeout, login required,
non-200 response, canceled request, and unknown error. Failure messages support
Persian, English, and Arabic.

```kotlin
result.onFailure { exception ->
    when (val failure = exception as? Failure) {
        null -> logUnexpectedError(exception)
        else -> {
            logFailure(failure.failureCode, failure.failureType)
            showError(failure.failureMessage)
        }
    }
}
```

## Current limitations

- Only POST requests are exposed.
- Only HTTP `200 OK` is treated as a valid transport response.
- `Parameters = null` is treated as a failure, including for otherwise successful
  operations with no response body.
- `setCustomHeaders`, `setPathUrl`, and `setNoProxyMode` store configuration but the
  current Ktor client does not apply it.
- `setFollowRedirect` stores configuration, but redirects are currently always
  enabled by the Ktor client.
- The selected builder language is sent through `Accept-Language`; some server-side
  failures are currently created with Persian messages regardless of that setting.

## Package overview

| Package | Purpose |
| --- | --- |
| `v2.AyanApi` | Client builder and public POST API |
| `v2.api` | Request execution, safe-call mapping, and result helpers |
| `v2.model` | Request/response envelopes and API status models |
| `v2.helpers` | Failure model and user-agent generation |
| `v2.network` | Ktor `HttpClient` configuration |
