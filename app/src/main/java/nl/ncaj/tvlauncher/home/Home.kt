package nl.ncaj.tvlauncher.home

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import nl.ncaj.tvlauncher.AppLauncherContract.Companion.launch
import nl.ncaj.tvlauncher.R
import nl.ncaj.tvlauncher.updater.AppUpdate

data class LeanbackApp(
  val name: CharSequence,
  val banner: Painter,
  val packageName: String
)

val ApplicationResolver.ResolvedApplication.asLeanbackApp
  get() = LeanbackApp(
    name = loadLaunchLabel(),
    banner = (loadBanner() ?: error("Banner required for leanback app")).asPainter,
    packageName = packageName
  )

private val Drawable.asPainter get() = BitmapPainter(this.toBitmap().asImageBitmap())

@Composable
private fun LeanbackAppItem(
  app: LeanbackApp,
  modifier: Modifier = Modifier,
  onClick: (LeanbackApp) -> Unit = {}
) {
  var focused by remember { mutableStateOf(false) }
  Box(
    modifier = modifier
      .onFocusChanged { focused = it.isFocused }
      .focusable()
      .clickable { onClick(app) }
      .onKeyEvent {
        if (it.key == Key.Enter) {
          onClick(app)
          return@onKeyEvent true
        }
        return@onKeyEvent false
      }
  ) {
    Image(
      painter = app.banner,
      contentDescription = app.name.toString(),
      modifier = Modifier.fillMaxSize()
    )
    if (focused) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = Color.Red, style = Stroke(3.0f))
      }
    }
  }
}

@Composable
private fun LeanbackAppGrid(
  items: List<LeanbackApp>,
  modifier: Modifier = Modifier,
  columns: Int = 5,
  spacing: Dp = 20.dp,
  itemRatio: Float = 0.5625f,
  openApplication: (LeanbackApp) -> Unit = {}
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
                .then(if (index == 0) Modifier.focusRequester(focusRequester) else Modifier),
              onClick = openApplication
            )
          }
        }
      }
    }
  }
}

@Composable
private fun UpdateBox(
  update: AppUpdate.Update,
  onClick: (AppUpdate.Update) -> Unit,
  modifier: Modifier = Modifier
) {
  var updateFocused by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .clickable { onClick(update) }
      .onKeyEvent {
        if (it.key == Key.Enter) {
          onClick(update)
          return@onKeyEvent true
        }
        return@onKeyEvent false
      },
  ) {
    Column(
      modifier = Modifier
        .padding(20.dp)
        .onFocusChanged { updateFocused = it.isFocused }
        .focusable(true)
    ) {
      BasicText(
        text = "New update is available!",
        style = TextStyle.Default.copy(
          color = Color.White,
          fontSize = 16.sp
        )
      )
    }
    Canvas(
      modifier = Modifier.matchParentSize()
    ) {
      drawRect(
        color = if (updateFocused) Color.Red else Color.Black,
        style = Stroke(3.0f)
      )
    }
  }
}

@Composable
fun Home(
  viewModel: HomeViewModel
) {
  val appLauncher = viewModel.getActivityLauncher()
  val installLauncher = viewModel.getUpdateInstallLauncher()
  val update by viewModel.getUpdates()

  Column {
    update?.let {
      UpdateBox(
        onClick = { update -> installLauncher.launch(update.fileUri) },
        update = it,
        modifier = Modifier
          .align(Alignment.End)
          .padding(16.dp)
      )
    }
    LeanbackAppGrid(
      items = viewModel.apps,
      openApplication = { appLauncher.launch(it.packageName) }
    )
  }
}

@Preview(
  showBackground = true,
  widthDp = 1280,
  heightDp = 720,
)
@Composable
fun PreviewAppGrid() {
  val banner = painterResource(id = R.drawable.banner)
  val apps = (1 until 13).map { LeanbackApp(it.toString(), banner, "packageName") }
  LeanbackAppGrid(items = apps)
}
