import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import later.asFlow
import later.later
import tz.co.asoft.*
import kotlin.test.Test

class FlowTest {
    @Test
    fun should_convert_to_a_flow_and_yield_results() = asyncTest {
        later {
            println("Running later")
            delay(1000)
            50
        }.asFlow().collect {
            expect(it).toBe(50)
        }
    }

    @Test
    fun should_catch_errors_well() = asyncTest {
        later {
            println("Running later")
            delay(1000)
            throw Exception("Failed for fun")
            50
        }.asFlow().catch {
            println(it)
        }.collect {
            println("Finished with: $it")
        }
    }
}