package tz.co.asoft

actual open class Later<T> actual constructor(executor: ((resolve: (T) -> Unit, reject: ((Throwable) -> Unit)) -> Unit)?) : BaseLater<T>(executor) {
    actual companion object {
        actual fun <T> resolve(value: T) = Later<T> { resolve, _ -> resolve(value) }

        actual fun reject(error: Throwable) = Later<Nothing> { _, reject -> reject(error) }
    }
}