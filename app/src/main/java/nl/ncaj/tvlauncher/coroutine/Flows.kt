package nl.ncaj.tvlauncher.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

/**
 * Launches a new coroutine and emits all values of this flow to it.
 */
fun <T> Flow<T>.launchStateIn(
  coroutineScope: CoroutineScope,
  initialValue: T
): StateFlow<T> = MutableStateFlow(initialValue).also { state ->
  this.onEach { state.value = it }.launchIn(coroutineScope)
}
