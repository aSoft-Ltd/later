package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val LaterScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.Default) }