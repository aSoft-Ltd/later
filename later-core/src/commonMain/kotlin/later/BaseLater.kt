package later

import later.LaterState.PENDING
import later.LaterState.Settled
import later.LaterState.Settled.FULFILLED
import later.LaterState.Settled.REJECTED
import kotlin.js.JsName
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

open class BaseLater<T>(executor: ((resolve: (T) -> Unit, reject: ((Throwable) -> Unit)) -> Unit)? = null) {
    private var thenQueue: MutableList<LaterQueueComponent<*>> = mutableListOf()
    private var finallyQueue: MutableList<LaterQueueComponent<*>> = mutableListOf()

    private var innerState: LaterState<T> = PENDING()

    val state get() = innerState

    init {
        loadToNextEventLoop {
            try {
                executor?.invoke(::resolveWith, ::rejectWith)
            } catch (err: Throwable) {
                rejectWith(err)
            }
        }
    }

    companion object {
        @JvmStatic
        fun <T> resolve(value: T) = BaseLater<T> { resolve, _ -> resolve(value) }

        @JvmStatic
        fun reject(error: Throwable) = BaseLater<Nothing> { _, reject -> reject(error) }
    }

    @JsName("then")
    @JvmSynthetic
    fun <S> then(onResolved: ((T) -> S)?, onRejected: ((Throwable) -> S)?): Later<S> {
        val controlledLater = Later<S>()
        thenQueue.add(LaterQueueComponent(controlledLater, onResolved as? (Any?) -> S, onRejected))
        when (val s = innerState) {
            is FULFILLED -> propagateFulfilled(s.value)
            is REJECTED -> propagateRejected(s.cause)
        }
        return controlledLater
    }

    @JvmSynthetic
    fun <S> error(handler: (Throwable) -> S) = then(null, handler)

    @JvmSynthetic
    @JsName("catch")
    fun <S> catch(handler: (Throwable) -> S) = error(handler)

    protected fun cleanUp(cleanUp: (state: Settled<T>) -> Any?): Later<T> {
        val s = innerState
        if (s is Settled) {
            cleanUp(s)
            return when (s) {
                is FULFILLED -> Later.resolve(s.value)
                is REJECTED -> Later.reject(s.cause) as Later<T>
            }
        }

        val controlledLater = Later<T>()
        val resolve = { value: T ->
            cleanUp(FULFILLED(value))
            value
        }
        val rejected = { err: Throwable ->
            cleanUp(REJECTED(err))
            err as T
        }
        finallyQueue.add(LaterQueueComponent(controlledLater, resolve as? (Any?) -> T, rejected))
        return controlledLater
    }

    @JvmSynthetic
    fun complete(cleanUp: (state: Settled<T>) -> Any?) = cleanUp(cleanUp)

    @JvmSynthetic
    @JsName("finally")
    fun finally(cleanUp: (state: Settled<T>) -> Any?) = cleanUp(cleanUp)

    fun resolveWith(value: T) {
        if (innerState is PENDING) {
            try {
                innerState = FULFILLED(value as T)
                propagateFulfilled(value)
            } catch (err: Throwable) {
                rejectWith(err)
            }
        }
    }

    fun rejectWith(error: Throwable) {
        if (innerState is PENDING) {
            innerState = REJECTED(error)
            propagateRejected(error)
        }
    }

    private fun propagateFulfilled(value: Any?) {
        thenQueue.forEach {
            val controlledLater = it.controlledLater as Later<Any?>
            val fulfilledFn = it.fulfilledFn
            try {
                val valueOrLater = fulfilledFn?.invoke(value) ?: throw RuntimeException("No fulfilled function provided")
                when {
                    valueOrLater.isThenable() -> valueOrLater.then(
                        onResolved = { v -> controlledLater.resolveWith(v) },
                        onRejected = { error -> controlledLater.rejectWith(error) }
                    )
                    else -> controlledLater.resolveWith(valueOrLater)
                }
            } catch (err: Throwable) {
                controlledLater.resolveWith(value)
            }
        }

        finallyQueue.forEach {
            val controlledLater = it.controlledLater as Later<Any?>
            val fulfilledFn = it.fulfilledFn
            fulfilledFn?.invoke(value)
            controlledLater.resolveWith(value)
        }
        thenQueue.clear()
        finallyQueue.clear()
    }

    private fun propagateRejected(error: Throwable) {
        thenQueue.forEach {
            val controlledLater = it.controlledLater as Later<Any?>
            try {
                val rejectedFn = it.rejectedFn ?: throw RuntimeException("No catch function")
                val valueOrLater = rejectedFn(error)
                when {
                    valueOrLater.isThenable() -> valueOrLater.then(
                        onResolved = { v -> controlledLater.resolveWith(v) },
                        onRejected = { err -> controlledLater.rejectWith(err) }
                    )
                    else -> controlledLater.resolveWith(valueOrLater)
                }
            } catch (err: Throwable) {
                controlledLater.rejectWith(err)
            }
        }

        finallyQueue.forEach {
            val controlledLater = it.controlledLater
            val rejectedFn = it.rejectedFn
            rejectedFn?.invoke(error)
            controlledLater.rejectWith(error)
        }
        thenQueue.clear()
        finallyQueue.clear()
    }
}
