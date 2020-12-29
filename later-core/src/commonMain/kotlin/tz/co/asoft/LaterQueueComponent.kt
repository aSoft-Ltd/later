package tz.co.asoft

import kotlin.js.JsExport

@JsExport
internal class LaterQueueComponent(
    var controlledLater: Later<*>,
    var fulfilledFn: ((Any?) -> Any?)?,
    val rejectedFn: ((Throwable) -> Any?)?
)