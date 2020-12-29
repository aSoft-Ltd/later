package tz.co.asoft

import kotlinx.coroutines.*

val LATER_DISPATCHER: ExecutorCoroutineDispatcher = newSingleThreadContext("tz.co.asoft.LATER_DISPATCHER")

val LATER_SCOPE = CoroutineScope(SupervisorJob() + LATER_DISPATCHER)

actual fun loadToNextEventLoop(body: () -> Unit) {
    LATER_SCOPE.launch { body() }
}