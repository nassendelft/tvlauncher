package nl.ncaj.tvlauncher.home

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import nl.ncaj.tvlauncher.AppLauncherContract
import nl.ncaj.tvlauncher.R

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
fun Home(
  viewModel: HomeViewModel
) {
  val launcher = rememberLauncherForActivityResult(InstallApkResultContract) {
    Log.d("ApkInstall", "Launch install result code: $it")
  }
  val appLauncher = rememberLauncherForActivityResult(viewModel.launcherContract) {
    // will not be called the update is installed because it will stop this app's process
  }

  val uri = viewModel.update?.fileUri

  Column(horizontalAlignment = Alignment.End) {
    Box(Modifier.padding(top = 20.dp, end = 20.dp)) {
      Button(
        modifier = Modifier.alpha(if (uri == null) 0.0f else 1.0f),
        onClick = { if (uri != null) launcher.launch(uri) }) {
        Text("Update to latest version")
      }
    }
    LeanbackAppGrid(
      items = viewModel.apps,
      openApplication = { appLauncher.launch(AppLauncherContract.Input(it.packageName)) }
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
