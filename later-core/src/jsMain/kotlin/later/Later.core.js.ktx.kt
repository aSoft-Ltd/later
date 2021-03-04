package later

import kotlin.js.Promise

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

fun <T> BaseLater<T>.asPromise(): Promise<T> = asDynamic().promise ?: Promise { resolve, reject ->
    then(onResolved = { resolve(it) }, onRejected = { reject(it) })
}

fun <T> Promise<T>.asLater(): Later<T> = asDynamic().later ?: Later { resolve, reject ->
    then(onFulfilled = { resolve(it) }, onRejected = { reject(it) })
}
