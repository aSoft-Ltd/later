package later

import java.util.function.Function

fun interface LaterExecutor<T> {
    fun execute(resolve: Function<T, Unit>, reject: Function<Throwable, Unit>)
}
