package tz.co.asoft

import kotlinx.coroutines.*

private val laterThread = newSingleThreadContext("tz.co.asoft.Later")
actual fun loadToNextEventLoop(body: () -> Unit) {
    runBlocking(laterThread) { body() }
}