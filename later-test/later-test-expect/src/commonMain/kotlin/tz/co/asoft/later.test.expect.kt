package tz.co.asoft

import tz.co.asoft.LaterState.PENDING
import tz.co.asoft.LaterState.Settled
import tz.co.asoft.LaterState.Settled.FULFILLED
import tz.co.asoft.LaterState.Settled.REJECTED
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun <S : LaterState<*>> assertionMessage(expectedState: String?, state: S) = """
    
    Expected : Later < $expectedState >
    Actual   : Later < $state >  
    ===============================================
""".trimIndent()

private inline fun <reified S : LaterState<*>> Expectation<BaseLater<*>>.toBe(): S {
    val state = value.state
    assertTrue(assertionMessage(S::class.simpleName, state)) { state is S }
    return try {
        state as S
    } catch (err: Throwable) {
        throw AssertionError(assertionMessage(S::class.simpleName, state))
    }
}

fun Expectation<BaseLater<*>>.toBeSettled() = toBe<Settled<*>>()
fun Expectation<BaseLater<*>>.toBePending() = toBe<PENDING<*>>()
fun <T> Expectation<BaseLater<T>>.toBeFulfilled() = toBe<FULFILLED<T>>()
fun <T> Expectation<BaseLater<T>>.toBeFulfilledWith(value: T): T {
    val state = toBeFulfilled()
    assertEquals(value, state.value)
    return state.value
}

fun Expectation<BaseLater<*>>.toBeRejected() = toBe<REJECTED<*>>()

fun Expectation<BaseLater<*>>.toBeRejectedWith(cause: Throwable) {
    val state = toBeRejected()
    assertEquals(cause, state.cause)
}