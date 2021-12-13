package com.example.tvlauncher

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.tvlauncher.ResultCodeLaunchContract.Companion.rememberActivityLauncher

internal data class LeanbackApp(
  val name: CharSequence,
  val banner: Painter,
  val launchContract: ActivityResultContract<Unit, Int>
)

internal val ApplicationResolver.ResolvedApplication.asLeanbackApp
  get() = LeanbackApp(
    name = loadLaunchLabel(),
    banner = (loadBanner() ?: error("Banner required for leanback app")).asPainter,
    launchContract = getLaunchContract()
  )

private val Drawable.asPainter get() = BitmapPainter(this.toBitmap().asImageBitmap())

@Composable
private fun LeanbackAppItem(
  app: LeanbackApp,
  modifier: Modifier = Modifier
) {
  val launcher = rememberActivityLauncher(app.launchContract)
  var focused by remember { mutableStateOf(false) }
  Box(
    modifier = modifier
      .onFocusChanged { focused = it.isFocused }
      .focusable()
  ) {
    Image(
      painter = app.banner,
      contentDescription = app.name.toString(),
      modifier = Modifier
        .fillMaxSize()
        .clickable { launcher.launch() }
        .onKeyEvent {
          if (it.key == Key.Enter) {
            launcher.launch()
            return@onKeyEvent true
          }
          return@onKeyEvent false
        }
    )
    if (focused) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = Color.Red, style = Stroke(3.0f))
      }
    }
  }
}

@Composable
internal fun LeanbackAppGrid(
  items: List<LeanbackApp>,
  modifier: Modifier = Modifier,
  columns: Int = 5,
  spacing: Dp = 20.dp,
  itemRatio: Float = 0.5625f,
) {
  // focusRequest is need to give initial focus
  // to first item on composition
  val focusRequester = remember { FocusRequester() }
  LaunchedEffect(Unit) { focusRequester.requestFocus() }

  BoxWithConstraints(modifier.fillMaxHeight()) {
    val cellWidth = (maxWidth - (spacing * (columns + 1))) / columns
    val cellHeight = cellWidth * itemRatio

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(spacing),
      contentPadding = PaddingValues(spacing)
    ) {
      itemsIndexed(items = items.chunked(columns)) { rowIndex, rowItems ->
        Row(
          modifier = Modifier.fillParentMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
          for ((columnIndex, item) in rowItems.withIndex()) {
            val index = ((columnIndex + 1) * (rowIndex + 1)) - 1
            LeanbackAppItem(
              app = item,
              modifier = Modifier
                .size(cellWidth, cellHeight)
                .then(if (index == 0) Modifier.focusRequester(focusRequester) else Modifier)
            )
          }
        }
      }
    }
  }
}

@Preview(
  showBackground = true,
  widthDp = 1280,
  heightDp = 720,
)
@Composable
fun PreviewAppGrid() {
  val contract = ResultCodeLaunchContract(Intent())
  val banner = painterResource(id = R.drawable.banner)
  val apps = (1 until 13).map { LeanbackApp(it.toString(), banner, contract) }
  LeanbackAppGrid(items = apps)
}
