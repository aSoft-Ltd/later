package later

import kotlinx.coroutines.*
import later.LaterState.Settled
import later.LaterState.Settled.FULFILLED
import later.LaterState.Settled.REJECTED
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

expect val LATER_SCOPE: CoroutineScope

fun <T> CoroutineScope.later(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Later<T> {
    val later = Later<T>()
    launch(context, start) {
        try {
            later.resolveWith(block())
        } catch (err: Throwable) {
            later.rejectWith(err)
        }
    }
    return later
}

/**
 * Converts and instance of this [BaseLater] into a [Deferred]
 */
fun <T> BaseLater<T>.asDeferred(): Deferred<T> = LATER_SCOPE.async(start = CoroutineStart.UNDISPATCHED) { await() }

/**
 * Suspends this [Later] and resumes with the result, or exception
 *
 * If this [Later] is already in a [Settled] state,
 * it returns the [FULFILLED.value] immediately or throws the [REJECTED.cause]
 */
suspend fun <T> BaseLater<T>.await(): T = when (val s = state) {
    is Settled -> when (s) {
        is FULFILLED -> s.value
        is REJECTED -> throw s.cause
    }
    is LaterState.PENDING -> suspendCancellableCoroutine { cont ->
        then({ value -> cont.resume(value) }, { err -> cont.resumeWithException(err) })
    }
}

/**
 * Convert's this [Deferred] into a [Later]
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Deferred<T>.asLater() = Later<T> { resolve, reject ->
    invokeOnCompletion {
        when (val e = getCompletionExceptionOrNull()) {
            null -> resolve(getCompleted())
            else -> reject(e)
        }
    }
}
