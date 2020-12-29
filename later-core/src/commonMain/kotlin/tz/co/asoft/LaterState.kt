package tz.co.asoft

sealed class LaterState {
    object PENDING : LaterState()
    sealed class Settled : LaterState() {
        data class FULFILLED(val value: Any?) : Settled()
        data class REJECTED(val cause: Throwable) : Settled()
    }
}