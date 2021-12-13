package com.example.tvlauncher

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.activity.result.contract.ActivityResultContract

class ApplicationResolver(context: Context) {
  private val packageManager = context.applicationContext.packageManager

  fun getLeanbackLaunchApplications(
    withBannersOnly: Boolean = true
  ): List<ResolvedApplication> {
    val intent = Intent(ACTION_MAIN).apply { addCategory(CATEGORY_LEANBACK_LAUNCHER) }
    return packageManager.queryIntentActivities(intent, 0)
      .let {
        if (withBannersOnly)
          it.filter { resolved -> resolved.activityInfo.bannerResource != 0 }
        else
          it
      }
      .map { ResolvedApplication(it.activityInfo, packageManager) }
  }

  class ResolvedApplication(
    private val activityInfo: ActivityInfo,
    private val packageManager: PackageManager
  ) {
    val packageName: String get() = activityInfo.packageName

    fun loadApplicationLabel() = activityInfo.applicationInfo.loadLabel(packageManager)
    fun loadLaunchLabel() = activityInfo.loadLabel(packageManager)
    fun loadIcon(): Drawable = activityInfo.loadIcon(packageManager)
    fun loadBanner(): Drawable? = activityInfo.loadBanner(packageManager)
    fun loadLogo(): Drawable? = activityInfo.loadLogo(packageManager)

    fun getLaunchContract(preferLeanback: Boolean = true): ActivityResultContract<Unit, Int> {
      val intent = if (preferLeanback) {
        packageManager.getLeanbackLaunchIntentForPackage(packageName)
          ?: packageManager.getLaunchIntentForPackage(packageName)
      } else {
        packageManager.getLaunchIntentForPackage(packageName)
      } ?: error("Could not find intent to start app")

      return ResultCodeLaunchContract(intent)
    }
  }
}
