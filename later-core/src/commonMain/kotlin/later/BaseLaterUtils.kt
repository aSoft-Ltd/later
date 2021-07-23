package later

fun <T, S> BaseLater<T>.then(onResolved: (T) -> S) = then(onResolved, null)