package tz.co.asoft

import kotlinx.coroutines.*
import kotlin.js.Promise

actual class Later<T> actual constructor(
    actual val scope: CoroutineScope,
    actual val deferred: Deferred<T>
) : Deferred<T> by deferred {

    companion object {
        fun <T> from(scope: CoroutineScope = LaterScope, promise: Promise<T>) = Later(
            scope = scope,
            deferred = scope.async { promise.await() }
        )
    }

    /**
     * Returns *completed* result or throws [IllegalStateException] if this deferred value has not
     * [completed][isCompleted] yet. It throws the corresponding exception if this deferred was [cancelled][isCancelled].
     *
     * This function is designed to be used from [invokeOnCompletion] handlers, when there is an absolute certainty that
     * the value is already complete. See also [getCompletionExceptionOrNull].
     *
     * **Note: This is an experimental api.** This function may be removed or renamed in the future.
     */
    actual fun wait(): T = deferred.getCompleted()

    fun <S> catch(handler: (Throwable) -> S): Later<S> = from(scope = scope, asPromise().catch(handler))

    fun <S> then(completer: (T) -> S): Later<S> = map { completer(it) }

    fun finally(completer: (T) -> Unit) {
        asPromise().then(completer)
    }
}