package nl.ncaj.tvlauncher.updater

import android.app.DownloadManager
import android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE
import android.app.DownloadManager.EXTRA_DOWNLOAD_ID
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment.DIRECTORY_DOWNLOADS
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class allows to download of an app release.
 * Call [downloadApp] to start the download.
 *
 * @see GithubAppReleases
 */
@Singleton
class AppGrabber @Inject constructor(
  @ApplicationContext private val context: Context,
  private val downloadManager: DownloadManager
) {

  /**
   * Starts download of given app release.
   *
   * @return flow with a single emitted value for the status of the download when it's completed.
   * This can fail to retrieve the status of the download. In this case the flow will
   * emit an exception so make sure you catch with `.catch()`. Will immediately return
   * [DownloadStatus.Success] if file already exist.
   */
  fun downloadApp(release: Release): Flow<DownloadStatus> {
    val file = File(context.getExternalFilesDir(DIRECTORY_DOWNLOADS), release.file.name)

    // if the file is already downloaded we just return a success status
    if(file.exists() && file.length() == release.file.size) {
      return flow { emit(DownloadStatus.Success(file.toUri().toString())) }
    }

    val request = DownloadManager.Request(release.file.url.toUri())
      .setMimeType("application/vnd.android.package-archive")
      .setTitle("Downloading app update")
      .setDescription("version ${release.version}")
      .setDestinationInExternalFilesDir(context, DIRECTORY_DOWNLOADS, release.file.name)
    return getStatus(downloadManager.enqueue(request))
  }

  private fun getStatus(downloadId: Long) = callbackFlow<DownloadStatus> {
    val receiver = DownloadReceiver(downloadId) {
      val status = downloadManager.getStatus(it)
      trySendBlocking(status).onSuccess { channel.close() }
    }
    context.registerReceiver(receiver, IntentFilter(ACTION_DOWNLOAD_COMPLETE))
    awaitClose { context.unregisterReceiver(receiver) }
  }

  /**
   * Listens to [ACTION_DOWNLOAD_COMPLETE] actions received from [DownloadManager]
   *
   * Be aware: If the [DownloadManager] ever sends a broadcast but doesn't include the ID of the
   * download in the intent the [onDownloadReceived] callback is never called. This should be very
   * edge-case however, if this happens there's nothing we can do about it.
   *
   * @param downloadId the ID of the download to check the status of
   * @param onDownloadReceived callback which gets called if the received broadcast has the same
   * id as the one given in [downloadId]
   */
  private class DownloadReceiver(
    private val downloadId: Long,
    private val onDownloadReceived: (Long) -> Unit
  ) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
      if (intent.action != ACTION_DOWNLOAD_COMPLETE) return

      val id = intent.getLongExtra(EXTRA_DOWNLOAD_ID, 0)

      // ignore if it's not the id we're looking for
      if (id != downloadId) return

      onDownloadReceived(id)
    }
  }
}
