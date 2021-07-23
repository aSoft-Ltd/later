package later

fun interface LaterExecutor<T> {
    fun execute(resolve: Fun<T>, reject: Fun<Throwable>)
}
