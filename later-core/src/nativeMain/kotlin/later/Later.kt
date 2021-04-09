package later

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import later.LaterState.Settled
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual open class Later<T> actual constructor(executor: ((resolve: (T) -> Unit, reject: ((Throwable) -> Unit)) -> Unit)?) :
    BaseLater<T>(executor) {

    actual companion object {
        actual fun <T> resolve(value: T) = Later<T> { resolve, _ -> resolve(value) }
        actual fun reject(error: Throwable) = Later<Nothing> { _, reject -> reject(error) }
    }

    /**
     * Warning: This method blocks the [Despatcher.Default] and just waits for the result
     * This causes other later methods (if any are available) to stop executing
     * until this [Later] settles
     *
     * Advice: Consider using then(res=>{ TODO() })
     */
    fun wait(): T = runBlocking {
        suspendCancellableCoroutine { cont ->
            then(
                onResolved = { value -> cont.resume(value) },
                onRejected = { err -> cont.resumeWithException(err) }
            )
        }
    }
}