package tz.co.asoft

import kotlin.test.assertTrue

inline fun <reified S : LaterState> Expectation<Later<*>>.toBe() {
    val state = value.state
    assertTrue(
        """
    
    Expected : Later < ${S::class.simpleName} >
    Actual   : Later < ${state::class.simpleName} >  
    ===============================================
""".trimIndent()
    ) { state is S }
}