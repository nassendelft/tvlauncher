package com.example.tvlauncher.updater

import android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE
import android.app.DownloadManager.EXTRA_DOWNLOAD_ID
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.runBlocking

/**
 * Listens to [ACTION_DOWNLOAD_COMPLETE] actions received from [android.app.DownloadManager]
 */
class DownloadReceiver(
  private val onDownloadReceived: suspend (Long) -> Unit
) : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) = runBlocking {
    if (intent.action != ACTION_DOWNLOAD_COMPLETE) return@runBlocking

    val id = intent.getLongExtra(EXTRA_DOWNLOAD_ID, 0)
    if (id == 0L) {
      Log.w("DownloadReceiver", "Received a download id of '0'")
      return@runBlocking
    }

    onDownloadReceived(id)
  }
}
