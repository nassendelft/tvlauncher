package com.example.tvlauncher.updater

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.tvlauncher.BuildConfig.VERSION_NAME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.single
import net.swiftzer.semver.SemVer
import okio.IOException
import javax.inject.Inject

/**
 * [CoroutineWorker] that checks for new version of the app
 * and will download the new version if there's an update.
 */
@HiltWorker
class AppUpdateWork @AssistedInject constructor(
  @Assisted context: Context,
  @Assisted workerParams: WorkerParameters,
  private val appGrabber: AppGrabber,
  private val releases: GithubAppReleases
) : CoroutineWorker(context, workerParams) {

  override suspend fun doWork(): Result {
    try {
      val release = releases.getLatestRelease()

      if (!release.isNewer) {
        val data = workDataOf(KEY_HAS_UPDATE to false)
        return Result.success(data)
      }

      val status = appGrabber.downloadApp(release)
        .catch { emit(DownloadStatus.Failure.Unknown) }
        .single()

      return if (status is DownloadStatus.Success) {
        val data = workDataOf(
          KEY_HAS_UPDATE to true,
          KEY_URI to status.uri,
          KEY_VERSION to release.version,
          KEY_DATE to release.date.time
        )
        Result.success(data)
      } else {
        // TODO pass back the download status to failure result
        Result.failure()
      }
    } catch (exception: IOException) {
      val data = workDataOf(KEY_ERROR to (exception.message ?: exception::class.qualifiedName))
      return Result.failure(data)
    } catch (exception: IllegalStateException) {
      val data = workDataOf(KEY_ERROR to (exception.message ?: exception::class.qualifiedName))
      return Result.failure(data)
    }
  }

  private val Release.isNewer: Boolean
    get() {
      val currentVersion = SemVer.parse(VERSION_NAME)
      val otherVersion = SemVer.parse(version)
      return otherVersion > currentVersion
    }

  class Factory @Inject constructor(
    private val grabber: AppGrabber,
    private val releases: GithubAppReleases
  ) : WorkerFactory() {
    override fun createWorker(
      appContext: Context,
      workerClassName: String,
      workerParameters: WorkerParameters
    )  = if (workerClassName == AppUpdateWork::class.qualifiedName) {
      AppUpdateWork(appContext, workerParameters, grabber, releases)
    } else null
  }

  companion object {
    const val KEY_HAS_UPDATE = "com.example.tvlauncher.updater.KEY_HAS_UPDATE"
    const val KEY_URI = "com.example.tvlauncher.updater.KEY_URI"
    const val KEY_VERSION = "com.example.tvlauncher.updater.KEY_VERSION"
    const val KEY_DATE = "com.example.tvlauncher.updater.KEY_DATE"
    const val KEY_ERROR = "com.example.tvlauncher.updater.KEY_ERROR"
  }
}
