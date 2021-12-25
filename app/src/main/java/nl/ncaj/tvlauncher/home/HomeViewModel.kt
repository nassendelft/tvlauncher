package nl.ncaj.tvlauncher.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import nl.ncaj.tvlauncher.updater.AppUpdate
import nl.ncaj.tvlauncher.updater.InstallApkResultContract
import nl.ncaj.tvlauncher.updater.InstallUpdateLauncher
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  appResolver: ApplicationResolver,
  private val appUpdater: AppUpdate,
  private val launcherContract: AppLauncherContract
) : ViewModel() {
  val apps = appResolver.getLeanbackLaunchApplications().map { it.asLeanbackApp }

  @Composable
  fun getUpdates() = appUpdater.update.collectAsState(null)

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
