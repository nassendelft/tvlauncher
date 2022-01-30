package nl.ncaj.tvlauncher.tv

import android.content.ContentValues
import android.database.Cursor
import android.media.tv.TvContract

/**
 * A convenience class to access [TvContract.WatchNextPrograms]
 * entries in the system content provider.
 */
data class WatchNextProgram(
  val previewProgram: PreviewProgram,
  /**
   * @return The value of [TvContract.WatchNextPrograms.COLUMN_LAST_ENGAGEMENT_TIME_UTC_MILLIS]
   * for the program.
   */
  val lastEngagementTimeUtcMillis: Long?
) {

  companion object {
    fun fromContentValues(values: ContentValues) = WatchNextProgram(
      PreviewProgram.fromContentValues(values),
      values.getAsLong(TvContract.WatchNextPrograms.COLUMN_LAST_ENGAGEMENT_TIME_UTC_MILLIS)
    )

    /**
     * Creates a WatchNextProgram object from a cursor including the fields defined in
     * [TvContract.WatchNextPrograms].
     *
     * @param cursor A row from the TV Input Framework database.
     * @return A Program with the values taken from the cursor.
     */
    fun fromCursor(cursor: Cursor) = fromContentValues(Program.getContentValues(cursor))

    val PROJECTION = PreviewProgram.PROJECTION + arrayOf(
      TvContract.WatchNextPrograms.COLUMN_WATCH_NEXT_TYPE,
      TvContract.WatchNextPrograms.COLUMN_LAST_ENGAGEMENT_TIME_UTC_MILLIS
    )
  }
}
