import later.BaseLater
import later.loadToNextEventLoop
import later.then
import test.*
import kotlin.test.Test

class LaterTestCommon {
    fun later(value: Int) = BaseLater<Int> { resolve, reject ->
        loadToNextEventLoop {
            if (value < 5) reject(Exception("Number($value) is less than 5"))
            else resolve(value)
        }
    }

    fun BaseLater<Int>.process() = error {
        println("Error: ${it.message}")
        5
    }.then {
        println("Got $it")
    }.then { }

    @Test
    fun should_return_basic_values() = asyncTest {
        val later1 = later(6)

        val then1 = later1.process()

        val later2 = later(4)

        later2.process()
    }

    @Test
    fun finally_test() = asyncTest {
        later(4).error {
            println("Caught error: $it")
            5
        }.complete {
            println("I am certain that this is settled now")
            println(it)
        }
    }
}