package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlin.jvm.JvmName

fun <F, T> Later<F>.map(transform: suspend (F) -> T): Later<T> = Later(
    scope = scope, deferred = scope.async { transform(deferred.await()) }
)

fun <T> later(scope: CoroutineScope = LaterScope, builder: suspend () -> T) = Later(
    scope = scope,
    deferred = scope.async { builder() }
)

@JvmName("laterExtension")
fun <T> CoroutineScope.later(builder: suspend () -> T) = Later(
    scope = this,
    deferred = async { builder() }
)

suspend inline fun <T> Later<T>.await() = deferred.await()