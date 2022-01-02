package nl.ncaj.tvlauncher.home

import android.content.ContentResolver
import android.media.tv.TvContract
import dagger.hilt.android.scopes.ViewModelScoped
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
  fun getLatestContinueWatching() = contentResolver.query(
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
