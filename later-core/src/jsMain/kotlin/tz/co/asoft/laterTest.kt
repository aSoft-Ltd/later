package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

actual fun laterTest(block: suspend CoroutineScope.() -> Any?) = GlobalScope.promise(block = block).unsafeCast<dynamic>()