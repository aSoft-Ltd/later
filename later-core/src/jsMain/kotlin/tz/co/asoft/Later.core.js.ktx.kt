package tz.co.asoft

fun <T, S> BaseLater<BaseLater<T>>.later(
    onFulfilled: ((T) -> S)
): BaseLater<S> {
    return this.unsafeCast<BaseLater<T>>().then(onFulfilled)
}

fun <T, S> BaseLater<BaseLater<T>>.later(
    onFulfilled: ((T) -> S)?,
    onRejected: ((Throwable) -> S)?
): BaseLater<S> {
    return this.unsafeCast<BaseLater<T>>().then(onFulfilled, onRejected)
}