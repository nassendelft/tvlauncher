package com.example.tvlauncher.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvlauncher.AppLauncherContract
import com.example.tvlauncher.updater.AppUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  appResolver: ApplicationResolver,
  private val appUpdater: AppUpdate,
  val launcherContract: AppLauncherContract
) : ViewModel() {
  var apps by mutableStateOf<List<LeanbackApp>>(emptyList())
    private set

  var update by mutableStateOf<AppUpdate.Update?>(null)

  init {
    apps = appResolver.getLeanbackLaunchApplications().map { it.asLeanbackApp }
    viewModelScope.launch {
      appUpdater.update.collect { update = it }
    }
  }

  fun notifyAppInstalled() = appUpdater.notifyInstalled()
}
