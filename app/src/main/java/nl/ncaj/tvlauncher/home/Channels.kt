package nl.ncaj.tvlauncher.home

import android.content.ContentResolver
import android.database.ContentObserver
import android.media.tv.TvContract
import android.os.Handler
import android.os.Looper
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import nl.ncaj.tvlauncher.tv.WatchNextProgram
import javax.inject.Inject

@ViewModelScoped
class Channels @Inject constructor(
  private val contentResolver: ContentResolver
) {

  /**
   * Gets latest watched video that hasn't been fully watch yet.
   * Only returns video's who's intent URI is returned
   */
  fun getLatestContinueWatching() = callbackFlow<WatchNextProgram?> {
    val observer = object : ContentObserver(Handler.createAsync(Looper.getMainLooper())) {
      override fun onChange(selfChange: Boolean) {
        queryLatestContinueWatching()?.let { trySend(it) }
      }
    }

    contentResolver.registerContentObserver(
      TvContract.WatchNextPrograms.CONTENT_URI,
      false,
      observer
    )

    awaitClose { contentResolver.unregisterContentObserver(observer) }
  }.onStart { emit(queryLatestContinueWatching()) }.filterNotNull().flowOn(IO)

  private fun queryLatestContinueWatching() = contentResolver.query(
    TvContract.WatchNextPrograms.CONTENT_URI,
    WatchNextProgram.PROJECTION,
    "${TvContract.WatchNextPrograms.COLUMN_WATCH_NEXT_TYPE} = ? AND ? IS NOT NULL",
    arrayOf(
      TvContract.WatchNextPrograms.WATCH_NEXT_TYPE_CONTINUE.toString(),
      TvContract.WatchNextPrograms.COLUMN_INTENT_URI
    ),
    "${TvContract.WatchNextPrograms.COLUMN_LAST_ENGAGEMENT_TIME_UTC_MILLIS} DESC"
  ).use { cursor ->
    cursor?.moveToFirst()?.takeIf { it }?.let { WatchNextProgram.fromCursor(cursor) }
  }
}
