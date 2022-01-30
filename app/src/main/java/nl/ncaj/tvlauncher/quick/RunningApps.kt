package nl.ncaj.tvlauncher.quick

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import nl.ncaj.tvlauncher.Button
import nl.ncaj.tvlauncher.R
import nl.ncaj.tvlauncher.Text
import nl.ncaj.tvlauncher.Theme
import nl.ncaj.tvlauncher.onUserInteraction

@Composable
fun RunningApps(
  runningApps: List<RunningApplication>,
  onForceCloseApp: (RunningApplication) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth(fraction = 0.4f)
      .fillMaxHeight(fraction = 0.6f),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(
      modifier = Modifier.height(24.dp)
    )
    Text(
      text = "Force close app",
      style = Theme.typography.h3,
      color = Color.Black
    )
    if (runningApps.isEmpty()) {
      EmptyState(
        modifier = Modifier.fillMaxSize()
      )
    } else {
      val focusRequester = remember { FocusRequester() }
      LaunchedEffect(Unit) { focusRequester.requestFocus() }

      LazyColumn(
        modifier = modifier
          .fillMaxSize()
          .focusRequester(focusRequester),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp)
      ) {
        items(items = runningApps) {
          RunningAppItem(
            app = it,
            onForceCloseApp = onForceCloseApp
          )
        }
      }
    }
  }
}

@Composable
private fun EmptyState(
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "No running apps",
      color = Color.Black
    )
  }
}

@Composable
private fun RunningAppItem(
  app: RunningApplication,
  onForceCloseApp: (RunningApplication) -> Unit,
  modifier: Modifier = Modifier,
) {
  var focused by remember { mutableStateOf(false) }

  Row(
    modifier = modifier
      .onUserInteraction { onForceCloseApp(app) }
      .onFocusChanged { focused = it.isFocused }
      .focusable()
      .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.height(60.dp)
      ) {
        Image(
          painter = app.icon,
          contentDescription = app.name,
        )
        if (focused) {
          Box(
            modifier = Modifier.matchParentSize()
              .alpha(0.8f)
              .background(color = Color.Black)
          )
          Image(
            painter = painterResource(id = R.drawable.ic_outline_cancel_24),
            contentDescription = "Force close",
            colorFilter = ColorFilter.tint(Color.White)
          )
        }
      }
      Spacer(
        modifier = Modifier.height(8.dp)
      )
      Text(
        text = app.name,
        color = Color.Black
      )
    }
  }
}
