package nl.ncaj.tvlauncher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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

  val background = if (focused) {
    Modifier.background(color = colorFocused, shape = RoundedCornerShape(8.dp))
  } else {
    Modifier.border(width = 2.dp, color = color, shape = RoundedCornerShape(8.dp))
  }

  Box(
    modifier = modifier
      .onFocusChanged { focused = it.isFocused }
      .clickable { onClick() }
      .then(background)
  ) {
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
