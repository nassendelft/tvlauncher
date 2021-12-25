package nl.ncaj.tvlauncher.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import javax.inject.Inject

/**
 * Contract to start an application of given package name.
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

  /**
   * Input for [AppLauncherContract]
   *
   * @param packageName the package name of the app to start
   * @param preferLeanback if the app has a leanback launcher prefer that over a 'normal' one
   */
  data class Input(
    val packageName: String, val
    preferLeanback: Boolean = true
  )

  companion object {

    /**
     * Convenience function for [ManagedActivityResultLauncher.launch] without
     * the need to create a new instance of [Input] ourselves.
     *
     * @param packageName the package name of the app to start
     * @param preferLeanback if the app has a leanback launcher prefer that over a 'normal' one
     */
    fun ManagedActivityResultLauncher<Input, Int>.launch(
      packageName: String,
      preferLeanback: Boolean = true
    ) = launch(Input(packageName, preferLeanback))
  }
}

typealias AppLauncher = ManagedActivityResultLauncher<AppLauncherContract.Input, Int>
