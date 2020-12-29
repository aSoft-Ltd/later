package tz.co.asoft

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

expect fun loadToNextEventLoop(body: () -> Unit)

@OptIn(ExperimentalContracts::class)
fun isThenable(o: Any?): Boolean {
    contract {
        returns(true) implies (o is BaseLater<*>)
    }
    if (o == null) return false
    if (o is BaseLater<*>) return true
//    if (js("typeof o.then === 'function'")) return true
    return false
}
