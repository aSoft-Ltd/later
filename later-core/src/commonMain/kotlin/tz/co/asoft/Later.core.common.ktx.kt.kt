package tz.co.asoft

fun <T, S> Later<T>.then(onResolved: (T) -> S) = then(onResolved, null)