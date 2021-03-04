@file:JsExport

package later

import kotlin.js.JsExport
import later.LaterState.Settled.FULFILLED
import later.LaterState.Settled.REJECTED

/**
 * ### State of a [Later]
 * An encapsulation representing a [Later.state]
 *
 * A [Later] always begin on a [PENDING] state.
 *
 * When it runs, and finishes off it's work it gets [Settled]
 * in either a [FULFILLED] or [REJECTED] state
 */
sealed class LaterState<T> {

    /**
     * Initial [LaterState]. Every later begins at this state
     */
    class PENDING<T> : LaterState<T>() {
        override fun toString() = "PENDING"
    }

    /**
     * Encapsulation of a settled stated.
     *
     * This may be either [FULFILLED] or [REJECTED]
     */
    sealed class Settled<T> : LaterState<T>() {
        /**
         * A state representing a successful completion of a [Later]
         * @param value The value that this [Later] completed with
         */
        data class FULFILLED<T>(val value: T) : Settled<T>()

        /**
         * A state representing a failed completion of a [Later]
         * @param cause The [Throwable] that caused the failure
         */
        data class REJECTED<T>(val cause: Throwable) : Settled<T>()
    }
}
