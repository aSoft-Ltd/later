import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import tz.co.asoft.asyncTest
import kotlin.js.Promise
import kotlin.test.Test

fun laterTest(block: suspend CoroutineScope.() -> Later<*>) = GlobalScope.promise(block = block).unsafeCast<dynamic>()

class LaterTest {

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
        val later = Later<Int> { resolve, reject ->
            console.log("Started executing")
            setTimeout(
                {
//                    resolve(42)
                    reject(RuntimeException("Rejecting For Fun"))
                },
                1000
            )
        }

        val firstThen = later.catch { err ->
            console.log(err.message)
            0
        }.then {
            console.log("Recovered to $it")
            Later.resolve(it + 1)
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

    fun later(value: Int) = Later<Int> { resolve, reject ->
        if (value < 5) reject(Exception("Number($value) is less than 5"))
        else resolve(value)
    }

    fun Later<Int>.process() = catch {
        console.log("Error: ${it.message}")
        5
    }.then {
        console.log("Got $it")
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
        val res = later(4).await()
        console.log("Awaited value was $res")
    }
}