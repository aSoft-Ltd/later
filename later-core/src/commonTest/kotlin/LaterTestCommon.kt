import tz.co.asoft.*
import kotlin.test.Test

class LaterTestCommon {
    fun later(value: Int) = BaseLater<Int> { resolve, reject ->
        if (value < 5) reject(Exception("Number($value) is less than 5"))
        else resolve(value)
    }

    fun BaseLater<Int>.process() = error {
        println("Error: ${it.message}")
        5
    }.then {
        println("Got $it")
    }.then { }

    @Test
    fun should_return_basic_values() = laterTest {
        val later1 = later(6)

        val then1 = later1.process()

        val later2 = later(4)

        later2.process()
    }

    @Test
    fun testing_awaiting() = asyncTest {
        val res = later(6).await()
        println("Awaited value was $res")
    }

    @Test
    fun finally_test() = laterTest {
        later(4).error {
            println("Caught error: $it")
            5
        }.complete {
            println("I am certain that this is settled now")
            println(it)
        }
    }
}