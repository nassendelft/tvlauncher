package nl.ncaj.tvlauncher.home

import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import nl.ncaj.tvlauncher.R
import nl.ncaj.tvlauncher.home.AppLauncherContract.Companion.launch
import nl.ncaj.tvlauncher.launch
import nl.ncaj.tvlauncher.onEnterKeyEvent
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
  val scale by animateFloatAsState(if (focused) 1.2f else 1f)
  Box(
    modifier = modifier
      .scale(scale)
      .onFocusChanged { focused = it.isFocused }
      .focusable()
      .clickable { onClick(app) }
      .onEnterKeyEvent { onClick(app) }
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
      .onEnterKeyEvent { onClick(update) },
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
private fun GearSettings(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  var focused by remember { mutableStateOf(false) }
  Row(
    modifier = modifier
      .clickable { onClick() }
      .onEnterKeyEvent { onClick() }
      .onFocusChanged { focused = it.isFocused }
      .focusable(true),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_outline_settings_24),
      contentDescription = "Settings",
      colorFilter = ColorFilter.tint(if (focused) Color.Red else Color.White)
    )
    Spacer(
      modifier = Modifier.width(8.dp)
    )
    BasicText(
      text = "Settings",
      style = TextStyle.Default.copy(color = Color.Red),
      modifier = Modifier.alpha(if (focused) 1.0f else 0.0f)
    )
  }
}

@Composable
fun Home(
  viewModel: HomeViewModel
) {
  val appLauncher = viewModel.getAppLauncher()
  val installLauncher = viewModel.getUpdateInstallLauncher()
  val settingsLauncher = viewModel.getSettingLauncher()
  val update by viewModel.getUpdates()

  Column {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      GearSettings(
        onClick = { settingsLauncher.launch() }
      )
      Spacer(
        modifier = Modifier.weight(1.0f)
      )
      update?.let {
        UpdateBox(
          onClick = { update -> installLauncher.launch(update.fileUri) },
          update = it,
        )
      }
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
