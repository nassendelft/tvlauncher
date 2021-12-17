package com.example.tvlauncher

import android.app.Application
import androidx.work.Configuration
import com.example.tvlauncher.updater.AppUpdate
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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
