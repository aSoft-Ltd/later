@file:JsExport

package tz.co.asoft

import kotlin.js.JsExport

sealed class LaterState

/**
 * Encapsulation of a settled stated
 * this may be either [FULFILLED] or [Settled]
 */
sealed class Settled : LaterState()

object PENDING : LaterState()
data class FULFILLED(val value: Any?) : Settled()
data class REJECTED(val cause: Throwable) : Settled()
