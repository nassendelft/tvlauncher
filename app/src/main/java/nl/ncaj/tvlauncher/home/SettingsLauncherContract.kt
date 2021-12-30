package nl.ncaj.tvlauncher.home

import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_SETTINGS
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

/**
 * Contract to open the system settings screen
 */
object SettingsLauncherContract : ActivityResultContract<Unit, Int>() {
  override fun createIntent(context: Context, input: Unit) = Intent(ACTION_SETTINGS)
  override fun parseResult(resultCode: Int, intent: Intent?) = resultCode
}

typealias SettingsLauncher = ManagedActivityResultLauncher<Unit, Int>
