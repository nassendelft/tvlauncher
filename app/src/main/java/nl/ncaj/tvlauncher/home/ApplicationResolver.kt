package nl.ncaj.tvlauncher.home

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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@ViewModelScoped
class ApplicationResolver @Inject constructor(
  private val packageManager: PackageManager,
  @ApplicationContext private val context: Context
) {

  fun getLeanbackLaunchApplications(withBannersOnly: Boolean = true) = callbackFlow {
    val receiver = AppInstallBroadcastReceiver { trySend(queryApps(withBannersOnly)) }
    context.registerReceiver(receiver, AppInstallBroadcastReceiver.intentFilter)
    awaitClose { context.unregisterReceiver(receiver) }
  }.onStart { emit(queryApps(withBannersOnly)) }.flowOn(Dispatchers.IO)

  private fun queryApps(withBannersOnly: Boolean) =
    packageManager.queryIntentActivities(leanbackAppsIntent, 0)
      .let {
        if (withBannersOnly)
          it.filter { resolved -> resolved.activityInfo.bannerResource != 0 }
        else
          it
      }
      .map { ResolvedApplication(it.activityInfo, packageManager) }

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
    private val activityInfo: ActivityInfo,
    private val packageManager: PackageManager
  ) {
    val packageName: String = activityInfo.packageName

    @Suppress("DEPRECATION")
    val isGame = activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_IS_GAME != 0 ||
      activityInfo.applicationInfo.category == ApplicationInfo.CATEGORY_GAME

    fun loadApplicationLabel() = activityInfo.applicationInfo.loadLabel(packageManager)
    fun loadLaunchLabel() = activityInfo.loadLabel(packageManager)
    fun loadIcon(): Drawable = activityInfo.loadIcon(packageManager)
    fun loadBanner(): Drawable? = activityInfo.loadBanner(packageManager)
    fun loadLogo(): Drawable? = activityInfo.loadLogo(packageManager)
  }

  companion object {
    private val leanbackAppsIntent = Intent(ACTION_MAIN)
      .apply { addCategory(CATEGORY_LEANBACK_LAUNCHER) }
  }
}
