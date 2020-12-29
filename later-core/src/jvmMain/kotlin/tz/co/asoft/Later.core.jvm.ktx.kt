package tz.co.asoft

import kotlinx.coroutines.*

val LATER_DISPATCHER_JVM: ExecutorCoroutineDispatcher = newSingleThreadContext("tz.co.asoft.LATER_DISPATCHER")

val LATER_SCOPE_JVM = CoroutineScope(SupervisorJob() + LATER_DISPATCHER_JVM)

actual fun loadToNextEventLoop(body: () -> Unit) {
    LATER_SCOPE_JVM.launch { body() }
}