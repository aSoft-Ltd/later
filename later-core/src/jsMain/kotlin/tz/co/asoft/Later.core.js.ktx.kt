package tz.co.asoft

fun <T, S> Later<Later<T>>.later(
    onFulfilled: ((T) -> S)
): Later<S> {
    return this.unsafeCast<Later<T>>().then(onFulfilled)
}

fun loadToNextEvenLoop(body: () -> Unit) = setTimeout(body)

fun <T, S> Later<Later<T>>.later(
    onFulfilled: ((T) -> S)?,
    onRejected: ((Throwable) -> S)?
): Later<S> {
    return this.unsafeCast<Later<T>>().then(onFulfilled, onRejected)
}