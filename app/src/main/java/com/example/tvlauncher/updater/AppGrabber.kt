package com.example.tvlauncher.updater

import android.app.Application
import android.app.DownloadManager
import android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE
import android.content.Context
import android.content.IntentFilter
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext

/**
 * This class allows to download of an app release.
 * Call [downloadApp] to start the download.
 * The result can be observed by subscribing to [appDownloads].
 * These result events are dispatch using the [Main] dispatcher.
 *
 * @see AppVersions
 */
internal class AppGrabber(private val context: Context) {

  private val downloadManager = context.getSystemService<DownloadManager>()
    ?: error("Could not get an instance of DownloadManager")

  init {
    check(context is Application) { "context passed should be the application context" }

    // Register DownloadReceiver so that we can events when a download finishes
    context.registerReceiver(
      DownloadReceiver(::notifyDownloadComplete),
      IntentFilter(ACTION_DOWNLOAD_COMPLETE)
    )
  }

  private val _appDownloads = MutableSharedFlow<DownloadStatus>()
  val appDownloads = _appDownloads.asSharedFlow()

  private suspend fun notifyDownloadComplete(id: Long) = withContext(Main) {
    val status = downloadManager.getStatus(id)
    _appDownloads.emit(status)
  }

  /**
   * Starts download of given app release.
   *
   * @return ID needed to interact with this download
   */
  fun downloadApp(release: Release): Long {
    val request = DownloadManager.Request(release.file.url.toUri())
      .setMimeType("application/vnd.android.package-archive")
      .setTitle(context.applicationInfo.loadLabel(context.packageManager))
      .setDescription("Downloading ${release.version}")
    return downloadManager.enqueue(request)
  }
}
