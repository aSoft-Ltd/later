package later

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
val LATER_SCOPE_NATIVE = CoroutineScope(Dispatchers.Unconfined + SupervisorJob())

actual fun loadToNextEventLoop(body: () -> Unit) {
    LATER_SCOPE_NATIVE.launch { body() }
}