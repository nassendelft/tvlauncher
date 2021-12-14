package com.example.tvlauncher.updater

/**
 * Convenience class for [android.app.DownloadManager] status values.
 */
sealed class DownloadStatus {

  /**
   * Indicates when the download finished successfully.
   */
  data class Success(val uri: String) : DownloadStatus()

  /**
   * Indicates when download is paused.
   */
  sealed class Paused : DownloadStatus() {

    /**
     * Indicates when the download is paused because some network error
     * occurred and the download manager is waiting before retrying the request.
     */
    object WaitingToRetry: DownloadStatus()

    /**
     * Indicates when the download is waiting for network connectivity to
     * proceed.
     */
    object WaitingForNetwork: DownloadStatus()

    /**
     * Indicates when the download exceeds a size limit for downloads over
     * the mobile network and the download manager is waiting for a Wi-Fi connection to proceed.
     */
    object QueuedForWifi: DownloadStatus()

    /**
     * Indicates when the download is paused for some other reason.
     */
    object Unknown: DownloadStatus()
  }

  /**
   * Indicates when a download is pending.
   */
  object Pending: DownloadStatus()

  /**
   * Indicates when a download is running.
   */
  object Running: DownloadStatus()

  /**
   * Indicates when a download failed.
   */
  sealed class Failure : DownloadStatus() {

    /**
     * Indicates when download failed because of a server error
     * @param code the HTTP status code
     */
    data class Http(val code: Int) : Failure()

    /**
     * Indicates when download failed because of an application error.
     */
    sealed class Error : Failure() {

      /**
       * Indicates when the download has completed with an error that doesn't fit
       * under any other error code.
       */
      object Unknown: Error()

      /**
       * Indicates when a storage issue arises which doesn't fit under any
       * other error code. Use the more specific [InsufficientSpace] and
       * [DeviceNotFound] when appropriate.
       */
      object File: Error()

      /**
       * Indicates when an HTTP code was received that download manager
       * can't handle.
       */
      object UnhandledHttpCode: Error()

      /**
       * Indicates when an error receiving or processing data occurred at
       * the HTTP level.
       */
      object HttpDataError: Error()

      /**
       * Indicates when there were too many redirects.
       */
      object TooManyRedirects: Error()

      /**
       * Indicates when there was insufficient storage space. Typically,
       * this is because the SD card is full.
       */
      object InsufficientSpace: Error()

      /**
       * Indicates when no external storage device was found. Typically,
       * this is because the SD card is not mounted.
       */
      object DeviceNotFound: Error()

      /**
       * Indicates when some possibly transient error occurred but we can't resume the download.
       */
      object CannotResume: Error()

      /**
       * Indicate when the requested destination file already exists (the
       * download manager will not overwrite an existing file).
       */
      object FileAlreadyExist: Error()
    }
  }
}
