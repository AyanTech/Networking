package ir.ayantech.ayannetworking.v2.helpers

import ir.ayantech.ayannetworking.ayanModel.FailureRepository
import ir.ayantech.ayannetworking.ayanModel.FailureType
import ir.ayantech.ayannetworking.ayanModel.Language
import org.junit.Assert.assertEquals
import org.junit.Test

class FailureTest {

    @Test
    fun `default message is localized from failure type`() {
        // Arrange
        val language = Language.PERSIAN
        val type = FailureType.LOGIN_REQUIRED

        // Act
        val failure = createFailure(type = type, language = language)

        // Assert
        assertEquals(Failure.LOGIN_REQUIRED_FA, failure.failureMessage)
    }

    @Test
    fun `explicit server message overrides localized default`() {
        // Arrange
        val serverMessage = "The server rejected the request"

        // Act
        val failure = Failure(
            failureRepository = FailureRepository.REMOTE,
            failureType = FailureType.UNKNOWN,
            failureCode = "G99999",
            language = Language.ENGLISH,
            failureStatus = null,
            failureMessage = serverMessage
        )

        // Assert
        assertEquals(serverMessage, failure.failureMessage)
    }

    private fun createFailure(type: FailureType, language: Language) = Failure(
        failureRepository = FailureRepository.LOCAL,
        failureType = type,
        failureCode = Failure.APP_INTERNAL_ERROR_CODE,
        language = language,
        failureStatus = null
    )
}
