package ir.ayantech.ayannetworking.v2.api

import ir.ayantech.ayannetworking.ayanModel.FailureRepository
import ir.ayantech.ayannetworking.ayanModel.FailureType
import ir.ayantech.ayannetworking.ayanModel.Language
import ir.ayantech.ayannetworking.v2.helpers.Failure
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import org.junit.Assert.assertEquals
import org.junit.Test

class ThrowableToFailureTest {

    @Test
    fun `unknown host maps to no internet failure`() {
        // Arrange
        val throwable = UnknownHostException("offline")

        // Act
        val failure = throwable.toFailure(Language.ENGLISH)

        // Assert
        assertFailure(failure, FailureType.NO_INTERNET_CONNECTION)
        assertEquals(Failure.NO_INTERNET_CONNECTION_EN, failure.failureMessage)
    }

    @Test
    fun `timeout exceptions map to timeout failure`() {
        // Arrange
        val throwable = listOf(
            TimeoutException(),
            SocketTimeoutException(),
            InterruptedIOException("timeout"),
            IOException("canceled")
        )

        // Act
        val failures = throwable.map { it.toFailure(Language.ENGLISH) }

        // Assert
        failures.forEach { assertFailure(it, FailureType.TIMEOUT) }
    }

    @Test
    fun `unrecognized exception maps to unknown failure`() {
        // Arrange
        val throwable = IllegalArgumentException("bad input")

        // Act
        val failure = throwable.toFailure(Language.ENGLISH)

        // Assert
        assertFailure(
            failure,
            FailureType.UNKNOWN,
            expectedCode = Failure.NO_CODE_SERVER_ERROR_CODE
        )
        assertEquals(Failure.NO_CODE_SERVER_ERROR_CODE, failure.failureCode)
    }

    @Test
    fun `mapping preserves requested language`() {
        // Arrange
        val throwable = TimeoutException()

        // Act
        val failure = throwable.toFailure(Language.ARABIC)

        // Assert
        assertEquals(Language.ARABIC, failure.language)
        assertEquals(Failure.TIMEOUT_AR, failure.failureMessage)
    }

    private fun assertFailure(
        failure: Failure,
        expectedType: FailureType,
        expectedCode: String = Failure.APP_INTERNAL_ERROR_CODE
    ) {
        assertEquals(FailureRepository.LOCAL, failure.failureRepository)
        assertEquals(expectedType, failure.failureType)
        assertEquals(expectedCode, failure.failureCode)
    }
}
