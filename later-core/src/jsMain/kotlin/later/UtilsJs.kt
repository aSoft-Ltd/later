package later

external fun setTimeout(handler: dynamic, timeout: Int = definedExternally, vararg arguments: Any?): Int

actual fun loadToNextEventLoop(body: () -> Unit): dynamic = setTimeout(body)