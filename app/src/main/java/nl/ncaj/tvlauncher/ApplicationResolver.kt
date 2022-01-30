package nl.ncaj.tvlauncher

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.content.Intent.CATEGORY_LEANBACK_LAUNCHER
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Process
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ViewModelScoped
class ApplicationResolver @Inject constructor(
  private val packageManager: PackageManager,
  private val activityManager: ActivityManager,
  @ApplicationContext private val context: Context
) {

  /**
   * List of currently running applications
   */
  private val runningApplications
    get() = (activityManager.runningAppProcesses ?: emptyList())
      .asSequence()
      .flatMap { it.pkgList.asSequence() }
      .distinct()
      .map { packageManager.getApplicationInfo(it, 0) }
      .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 }
      .map { ResolvedApplication(it, packageManager) }
      .toList()

  fun getRunningApplications() = callbackFlow {
    val observer = ActivityManager.OnUidImportanceListener { _, _ ->
        trySend(runningApplications)
    }
    activityManager.addOnUidImportanceListener(observer, IMPORTANCE_GONE)
    awaitClose { activityManager.removeOnUidImportanceListener(observer) }
  }.onStart { emit(runningApplications) }.flowOn(Dispatchers.IO)

  /**
   * Kills any application that is has a running process that is associated with the
   * given package name.
   *
   * @param packageName the package name of an application returned from [runningApplications]
   */
  fun forceStopApplication(packageName: String) {
    activityManager.forceStopPackage(packageName)
  }

  /**
   * Get a list of activity launchers for applications that support Android/Google TV.
   * This is based on a flag in the `AndroidManifest.xml',
   * mainly [Intent.CATEGORY_LEANBACK_LAUNCHER].
   *
   * @return a [kotlinx.coroutines.flow] which can be used to keep track of installed
   * applications that support these intents.
   */
  fun getLeanbackLaunchApplications(withBannersOnly: Boolean = true) = callbackFlow {
    val receiver = AppInstallBroadcastReceiver { trySend(queryLeanbackApps(withBannersOnly)) }
    context.registerReceiver(receiver, AppInstallBroadcastReceiver.intentFilter)
    awaitClose { context.unregisterReceiver(receiver) }
  }.onStart { emit(queryLeanbackApps(withBannersOnly)) }.flowOn(Dispatchers.IO)

  private fun queryLeanbackApps(withBannersOnly: Boolean) =
    packageManager.queryIntentActivities(leanbackAppsIntent, 0)
      .let {
        if (withBannersOnly)
          it.filter { resolved -> resolved.activityInfo.bannerResource != 0 }
        else
          it
      }
      .map { ResolvedApplicationActivity(it.activityInfo, packageManager) }

  private class AppInstallBroadcastReceiver(
    private val onAppChange: () -> Unit
  ) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
      if (!allowedIntents.contains(intent.action)) return
      onAppChange()
    }

    companion object {
      private val allowedIntents = listOf(
        Intent.ACTION_PACKAGE_ADDED,
        Intent.ACTION_PACKAGE_REMOVED,
        Intent.ACTION_PACKAGE_CHANGED,
        Intent.ACTION_PACKAGE_FULLY_REMOVED,
        Intent.ACTION_PACKAGE_REPLACED
      )
      val intentFilter = IntentFilter().apply {
        allowedIntents.forEach { addAction(it) }
        addDataScheme("package")
      }
    }
  }

  class ResolvedApplication(
    private val applicationInfo: ApplicationInfo,
    private val packageManager: PackageManager
  ) {
    val packageName = applicationInfo.packageName

    fun loadLabel() = applicationInfo.loadLabel(packageManager)
    fun loadIcon(): Drawable = applicationInfo.loadIcon(packageManager)
    fun loadBanner(): Drawable? = applicationInfo.loadBanner(packageManager)
    fun loadLogo(): Drawable? = applicationInfo.loadLogo(packageManager)

    @Suppress("DEPRECATION")
    val isGame = applicationInfo.flags and ApplicationInfo.FLAG_IS_GAME != 0 ||
      applicationInfo.category == ApplicationInfo.CATEGORY_GAME
  }

  class ResolvedApplicationActivity(
    private val activityInfo: ActivityInfo,
    private val packageManager: PackageManager
  ) {
    val packageName = activityInfo.packageName
    val resolvedApplication = ResolvedApplication(activityInfo.applicationInfo, packageManager)

    fun loadLabel() = activityInfo.loadLabel(packageManager)
    fun loadIcon(): Drawable = activityInfo.loadIcon(packageManager)
    fun loadBanner(): Drawable? = activityInfo.loadBanner(packageManager)
    fun loadLogo(): Drawable? = activityInfo.loadLogo(packageManager)
  }

  companion object {
    private val leanbackAppsIntent = Intent(ACTION_MAIN)
      .apply { addCategory(CATEGORY_LEANBACK_LAUNCHER) }
  }
}
