package tz.co.asoft

import kotlin.js.JsName
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

open class BaseLater<T>(executor: ((resolve: (T) -> Unit, reject: ((Throwable) -> Unit)) -> Unit)? = null) {
    private var thenQueue: MutableList<LaterQueueComponent> = mutableListOf()
    private var finallyQueue: MutableList<LaterQueueComponent> = mutableListOf()

    private var innerState: LaterState = PENDING

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
        thenQueue.add(LaterQueueComponent(controlledLater, onResolved as? (Any?) -> Any?, onRejected))
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

    protected fun cleanUp(cleanUp: (state: Settled) -> Any?): Later<T> {
        val s = innerState
        if (s is Settled) {
            cleanUp(s)
            return when (s) {
                is FULFILLED -> Later.resolve(s.value as T)
                is REJECTED -> Later.reject(s.cause) as Later<T>
            }
        }

        val controlledLater = Later<T>()
        val resolve = { value: Any? ->
            cleanUp(FULFILLED(value))
        }
        val rejected = { err: Throwable ->
            cleanUp(REJECTED(err))
        }
        finallyQueue.add(LaterQueueComponent(controlledLater, resolve, rejected))
        return controlledLater
    }

    @JvmSynthetic
    fun complete(cleanUp: (state: Settled) -> Any?) = cleanUp(cleanUp)

    @JvmSynthetic
    @JsName("finally")
    fun finally(cleanUp: (state: Settled) -> Any?) = cleanUp(cleanUp)

    fun <T> resolveWith(value: T) {
        if (innerState is PENDING) {
            innerState = FULFILLED(value)
            propagateFulfilled(value)
        }
    }

    fun rejectWith(error: Throwable) {
        if (innerState == PENDING) {
            innerState = REJECTED(error)
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
                        onResolved = { v -> controlledLater.resolveWith(v) },
                        onRejected = { error -> controlledLater.rejectWith(error) }
                    )
                } else {
                    controlledLater.resolveWith(valueOrLater)
                }
            } catch (err: Throwable) {
                controlledLater.resolveWith(value)
            }
        }

        finallyQueue.forEach {
            val controlledLater = it.controlledLater
            val fulfilledFn = it.fulfilledFn
            fulfilledFn?.invoke(value)
            controlledLater.resolveWith(value)
        }
        thenQueue.clear()
        finallyQueue.clear()
    }

    private fun propagateRejected(error: Throwable) {
        thenQueue.forEach {
            val controlledLater = it.controlledLater
            try {
                val rejectedFn = it.rejectedFn ?: throw RuntimeException("No catch function")
                val valueOrLater = rejectedFn(error)
                if (isThenable(valueOrLater)) {
                    valueOrLater.then(
                        onResolved = { v -> controlledLater.resolveWith(v) },
                        onRejected = { err -> controlledLater.rejectWith(err) }
                    )
                } else {
                    controlledLater.resolveWith(valueOrLater)
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
