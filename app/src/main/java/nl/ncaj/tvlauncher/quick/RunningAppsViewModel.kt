package nl.ncaj.tvlauncher.quick

import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import nl.ncaj.tvlauncher.ApplicationResolver
import nl.ncaj.tvlauncher.coroutine.launchStateIn
import javax.inject.Inject

@HiltViewModel
class RunningAppsViewModel @Inject constructor(
  private val applicationResolver: ApplicationResolver
) : ViewModel() {

  val runningApps = applicationResolver.getRunningApplications()
    .map { apps -> apps.map { it.asRunningApplication } }
    .launchStateIn(viewModelScope, emptyList())

  fun forceCloseApp(app: RunningApplication) {
    applicationResolver.forceStopApplication(app.packageName)
  }
}

private val ApplicationResolver.ResolvedApplication.asRunningApplication
  get() = RunningApplication(
    name = loadLabel().toString(),
    icon = BitmapPainter((loadBanner() ?: loadIcon()).toBitmap().asImageBitmap()),
    packageName = packageName
  )
