import tz.co.asoft.*
import kotlin.js.Promise
import kotlin.test.Test

class LaterTestJS {

    @Test
    fun returns_a_promise_without_error(): dynamic {
        val promise = Promise { resolve: (Int) -> Unit, reject: ((Throwable) -> Unit)? ->
            console.log("Started resolving")
            setTimeout({ resolve(42) }, 1000)
        }

        val firstThen = promise.then {
            console.log("Got 1st value: $it")
            it + 1
        }

        val secondThen = firstThen.then {
            console.log("Got 2nd value: $it")
        }
        return secondThen
    }

    @Test
    fun should_have_a_good_api(): dynamic {
        val later = BaseLater<Int> { resolve, reject ->
            console.log("Started executing")
            setTimeout(
                {
//                    resolve(42)
                    reject(RuntimeException("Rejecting For Fun"))
                },
                1000
            )
        }

        val firstThen = later.error { err ->
            console.log(err.message)
            0
        }.then {
            console.log("Recovered to $it")
            BaseLater.resolve(it + 1)
        }.later {
            console.log("Got $it")
            it
        }

        val secondThen = firstThen.then { value ->
            console.log("Got Value: $value")
            value + 1
        }

        return firstThen
    }

    fun later(value: Int) = BaseLater<Int> { resolve, reject ->
        if (value < 5) reject(Exception("Number($value) is less than 5"))
        else resolve(value)
    }

    fun BaseLater<Int>.process() = error {
        console.log("Error: ${it.message}")
        5
    }.then {
        console.log("Got $it")
    }.then { }

    @Test
    fun should_return_basic_values() = asyncTest {
        val later1 = later(6)

        val then1 = later1.process()

        val later2 = later(4)

        later2.process()
    }

//    @Test
//    fun testing_awaiting() = asyncTest {
//        val res = later(6).await()
//        console.log("Awaited value was $res")
//    }

    @Test
    fun finally_test() = asyncTest {
        later(4).error {
            console.log("Caught error: $it")
        }.finally {
            console.log("I am certain that this is settled now")
            console.log(it)
        }
    }
}