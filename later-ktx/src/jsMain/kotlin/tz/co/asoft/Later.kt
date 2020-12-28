@file:Suppress("PackageDirectoryMismatch")

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.Promise

external fun setTimeout(handler: dynamic, timeout: Int = definedExternally, vararg arguments: Any?): Int

class LaterMaterial(
    var controlledLater: Later<*>,
    var fulfilledFn: ((Any?) -> Any?)?,
    val rejectedFn: ((Throwable) -> Any?)?
)

@OptIn(ExperimentalContracts::class)
fun isThenable(o: Any?): Boolean {
    contract {
        returns(true) implies (o is Later<*>)
    }
    if (o == null) return false
    if (o is Later<*>) return true
    if (js("typeof o.then === 'function'")) return true
    return false
}

//@JsExport
class Later<T> {
    private var thenQueue: MutableList<LaterMaterial> = mutableListOf()
    private var finallyQueue: MutableList<LaterMaterial> = mutableListOf()

    private var state: State = State.PENDING

    sealed class State {
        object PENDING : State()
        class FULFILLED(val value: Any?) : State()
        class REJECTED(val cause: Throwable) : State()
    }

    constructor(executor: (resolve: (T) -> Unit, reject: ((Throwable) -> Unit)) -> Unit) {
        setTimeout({
            try {
                executor(::onFulfilled, ::onRejected)
            } catch (err: Throwable) {
            }
        })
    }

    constructor()

    companion object {
        fun <T> resolve(value: T) = Later<T> { resolve, _ -> resolve(value) }
        fun reject(error: Throwable) = Later<Nothing> { _, reject -> reject(error) }
    }

    @JsName("then")
    fun <S> then(onResolved: ((T) -> S)?, onRejected: ((Throwable) -> S)?): Later<S> {
        val controlledLater = Later<S>()
        thenQueue.add(LaterMaterial(controlledLater, onResolved as? (Any?) -> Any?, onRejected))
        when (val s = state) {
            is State.FULFILLED -> propagateFulfilled(s.value)
            is State.REJECTED -> propagateRejected(s.cause)
        }
        return controlledLater
    }

    @JsName("catch")
    fun <S> catch(handler: (Throwable) -> S) = then(null, handler)

    @JsName("finally")
    fun finally() {

    }

    private fun onFulfilled(value: Any?) {
        if (state is State.PENDING) {
            state = State.FULFILLED(value)
            propagateFulfilled(value)
        }
    }

    private fun onRejected(error: Throwable) {
        if (state == State.PENDING) {
            state = State.REJECTED(error)
            propagateRejected(error)
        }
    }

    private fun propagateFulfilled(value: Any?) {
        thenQueue.forEach {
            val controlledLater = it.controlledLater
            val fulfilledFn = it.fulfilledFn
            try {
                val valueOrLater = fulfilledFn?.invoke(value) ?: throw RuntimeException("No fulfilled function provided")
                if (isThenable(valueOrLater)) {
                    valueOrLater.then(
                        onResolved = { v -> controlledLater.onFulfilled(v) },
                        onRejected = { error -> controlledLater.onRejected(error) }
                    )
                } else {
                    controlledLater.onFulfilled(valueOrLater)
                }
            } catch (err: Throwable) {
                controlledLater.onFulfilled(value)
            }
        }
        thenQueue.clear()
    }

    private fun propagateRejected(error: Throwable) {
        thenQueue.forEach {
            val controlledLater = it.controlledLater
            try {
                val rejectedFn = it.rejectedFn ?: throw RuntimeException("No catch function")
                val valueOrLater = rejectedFn(error)
                if (isThenable(valueOrLater)) {
                    valueOrLater.then(
                        onResolved = { v -> controlledLater.onFulfilled(v) },
                        onRejected = { err -> controlledLater.onRejected(err) }
                    )
                } else {
                    controlledLater.onFulfilled(valueOrLater)
                }
            } catch (err: Throwable) {
                controlledLater.onRejected(err)
            }
        }
        thenQueue.clear()
    }
}

fun <T, S> Later<T>.then(onResolved: (T) -> S) = then(onResolved, null)

fun <T, S> Later<Later<T>>.later(
    onFulfilled: ((T) -> S)
): Later<S> {
    return this.unsafeCast<Later<T>>().then(onFulfilled)
}

fun <T, S> Later<Later<T>>.later(
    onFulfilled: ((T) -> S)?,
    onRejected: ((Throwable) -> S)?
): Later<S> {
    return this.unsafeCast<Later<T>>().then(onFulfilled, onRejected)
}

suspend fun <T> Later<T>.await() = suspendCancellableCoroutine<T> { cont ->
    then(
        onResolved = { value -> cont.resume(value) },
        onRejected = { err -> cont.resumeWithException(err) }
    )
}
