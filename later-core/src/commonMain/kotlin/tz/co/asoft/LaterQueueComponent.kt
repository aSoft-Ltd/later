package tz.co.asoft

internal class LaterQueueComponent(
    var controlledLater: Later<*>,
    var fulfilledFn: ((Any?) -> Any?)?,
    val rejectedFn: ((Throwable) -> Any?)?
)