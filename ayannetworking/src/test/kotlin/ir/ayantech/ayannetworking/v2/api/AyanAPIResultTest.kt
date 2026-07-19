package ir.ayantech.ayannetworking.v2.api

import ir.ayantech.networking.v2.api.AyanAPIResult
import ir.ayantech.networking.v2.api.fold
import ir.ayantech.networking.v2.api.onChangeState
import ir.ayantech.networking.v2.api.onFailure
import ir.ayantech.networking.v2.api.onSuccess
import ir.ayantech.networking.v2.model.ApiCallStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class AyanAPIResultTest {

    @Test
    fun `success contains value and reports only success state`() {
        // Arrange
        val expected = listOf("first", "second")

        // Act
        val result = AyanAPIResult.success(expected)

        // Assert
        assertSame(expected, result.value)
        assertTrue(result.isSuccess)
        assertFalse(result.isError)
        assertFalse(result.isChangeState)
    }

    @Test
    fun `error contains exception and reports only error state`() {
        // Arrange
        val expected = IllegalStateException("request failed")

        // Act
        val result = AyanAPIResult.error<String>(expected)

        // Assert
        assertSame(expected, (result as AyanAPIResult.Error).ayanFailure)
        assertFalse(result.isSuccess)
        assertTrue(result.isError)
        assertFalse(result.isChangeState)
    }

    @Test
    fun `change state contains status and reports only change state`() {
        // Arrange
        val expected = ApiCallStatus.LOADING

        // Act
        val result = AyanAPIResult.changeState<String>(expected)

        // Assert
        assertEquals(expected, (result as AyanAPIResult.ChangeState).state)
        assertFalse(result.isSuccess)
        assertFalse(result.isError)
        assertTrue(result.isChangeState)
    }

    @Test
    fun `callbacks invoke only the matching action`() {
        // Arrange
        val calls = mutableListOf<String>()
        val result: AyanAPIResult<String, ApiCallStatus, Exception> =
            AyanAPIResult.success("payload")

        // Act
        result
            .onSuccess { calls += "success:$it" }
            .onChangeState { calls += "state:$it" }
            .onFailure { calls += "failure:${it.message}" }

        // Assert
        assertEquals(listOf("success:payload"), calls)
    }

    @Test
    fun `fold returns value from matching branch`() {
        // Arrange
        val result: AyanAPIResult<String, ApiCallStatus, Exception> =
            AyanAPIResult.changeState(ApiCallStatus.SUCCESSFUL)

        // Act
        val folded = result.fold(
            onSuccess = { "success:$it" },
            onChangeState = { "state:$it" },
            onError = { "error:${it?.message}" }
        )

        // Assert
        assertEquals("state:SUCCESSFUL", folded)
    }
}
