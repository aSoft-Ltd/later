@file:JvmName("LaterUtils")

package later

import kotlinx.coroutines.*
import java.util.concurrent.CompletableFuture

val LATER_DISPATCHER_JVM: ExecutorCoroutineDispatcher = newSingleThreadContext("tz.co.asoft.LATER_DISPATCHER")

val LATER_SCOPE_JVM = CoroutineScope(SupervisorJob() + LATER_DISPATCHER_JVM)

actual fun loadToNextEventLoop(body: () -> Unit) {
    LATER_SCOPE_JVM.launch { body() }
}

fun <T> CompletableFuture<T>.asLater(): Later<T> = Later { resolve, reject ->
    whenComplete { value: T?, err: Throwable? ->
        when {
            value != null && err != null -> resolve(value)
            value != null && err == null -> resolve(value)
            value == null && err != null -> reject(err)
            value == null && err == null -> reject(IllegalStateException("Completable future didn't return with value or exception"))
        }
    }
}