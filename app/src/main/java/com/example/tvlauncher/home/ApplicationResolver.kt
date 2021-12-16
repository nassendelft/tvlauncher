package com.example.tvlauncher.home

import android.content.Intent
import android.content.Intent.ACTION_MAIN
import android.content.Intent.CATEGORY_LEANBACK_LAUNCHER
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.activity.result.contract.ActivityResultContract
import com.example.tvlauncher.AppLauncherContract
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ApplicationResolver @Inject constructor(
  private val packageManager: PackageManager
) {

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
  }
}
