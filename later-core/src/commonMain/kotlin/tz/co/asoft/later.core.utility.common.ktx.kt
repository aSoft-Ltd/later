package tz.co.asoft

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

expect fun loadToNextEventLoop(body: () -> Unit)

@OptIn(ExperimentalContracts::class)
internal fun Any?.isThenable(): Boolean {
    contract {
        returns(true) implies (this@isThenable is BaseLater<*>)
    }
    if (this == null) return false
    if (this is BaseLater<*>) return true
//    if (js("typeof o.then === 'function'")) return true
    return false
}
