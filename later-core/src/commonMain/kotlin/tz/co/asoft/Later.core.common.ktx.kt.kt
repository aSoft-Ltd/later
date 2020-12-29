package tz.co.asoft

fun <T, S> BaseLater<T>.then(onResolved: (T) -> S) = then(onResolved, null)