package nl.ncaj.tvlauncher.home

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import nl.ncaj.tvlauncher.*
import nl.ncaj.tvlauncher.R
import nl.ncaj.tvlauncher.home.AppLauncherContract.Companion.launch
import nl.ncaj.tvlauncher.home.UriLauncherContract.launch
import nl.ncaj.tvlauncher.updater.AppUpdate


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
      Text(
        text = "New update is available!",
        style = TextStyle.Default.copy(fontSize = 16.sp)
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

  Button(
    onClick = onClick,
    modifier = modifier.onFocusChanged { focused = it.isFocused }
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(8.dp)
    ) {
      Image(
        painter = painterResource(id = R.drawable.ic_outline_settings_24),
        contentDescription = "Settings",
        colorFilter = ColorFilter.tint(Color.White)
      )
      if (focused) {
        Text(
          text = "Settings",
          modifier = Modifier.padding(start = 8.dp)
        )
      }
    }
  }
}

@Composable
fun Home(
  viewModel: HomeViewModel
) {
  val appLauncher = viewModel.getAppLauncher()
  val installLauncher = viewModel.getUpdateInstallLauncher()
  val settingsLauncher = viewModel.getSettingLauncher()
  val uriLauncher = viewModel.getUriLauncher()
  val update by viewModel.appUpdate.collectAsState(initial = null)
  val categories by viewModel.categories.collectAsState(initial = emptyList())
  val watchNext by viewModel.latestWatched.collectAsState(initial = null)

  LeanbackAppGrid(
    categories = categories,
    openApplication = { app -> appLauncher.launch(app.packageName) },
    headerItem = { modifier ->
      item {
        Header(
          latestWatched = watchNext,
          appUpdate = update,
          modifier = modifier,
          onWatch = { uriLauncher.launch(it) },
          onOpenSettings = { settingsLauncher.launch() },
          onInstallUpdate = { installLauncher.launch(it) }
        )
      }
    }
  )
}

@Composable
private fun Header(
  latestWatched: WatchNext?,
  onWatch: (Uri) -> Unit,
  onOpenSettings: () -> Unit,
  onInstallUpdate: (Uri) -> Unit,
  appUpdate: AppUpdate.Update?,
  modifier: Modifier = Modifier,
) {
  val brush = remember { Brush.verticalGradient(listOf(Color(0x00303030), Color(0xFF303030))) }

  val imagePainter = latestWatched?.let { rememberImagePainter(it.poster) }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(300.dp)
  ) {
    Box(
      modifier = Modifier.matchParentSize()
    ) {
      imagePainter?.let {
        Image(
          painter = imagePainter,
          contentDescription = "Show to watch next",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxWidth()
        )
      }
      Canvas(
        modifier = Modifier.fillMaxSize()
      ) {
        drawRect(brush)
      }
      latestWatched?.let {
        WatchNext(
          next = it,
          onWatch = { show -> onWatch(show.uri) },
          modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(start = 20.dp)
        )
      }
    }
    Row(
      modifier = Modifier.padding(40.dp)
    ) {
      Spacer(
        modifier = Modifier.weight(1.0f)
      )
      appUpdate?.let {
        UpdateBox(
          onClick = { update -> onInstallUpdate(update.fileUri) },
          update = it,
        )
      }
      GearSettings(
        onClick = onOpenSettings
      )
    }
  }
}

@Composable
fun WatchNext(
  next: WatchNext,
  onWatch: (WatchNext) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
  ) {
    Text(
      text = next.title ?: "",
      style = Theme.typography.h1
    )
    next.episode?.let {
      Text(
        text = next.episode
      )
    }
    Button(
      onClick = { onWatch(next) },
      modifier = Modifier.padding(top = 32.dp)
    ) {
      Text(
        text = "Continue watching",
        modifier = Modifier.padding(8.dp)
      )
    }
  }
}
