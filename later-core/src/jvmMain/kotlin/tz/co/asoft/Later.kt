package tz.co.asoft

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual open class Later<T> actual constructor(executor: ((resolve: (T) -> Unit, reject: ((Throwable) -> Unit)) -> Unit)?) : BaseLater<T>(executor) {
    constructor(executor: LaterExecutor<T>) : this({ resolve, reject -> executor.execute(resolve, reject) })

    actual companion object {
        @JvmStatic
        actual fun <T> resolve(value: T) = Later<T> { resolve, _ -> resolve(value) }

        @JvmStatic
        actual fun reject(error: Throwable) = Later<Nothing> { _, reject -> reject(error) }
    }

    fun <S> then(onResolve: Callback<T, S>): Later<S> = then(
        onResolved = { value -> onResolve.invoke(value) },
        onRejected = null
    )

    fun <S> then(onResolve: Callback<T, S>, onReject: Callback<Throwable, S>): Later<S> = then(
        onResolved = { value -> onResolve.invoke(value) },
        onRejected = { error -> onReject.invoke(error) }
    )

    /**
     * Same as calling catch on javascript / kotlin
     */
    fun <S> error(handler: Callback<Throwable, S>): Later<S> = then(
        onResolved = null,
        onRejected = { err -> handler.invoke(err) }
    )

    /**
     * Same as calling finally on javascript / kotlin
     */
    fun complete(handler: Callback<State.Settled, Any?>) = complete { state -> handler.invoke(state) }

    /**
     * Warning: This method blocks the [LATER_DISPATCHER] and just waits for the result
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