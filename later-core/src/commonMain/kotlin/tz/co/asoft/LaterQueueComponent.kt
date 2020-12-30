package tz.co.asoft

internal class LaterQueueComponent<T>(
    var controlledLater: Later<T>,
    var fulfilledFn: ((Any?) -> T?)?,
    val rejectedFn: ((Throwable) -> T?)?
)