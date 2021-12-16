package com.example.tvlauncher.updater

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class can check for updates of new versions of this app.
 * It does this by calling [WorkManager] to do most of the heavy lifting.
 *
 * Use [checkForUpdate] to start the update check process and use the [update] field
 * to check the events that it emits.
 *
 * @see AppUpdateWork
 */
@Singleton
class AppUpdate @Inject constructor(
  private val workManager: WorkManager
) {

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

  fun notifyInstalled() {
    _update.value = null
  }

  private val Data.asUpdate
    get() = Update(
      fileUri = getString(AppUpdateWork.KEY_URI)?.toUri()
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
