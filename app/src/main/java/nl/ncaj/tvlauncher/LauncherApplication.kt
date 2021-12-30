package nl.ncaj.tvlauncher

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import nl.ncaj.tvlauncher.updater.AppUpdate
import javax.inject.Inject

@HiltAndroidApp
class LauncherApplication : Application(), Configuration.Provider {

  @Inject
  lateinit var workerConfiguration: Configuration

  @Inject
  lateinit var appUpdate: AppUpdate

  override fun onCreate() {
    super.onCreate()
    appUpdate.checkForUpdate()
  }

  override fun getWorkManagerConfiguration() = workerConfiguration
}
