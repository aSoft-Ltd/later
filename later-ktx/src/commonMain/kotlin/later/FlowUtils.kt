package later

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> BaseLater<T>.asFlow(): Flow<T> = flow {
    emit(await())
}