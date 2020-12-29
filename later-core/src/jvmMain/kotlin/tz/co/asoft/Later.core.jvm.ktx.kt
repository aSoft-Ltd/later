package tz.co.asoft

import kotlinx.coroutines.*

val LATER_DISPATCHER: ExecutorCoroutineDispatcher = newSingleThreadContext("tz.co.asoft.LATER_DISPATCHER")

actual fun loadToNextEventLoop(body: () -> Unit) {
    runBlocking(LATER_DISPATCHER) { body() }
}