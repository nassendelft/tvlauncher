package nl.ncaj.tvlauncher.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import nl.ncaj.tvlauncher.coroutine.launchStateIn
import nl.ncaj.tvlauncher.updater.AppUpdate
import nl.ncaj.tvlauncher.updater.InstallApkResultContract
import nl.ncaj.tvlauncher.updater.InstallUpdateLauncher
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val launcherContract: AppLauncherContract,
  appResolver: ApplicationResolver,
  appUpdater: AppUpdate,
  channels: Channels,
) : ViewModel() {

  val latestWatched = channels.getLatestContinueWatching()
    .map { nextProgram ->
      WatchNext(
        nextProgram.previewProgram.program.title,
        nextProgram.previewProgram.program.episodeTitle,
        nextProgram.previewProgram.program.posterArtUri,
        nextProgram.previewProgram.intentUri ?: error("Should not be null")
      )
    }
    .launchStateIn(viewModelScope, null)

  val categories = appResolver.getLeanbackLaunchApplications()
    .map { it.map { app -> app.asLeanbackApp } }
    .map { launchApps ->
      listOfNotNull(
        LeanbackCategory(label = "Apps", apps = launchApps.filter { !it.isGame }),
        launchApps.filter { it.isGame }.takeIf { it.isNotEmpty() }?.let {
          LeanbackCategory(label = "Games", apps = it)
        }
      )
    }
    .launchStateIn(viewModelScope, emptyList())

  val appUpdate = appUpdater.update

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

  @Composable
  fun getUriLauncher(): UriLauncher =
    rememberLauncherForActivityResult(UriLauncherContract) {
      // if the user returns from this activity we ignore the result
    }
}

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

data class WatchNext(
  val title: String?,
  val episode: String?,
  val poster: Uri?,
  val uri: Uri
)
