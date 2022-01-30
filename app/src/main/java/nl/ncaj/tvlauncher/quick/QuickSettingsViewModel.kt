package nl.ncaj.tvlauncher.quick

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuickSettingsViewModel @Inject constructor() : ViewModel() {
  val quickSettings = listOf(QuickSetting.RunningApps)
}
