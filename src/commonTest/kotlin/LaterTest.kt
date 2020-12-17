import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import tz.co.asoft.Later
import kotlin.test.Test

class LaterTest {

    @Test
    fun should_have_a_good_api() {
        val l = Later(deferred = GlobalScope.async { 1 })
    }
}