package nl.ncaj.tvlauncher

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract
import javax.inject.Inject

/**
 * Contract to just start an application of given package name.
 *
 * @param packageManager an instance of this application's [PackageManager]
 */
class AppLauncherContract @Inject constructor(
  private val packageManager: PackageManager
) : ActivityResultContract<AppLauncherContract.Input, Int>() {

  override fun createIntent(context: Context, input: Input) = if (input.preferLeanback) {
    packageManager.getLeanbackLaunchIntentForPackage(input.packageName)
      ?: packageManager.getLaunchIntentForPackage(input.packageName)
  } else {
    packageManager.getLaunchIntentForPackage(input.packageName)
  } ?: error("Could not find intent to start app")

  override fun parseResult(resultCode: Int, intent: Intent?) = resultCode

  data class Input(val packageName: String, val preferLeanback: Boolean = true)
}
