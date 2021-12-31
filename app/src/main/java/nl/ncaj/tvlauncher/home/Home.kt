package nl.ncaj.tvlauncher.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.ncaj.tvlauncher.FetchDataState
import nl.ncaj.tvlauncher.R
import nl.ncaj.tvlauncher.home.AppLauncherContract.Companion.launch
import nl.ncaj.tvlauncher.launch
import nl.ncaj.tvlauncher.onUserInteraction
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

  Box(
    modifier = modifier
      .onUserInteraction { onClick() }
      .onFocusChanged { focused = it.isFocused }
      .focusable(true)
      .animateContentSize(tween())
  ) {
    Canvas(
      modifier = Modifier.matchParentSize()
    ) {
      drawRoundRect(
        color = if (focused) Color(0xFFEE6CFF) else Color.White,
        cornerRadius = CornerRadius(16f, 16f),
        style = if (focused) Fill else Stroke(width = 2f)
      )
    }
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
        BasicText(
          text = "Settings",
          style = TextStyle.Default.copy(color = Color.White),
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
  val update by viewModel.getAppUpdate()
  val categories = (viewModel.categories as? FetchDataState.Data)?.value ?: emptyList()

  LeanbackAppGrid(
    categories = categories,
    openApplication = { app -> appLauncher.launch(app.packageName) },
    headerItem = {
      item {
        Row(
          modifier = it.fillMaxWidth()
        ) {
          Spacer(
            modifier = Modifier.weight(1.0f)
          )
          update?.let {
            UpdateBox(
              onClick = { update -> installLauncher.launch(update.fileUri) },
              update = it,
            )
          }
          GearSettings(
            onClick = { settingsLauncher.launch() }
          )
        }
      }
    }
  )
}
