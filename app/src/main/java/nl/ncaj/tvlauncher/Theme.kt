package nl.ncaj.tvlauncher

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun Button(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  color: Color = Color.White,
  colorFocused: Color = Color(0xFFEE6CFF),
  block: @Composable () -> Unit
) {
  var focused by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .onUserInteraction { onClick() }
      .onFocusChanged { focused = it.isFocused }
      .focusable()
  ) {
    // uses Canvas instead of border and background modifier here
    // because it looks like there's a bug somewhere in compose that crashes the app
    // as of androidx.compose.ui:ui:1.0.5
    Canvas(
      modifier = Modifier.matchParentSize()
    ) {
      drawRoundRect(
        color = if (focused) colorFocused else color,
        cornerRadius = CornerRadius(16f, 16f),
        style = if (focused) Fill else Stroke(width = 2f)
      )
    }
    block()
  }
}

@Composable
fun Text(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = Color.White,
  style: TextStyle = Theme.typography.body,
  overflow: TextOverflow = TextOverflow.Clip,
  softWrap: Boolean = true,
  maxLines: Int = Int.MAX_VALUE,
) {
  BasicText(
    text = text,
    style = style.copy(color = color),
    modifier = modifier,
    overflow = overflow,
    softWrap = softWrap,
    maxLines = maxLines
  )
}

data class Theme(
  val typography: Typography = Typography()
) {

  data class Typography(
    val h1: TextStyle = TextStyle(
      fontSize = 28.sp
    ),
    val h3: TextStyle = TextStyle(
      fontSize = 22.sp
    ),
    val h6: TextStyle = TextStyle(
      fontSize = 16.sp
    ),
    val body: TextStyle = TextStyle(
      fontSize = 16.sp
    )
  )

  companion object {
    private val instance = Theme()
    val typography = instance.typography
  }
}
