package later

import expect.BasicExpectation
import later.LaterState.PENDING
import later.LaterState.Settled
import later.LaterState.Settled.FULFILLED
import later.LaterState.Settled.REJECTED
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun <S : LaterState<*>> assertionMessage(expectedState: String?, state: S) = """
    
    Expected : Later < $expectedState >
    Actual   : Later < $state >  
    ===============================================
""".trimIndent()

private inline fun <reified S : LaterState<*>> BasicExpectation<out BaseLater<*>>.stateToBe(): S {
    val state = value.state
    assertTrue(assertionMessage(S::class.simpleName, state)) { state is S }
    return try {
        state as S
    } catch (err: Throwable) {
        throw AssertionError(assertionMessage(S::class.simpleName, state))
    }
}

fun BasicExpectation<out BaseLater<*>>.toBeSettled() = stateToBe<Settled<*>>()

fun BasicExpectation<out BaseLater<*>>.toBePending() = stateToBe<PENDING<*>>()

fun <T> BasicExpectation<out BaseLater<T>>.toBeFulfilled() = stateToBe<FULFILLED<T>>()

fun <T> BasicExpectation<out BaseLater<T>>.toBeFulfilledWith(value: T) {
    val error = AssertionError("Expected state to be fullfilled but was ${this.value.state::class.simpleName}")
    val state = this.value.state as? FULFILLED<T> ?: throw error
    assertEquals(value, state.value)
}

fun BasicExpectation<out BaseLater<*>>.toBeRejected() = stateToBe<REJECTED<*>>()

fun BasicExpectation<out BaseLater<*>>.toBeRejectedWith(cause: Throwable) {
    val error = AssertionError("Expected state to be rejected but was ${this.value.state::class.simpleName}")
    val state = this.value.state as? REJECTED ?: throw error
    assertEquals(cause, state.cause)
}
