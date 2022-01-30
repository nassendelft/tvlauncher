package nl.ncaj.tvlauncher.quick

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import nl.ncaj.tvlauncher.Text

fun NavGraphBuilder.quickSettingsNavigation(
  route: String,
  navController: NavController
) {
  navigation(
    startDestination = "selection",
    route = route
  ) {
    dialog(
      route = "selection",
      dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
      val viewModel = hiltViewModel<QuickSettingsViewModel>()
      QuickSettings(
        modifier = Modifier.background(color = Color.White, shape = RoundedCornerShape(20.dp)),
        items = viewModel.quickSettings,
        navController = navController
      )
    }
    dialog(
      route = "running-apps",
      dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
      val viewModel = hiltViewModel<RunningAppsViewModel>()
      val apps by viewModel.runningApps.collectAsState()
      RunningApps(
        modifier = Modifier.background(color = Color.White, shape = RoundedCornerShape(20.dp)),
        runningApps = apps,
        onForceCloseApp = viewModel::forceCloseApp
      )
    }
  }
}

private val QuickSetting.asNavRoute get() = when (this) {
  QuickSetting.RunningApps -> "running-apps"
}

@Composable
private fun QuickSettings(
  items: List<QuickSetting>,
  navController: NavController,
  modifier: Modifier = Modifier
) {
  val focusRequester = remember { FocusRequester() }
  LaunchedEffect(Unit) { focusRequester.requestFocus() }

  Box(
    modifier = modifier
      .fillMaxSize(fraction = 0.4f)
      .focusRequester(focusRequester),
  ) {
      QuickSettingsGrid(
        settings = items,
        onSettingSelected = { navController.navigate(it.asNavRoute) }
      )
  }
}

@Composable
private fun QuickSettingsGrid(
  settings: List<QuickSetting>,
  onSettingSelected: (QuickSetting) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(16.dp)
  ) {
    items(
      items = settings.chunked(4)
    ) {
      Row {
        for (quickSetting in it) {
          QuickSettingsGridItem(
            setting = quickSetting,
            onSettingSelected = onSettingSelected
          )
        }
      }
    }
  }
}

@Composable
private fun QuickSettingsGridItem(
  setting: QuickSetting,
  onSettingSelected: (QuickSetting) -> Unit,
  modifier: Modifier = Modifier
) {
  var focused by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .onFocusChanged { focused = it.isFocused }
      .clickable { onSettingSelected(setting) }
      .then(if (focused) Modifier.border(width = 1.dp, color = Color.Red) else Modifier)
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(
      painter = painterResource(id = setting.iconResId),
      contentDescription = setting.label,
    )
    Spacer(
      modifier = Modifier.height(8.dp)
    )
    Text(
      text = setting.label,
      color = Color.Black
    )
  }
}
