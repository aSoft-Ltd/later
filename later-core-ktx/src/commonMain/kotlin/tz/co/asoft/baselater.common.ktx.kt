package tz.co.asoft

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

expect val LATER_SCOPE: CoroutineScope

fun <T> CoroutineScope.later(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Later<T> = async(context, start, block).asLater()

/**
 * Converts and instance of this [BaseLater] into a [Deferred]
 */
suspend fun <T> BaseLater<T>.asDeferred(): Deferred<T> = LATER_SCOPE.async(start = CoroutineStart.UNDISPATCHED) {
    await()
}

/**
 * Suspends this [Later] and resumes with the result, or exception
 */
suspend fun <T> BaseLater<T>.await() = suspendCancellableCoroutine<T> { cont ->
    then(
        onResolved = { value -> cont.resume(value) },
        onRejected = { err -> cont.resumeWithException(err) }
    )
}

/**
 * Convert's this [Deferred] into a [Later]
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Deferred<T>.asLater(): Later<T> {
    val later = Later<T> { resolve, reject ->
        invokeOnCompletion {
            val e = getCompletionExceptionOrNull()
            if (e != null) {
                reject(e)
            } else {
                resolve(getCompleted())
            }
        }
    }
    return later
}