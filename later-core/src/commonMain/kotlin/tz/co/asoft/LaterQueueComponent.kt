package tz.co.asoft

internal class LaterQueueComponent(
    var controlledLater: BaseLater<*>,
    var fulfilledFn: ((Any?) -> Any?)?,
    val rejectedFn: ((Throwable) -> Any?)?
)