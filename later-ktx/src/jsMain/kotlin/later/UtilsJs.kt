package later

import kotlinx.coroutines.*

actual val LATER_SCOPE: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)