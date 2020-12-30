import kotlinx.coroutines.delay
import tz.co.asoft.*
import kotlin.math.exp
import kotlin.test.Test

class ApiTest {

    @Test
    fun should_convert_to_deferred_and_return_results() = asyncTest {
        val later = later {
            println("Running later")
            delay(1000)
            50
        }
        later.await()
        expect(later).toBeSettled()
        expect(later).toBeFulfilled()
        expect(later) {
            toBeSettled()
            toBeFulfilled()
            toBeFulfilledWith(50)
        }
    }
}