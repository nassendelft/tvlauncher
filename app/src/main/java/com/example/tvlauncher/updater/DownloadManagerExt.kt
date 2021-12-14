package com.example.tvlauncher.updater

import android.app.DownloadManager

/**
 * Convenience function for [DownloadManager.query].
 *
 * @return [DownloadStatus] of given download id
 * @throws IllegalArgumentException if download data could not be read from [DownloadManager]
 * @throws IllegalStateException if download could not be found
 */
internal fun DownloadManager.getStatus(id: Long): DownloadStatus =
  getStatusOrNull(id) ?: error("Could not find the the download of id '${id}'")

/**
 * Convenience function for [DownloadManager.query].
 *
 * @return [DownloadStatus] or null if no data could be found for given id
 * @throws IllegalArgumentException if data could not be read from [DownloadManager]
 */
internal fun DownloadManager.getStatusOrNull(id: Long): DownloadStatus? {
  val cursor = query(DownloadManager.Query().setFilterById(id))

  return if (cursor.moveToFirst()) {
    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
    if (status == DownloadManager.STATUS_SUCCESSFUL) {
      val uri = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))
      DownloadStatus.Success(uri)
    } else if (status == DownloadManager.STATUS_PENDING) {
      DownloadStatus.Pending
    } else if (status == DownloadManager.STATUS_RUNNING) {
      DownloadStatus.Running
    } else if (status == DownloadManager.STATUS_PAUSED) {
      cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON)).asPauseStatus
    } else {
      val reasonCode = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON))
      if (status == DownloadManager.STATUS_FAILED) {
        DownloadStatus.Failure.Http(reasonCode)
      } else {
        reasonCode.asErrorStatus
      }
    }
  } else null
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
    DownloadManager.ERROR_CANNOT_RESUME -> DownloadStatus.Failure.Error.CannotResume
    DownloadManager.ERROR_FILE_ERROR -> DownloadStatus.Failure.Error.File
    DownloadManager.ERROR_HTTP_DATA_ERROR -> DownloadStatus.Failure.Error.HttpDataError
    DownloadManager.ERROR_INSUFFICIENT_SPACE -> DownloadStatus.Failure.Error.InsufficientSpace
    DownloadManager.ERROR_DEVICE_NOT_FOUND -> DownloadStatus.Failure.Error.DeviceNotFound
    DownloadManager.ERROR_FILE_ALREADY_EXISTS -> DownloadStatus.Failure.Error.FileAlreadyExist
    DownloadManager.ERROR_TOO_MANY_REDIRECTS -> DownloadStatus.Failure.Error.TooManyRedirects
    DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> DownloadStatus.Failure.Error.UnhandledHttpCode
    else -> DownloadStatus.Failure.Error.Unknown
  }
