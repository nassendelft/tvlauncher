package nl.ncaj.tvlauncher

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*

private val acceptKeys = listOf(
  Key.Enter,
  Key.DirectionCenter
)

/**
 * Calls [onInteraction] when the key pressed is of one of [acceptKeys]
 * and the event is of [KeyEventType.KeyUp] or on click/touch event.
 *
 * @param onInteraction the lambda that is called when the above criteria is met
 */
fun Modifier.onUserInteraction(
  onInteraction: () -> Unit
) = this
  .clickable { onInteraction() }
  .onKeyEvent {
  if (it.type == KeyEventType.KeyUp && acceptKeys.contains(it.key)) {
    onInteraction()
    return@onKeyEvent true
  }
  return@onKeyEvent false
}
