package later

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import later.LaterState.Settled
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume
import java.util.function.Function
import kotlin.coroutines.resumeWithException

actual open class Later<T> actual constructor(executor: ((resolve: (T) -> Unit, reject: ((Throwable) -> Unit)) -> Unit)?) :
    BaseLater<T>(executor) {
    constructor(executor: LaterExecutor<T>) : this({ resolve, reject -> executor.execute(resolve, reject) })

    actual companion object {
        @JvmStatic
        actual fun <T> resolve(value: T) = Later<T> { resolve, _ -> resolve(value) }

        @JvmStatic
        actual fun reject(error: Throwable) = Later<Nothing> { _, reject -> reject(error) }
    }

    fun <S> then(onResolve: Function<T, S>): Later<S> = then(
        onResolved = { value -> onResolve.apply(value) },
        onRejected = null
    )

    fun <S> then(onResolve: Function<T, S>, onReject: Function<Throwable, S>): Later<S> = then(
        onResolved = { value -> onResolve.apply(value) },
        onRejected = { error -> onReject.apply(error) }
    )

    /**
     * Same as calling catch on javascript or kotlin
     */
    fun <S> error(handler: Function<Throwable, S>): Later<S> = then(
        onResolved = null,
        onRejected = { err -> handler.apply(err) }
    )

    /**
     * Same as calling finally on javascript or kotlin
     */
    fun complete(handler: Fun<Settled<T>>) = complete { state -> handler.invoke(state) }

    /**
     * Warning: This method blocks the [LATER_DISPATCHER_JVM] and just waits for the result
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

    fun asCompletableFuture(): CompletableFuture<T> {
        val future = CompletableFuture<T>()
        then(
            onResolved = { future.complete(it) },
            onRejected = { future.completeExceptionally(it) }
        )
        return future
    }
}