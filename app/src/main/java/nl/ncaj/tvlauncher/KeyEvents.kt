package nl.ncaj.tvlauncher

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*

private val acceptKeys = listOf(
  Key.Enter,
  Key.DirectionCenter
)

/**
 * Calls [onEnter] when the key pressed is of [Key.Enter]
 * and the event is of [KeyEventType.KeyUp].
 *
 * @param onEnter the lambda that is called when the above criteria is met
 */
fun Modifier.onEnterKeyEvent(
  onEnter: () -> Unit
) = this.onKeyEvent {
  if (it.type == KeyEventType.KeyUp && acceptKeys.contains(it.key)) {
    onEnter()
    return@onKeyEvent true
  }
  return@onKeyEvent false
}
