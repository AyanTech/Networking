package ir.ayantech.networking.ayanModel

enum class FailureRepository {
    LOCAL,
    REMOTE
}

enum class FailureType {
    NO_INTERNET_CONNECTION,
    TIMEOUT,
    CANCELED,
    LOGIN_REQUIRED,
    NOT_200,
    UNKNOWN
}

enum class Language(val title: String) {
    PERSIAN("fa"),
    ENGLISH("en"),
    ARABIC("ar")
}