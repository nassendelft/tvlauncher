package nl.ncaj.tvlauncher.updater

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * This class can check for updates of new versions of this app.
 * It does this by calling [WorkManager] to do most of the heavy lifting.
 *
 * Use [checkForUpdate] to start the update check process and use the [update] field
 * to check the events that it emits.
 *
 * @param context application context used to to be able to share the downloaded update file
 * with installer
 * @param workManagerProvider provider is given instead of an instance to prevent premature
 * initialization of the [WorkManager]
 *
 * @see AppUpdateWork
 */
@Singleton
class AppUpdate @Inject constructor(
  @ApplicationContext private val context: Context,
  private val workManagerProvider: Provider<WorkManager>
) {

  private val workManager by lazy { workManagerProvider.get() }

  private val _update = MutableStateFlow<Update?>(null)
  val update: StateFlow<Update?> = _update

  private var runningId: UUID? = null

  /**
   * Start to check for updates. New update events are emitted in the [update] flow.
   * It starts off a new process that will download the update if available.
   * When this method is called when already running it does nothing.
   */
  fun checkForUpdate() {
    if (runningId != null) {
      Log.w("AppUpdater", "Already checking for update")
      return
    }
    val request = OneTimeWorkRequestBuilder<AppUpdateWork>().build()
    runningId = request.id
    workManager.enqueue(request)
    val liveData = workManager.getWorkInfoByIdLiveData(request.id)
    liveData.observeForever(getUpdatesObserver(liveData))
  }

  /**
   * Observers all updates from worker until either the work info could not be find,
   * or it completed. If it completed and has found an update the [update] value will be set
   */
  private fun getUpdatesObserver(
    liveData: LiveData<WorkInfo>
  ) = object : Observer<WorkInfo> {
    override fun onChanged(workInfo: WorkInfo?) {
      if (workInfo == null) {
        clear()
        return
      }
      when (workInfo.state) {
        WorkInfo.State.SUCCEEDED -> onCompleted(workInfo)
        WorkInfo.State.ENQUEUED,
        WorkInfo.State.RUNNING,
        WorkInfo.State.BLOCKED -> return // do nothing
        WorkInfo.State.FAILED,
        WorkInfo.State.CANCELLED -> clear()
      }
    }

    private fun onCompleted(workInfo: WorkInfo) {
      val hasUpdate = workInfo.outputData.getBoolean(AppUpdateWork.KEY_HAS_UPDATE, false)
      if (hasUpdate) {
        _update.value = workInfo.outputData.asUpdate
      } else {
        _update.value = null
      }
      clear()
    }

    private fun clear() {
      liveData.removeObserver(this)
      runningId = null
    }
  }

  /**
   * Calls [FileProvider.getUriForFile] to setup the file path in this string for sharing
   * outside this application.
   * This string must be a valid URI.
   */
  private val String.asShareableUri
    get() = FileProvider.getUriForFile(
      context,
      context.packageName + ".apkprovider",
      this.toUri().toFile()
    )

  private val Data.asUpdate
    get() = Update(
      fileUri = getString(AppUpdateWork.KEY_URI)?.asShareableUri
        ?: error("Could not find any value for key '${AppUpdateWork.KEY_URI}'"),
      date = getLong(AppUpdateWork.KEY_DATE, 0).takeIf { it != 0L }?.let { Date(it) }
        ?: error("Could not find any value for key '${AppUpdateWork.KEY_DATE}'"),
      version = getString(AppUpdateWork.KEY_VERSION)
        ?: error("Could not find any value for key '${AppUpdateWork.KEY_VERSION}'")
    )

  data class Update(
    val fileUri: Uri,
    val date: Date,
    val version: String
  )
}
