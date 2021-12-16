package com.example.tvlauncher.updater

import android.app.DownloadManager
import android.database.Cursor

/**
 * Convenience function for [DownloadManager.query].
 *
 * @return [DownloadStatus] of given download id
 * @throws IllegalArgumentException if download data could not be read from [DownloadManager]
 * @throws IllegalStateException if download could not be found
 */
fun DownloadManager.getStatus(id: Long): DownloadStatus =
  getStatusOrNull(id) ?: error("Could not find the the download of id '${id}'")

/**
 * Convenience function for [DownloadManager.query].
 *
 * @return [DownloadStatus] or null if no data could be found for given id
 * @throws IllegalArgumentException if data could not be read from [DownloadManager]
 */
fun DownloadManager.getStatusOrNull(id: Long): DownloadStatus? {
  val cursor = query(DownloadManager.Query().setFilterById(id))
  if (!cursor.moveToFirst()) return null
  return cursor.getDownloadStatus()
}

private fun Cursor.getDownloadStatus() =
  when (this.getInt(this.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))) {
    DownloadManager.STATUS_SUCCESSFUL -> {
      val uri = this.getString(this.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
      DownloadStatus.Success(uri)
    }
    DownloadManager.STATUS_PENDING -> {
      DownloadStatus.Pending
    }
    DownloadManager.STATUS_RUNNING -> {
      DownloadStatus.Running
    }
    DownloadManager.STATUS_PAUSED -> {
      this.getInt(this.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON)).asPauseStatus
    }
    DownloadManager.STATUS_FAILED -> {
      this.getInt(this.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON)).asErrorStatus
    }
    else -> {
      DownloadStatus.Unknown
    }
  }

private val Int.asPauseStatus
  get() = when (this) {
    DownloadManager.PAUSED_QUEUED_FOR_WIFI -> DownloadStatus.Paused.QueuedForWifi
    DownloadManager.PAUSED_WAITING_FOR_NETWORK -> DownloadStatus.Paused.WaitingForNetwork
    DownloadManager.PAUSED_WAITING_TO_RETRY -> DownloadStatus.Paused.WaitingToRetry
    else -> DownloadStatus.Paused.Unknown
  }

private val Int.asErrorStatus
  get() = when (this) {
    DownloadManager.ERROR_CANNOT_RESUME -> DownloadStatus.Failure.CannotResume
    DownloadManager.ERROR_FILE_ERROR -> DownloadStatus.Failure.File
    DownloadManager.ERROR_HTTP_DATA_ERROR -> DownloadStatus.Failure.HttpDataError
    DownloadManager.ERROR_INSUFFICIENT_SPACE -> DownloadStatus.Failure.InsufficientSpace
    DownloadManager.ERROR_DEVICE_NOT_FOUND -> DownloadStatus.Failure.DeviceNotFound
    DownloadManager.ERROR_FILE_ALREADY_EXISTS -> DownloadStatus.Failure.FileAlreadyExist
    DownloadManager.ERROR_TOO_MANY_REDIRECTS -> DownloadStatus.Failure.TooManyRedirects
    DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> DownloadStatus.Failure.UnhandledHttpCode
    DownloadManager.ERROR_UNKNOWN -> DownloadStatus.Failure.Unknown
    else -> DownloadStatus.Failure.Http(this)
  }
