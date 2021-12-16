package com.example.tvlauncher.home

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

/**
 * Creates an intent that will query the system to install the app from the given uri.
 */
object InstallApkResultContract : ActivityResultContract<Uri, Int>() {
  override fun createIntent(
    context: Context,
    input: Uri
  ) = Intent(ACTION_VIEW, input).apply {
    setDataAndType(input, "application/vnd.android.package-archive")
    addFlags(FLAG_ACTIVITY_CLEAR_TASK)
    addFlags(FLAG_ACTIVITY_NEW_TASK)
    addFlags(FLAG_GRANT_READ_URI_PERMISSION)
  }

  override fun parseResult(resultCode: Int, intent: Intent?) = resultCode
}
