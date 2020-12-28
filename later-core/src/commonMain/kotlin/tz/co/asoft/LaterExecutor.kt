package tz.co.asoft

fun interface LaterExecutor<T> {
    fun execute(resolve: Callback<T, Unit>, reject: Callback<Throwable, Unit>)
}