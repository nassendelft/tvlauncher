package nl.ncaj.tvlauncher.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nl.ncaj.tvlauncher.FetchDataState
import nl.ncaj.tvlauncher.updater.AppUpdate
import nl.ncaj.tvlauncher.updater.InstallApkResultContract
import nl.ncaj.tvlauncher.updater.InstallUpdateLauncher
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val appResolver: ApplicationResolver,
  private val appUpdater: AppUpdate,
  private val launcherContract: AppLauncherContract
) : ViewModel() {

  private val ApplicationResolver.ResolvedApplication.asLeanbackApp
    get() = (loadBanner() ?: error("Banner required for leanback app")).toBitmap().let { banner ->
      LeanbackApp(
        name = loadLaunchLabel(),
        banner = BitmapPainter(banner.asImageBitmap()),
        packageName = packageName,
        isGame = isGame,
        palette = Palette.from(banner).generate()
      )
    }

  var categories by mutableStateOf<FetchDataState<List<LeanbackCategory>>>(FetchDataState.Fetching())
  private set

  init {
    viewModelScope.launch {
      val launchApps = appResolver.getLeanbackLaunchApplications().map { it.asLeanbackApp }
      val apps = LeanbackCategory(
        label = "Apps",
        apps = launchApps.filter { !it.isGame }
      )
      val games = launchApps.filter { it.isGame }.takeIf { it.isNotEmpty() }?.let {
        LeanbackCategory(
          label = "Games",
          apps = it
        )
      }
      categories = FetchDataState.Data(listOfNotNull(apps, games))
    }
  }

  @Composable
  fun getAppUpdate() = appUpdater.update.collectAsState(null)

  @Composable
  fun getAppLauncher(): AppLauncher =
    rememberLauncherForActivityResult(launcherContract) {
      // if the user returns from this activity we ignore the result
    }

  @Composable
  fun getUpdateInstallLauncher(): InstallUpdateLauncher =
    rememberLauncherForActivityResult(InstallApkResultContract) {
      // never returns RESULT_OK because when the app is update this process is killed
      // RESULT_CANCELLED is ignored
    }

  @Composable
  fun getSettingLauncher(): SettingsLauncher =
    rememberLauncherForActivityResult(SettingsLauncherContract) {
      // if the user returns from this activity we ignore the result
    }
}
