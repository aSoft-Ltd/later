package tz.co.asoft

import kotlinx.coroutines.*

actual val LATER_SCOPE: CoroutineScope = CoroutineScope(SupervisorJob() + LATER_DISPATCHER_JVM)