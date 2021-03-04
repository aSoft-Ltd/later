package later

expect open class Later<T>(executor: ((resolve: (T) -> Unit, reject: ((Throwable) -> Unit)) -> Unit)? = null) :
    BaseLater<T> {
    companion object {
        fun <T> resolve(value: T): Later<T>

        fun reject(error: Throwable): Later<Nothing>
    }
}