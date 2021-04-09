package later

import kotlinx.coroutines.*

val LATER_SCOPE_NATIVE = CoroutineScope(Dispatchers.Default + SupervisorJob())

actual fun loadToNextEventLoop(body: () -> Unit) {
    LATER_SCOPE_NATIVE.launch { body() }
}