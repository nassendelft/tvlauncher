package nl.ncaj.tvlauncher.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import nl.ncaj.tvlauncher.*
import nl.ncaj.tvlauncher.R
import nl.ncaj.tvlauncher.home.AppLauncherContract.Companion.launch
import nl.ncaj.tvlauncher.updater.AppUpdate

@Composable
private fun LeanbackAppItem(
  app: LeanbackApp,
  onClick: (LeanbackApp) -> Unit,
  modifier: Modifier = Modifier,
) {
  var focused by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(if (focused) 1.3f else 1f)
  val selectedPaddingVertical = remember {
    // make sure that we have the matching padding based on the size ratio
    4.dp * (app.banner.intrinsicSize.height / app.banner.intrinsicSize.width)
  }

  Box(
    modifier = modifier
      .then(if (focused) Modifier.zIndex(1f) else Modifier)
      .scale(scale)
      .onFocusChanged { focused = it.isFocused }
      .focusable()
      .onUserInteraction { onClick(app) }
  ) {
    if (focused) {
      OuterGlow(
        color = Color.Black.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxSize()
      )
    }
    Box(
      modifier = Modifier.padding(
        horizontal = if (focused) 4.dp else 0.dp,
        vertical = if (focused) selectedPaddingVertical else 0.dp
      )
    ) {
      Image(
        painter = app.banner,
        contentDescription = app.name.toString(),
        modifier = Modifier.fillMaxSize()
      )
      if (focused) {
        Canvas(
          modifier = Modifier.fillMaxSize()
        ) {
          drawRect(
            color = app.strokeColor,
            style = Stroke(4.0f)
          )
        }
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

  BoxWithConstraints(
    modifier = modifier.fillMaxHeight()
  ) {
    val cellWidth = remember { (maxWidth - (spacing * (columns + 1))) / columns }
    val cellHeight = remember { cellWidth * itemRatio }

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(spacing),
      contentPadding = PaddingValues(spacing)
    ) {
      itemsIndexed(
        items = items.chunked(columns)
      ) { rowIndex, rowItems ->
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
    modifier = modifier.onUserInteraction { onClick(update) },
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
      .onUserInteraction { onClick() }
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
  val update by viewModel.getAppUpdate()
  val appsState by viewModel.getApps()

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
    (appsState as? FetchDataState.Data)?.value?.let { apps ->
      LeanbackAppGrid(
        items = apps,
        openApplication = { app -> appLauncher.launch(app.packageName) }
      )
    }
  }
}
