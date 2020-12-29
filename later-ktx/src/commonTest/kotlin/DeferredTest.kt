import kotlinx.coroutines.delay
import tz.co.asoft.*
import kotlin.test.Test

class DeferredTest {

    @Test
    fun should_convert_to_deferred_and_return_results() = asyncTest {
        val later = later {
            println("Running later")
            delay(1000)
            50
        }

        val res1 = later.asDeferred().await()
        expect(res1).toBe(50)

        val res2 = later.await()
        expect(res2).toBe(50)
    }

}