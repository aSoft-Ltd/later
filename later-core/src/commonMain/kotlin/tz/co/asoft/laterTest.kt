package tz.co.asoft

import kotlinx.coroutines.CoroutineScope

expect fun laterTest(block: suspend CoroutineScope.() -> Any?)
