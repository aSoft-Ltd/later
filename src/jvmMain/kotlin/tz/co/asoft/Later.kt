package tz.co.asoft

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

actual class Later<T> actual constructor(
    @JvmSynthetic
    internal actual val scope: CoroutineScope,
    @JvmSynthetic
    @PublishedApi
    internal actual val deferred: Deferred<T>
) {
    companion object {
        /**
         * Provide a builder that will run another thread
         */
        @JvmStatic
        @JvmOverloads
        fun <T> from(scope: CoroutineScope = LaterScope, builder: Builder<T>): Later<T> = Later(scope, scope.async { builder.build() })
    }

    actual fun wait(): T = runBlocking(scope.coroutineContext) {
        deferred.await()
    }

    fun <S> map(convertor: Convertor<T, S>) = map(transform = { convertor.convert(it) })
}