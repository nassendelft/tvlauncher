package nl.ncaj.tvlauncher.tv

import android.content.ContentValues
import android.database.Cursor
import android.media.tv.TvContract
import java.util.*

/**
 * A convenience class to access [TvContract.WatchNextPrograms]
 * entries in the system content provider.
 *
 * This class makes it easy to insert or retrieve a program from the system content provider,
 * which is defined in [TvContract].
 */
class WatchNextProgram internal constructor(values: ContentValues) : BasePreviewProgram(values) {

  /**
   * @return The value of [TvContract.WatchNextPrograms.COLUMN_LAST_ENGAGEMENT_TIME_UTC_MILLIS]
   * for the program.
   */
  val lastEngagementTimeUtcMillis: Long
    get() = values.getAsLong(TvContract.WatchNextPrograms.COLUMN_LAST_ENGAGEMENT_TIME_UTC_MILLIS)

  override fun equals(other: Any?) = if (other !is WatchNextProgram) false
  else values == other.values

  /**
   * Indicates whether some other WatchNextProgram has any set attribute that is different from
   * this WatchNextProgram's respective attributes. An attribute is considered "set" if its key
   * is present in the ContentValues vector.
   */
  fun hasAnyUpdatedValues(update: WatchNextProgram): Boolean {
    val updateKeys = update.values.keySet()
    for (key in updateKeys) {
      val updateValue = update.values[key]
      val currValue = values[key]
      if (!Objects.deepEquals(updateValue, currValue)) {
        return true
      }
    }
    return false
  }

  override fun toString() = "WatchNextProgram{$values}"

  /**
   * @return The fields of the Program in the ContentValues format to be easily inserted into the
   * TV Input Framework database.
   */
  override fun toContentValues() = toContentValues(false)

  companion object {
    /**
     * Creates a WatchNextProgram object from a cursor including the fields defined in
     * [TvContract.WatchNextPrograms].
     *
     * @param cursor A row from the TV Input Framework database.
     * @return A Program with the values taken from the cursor.
     */
    fun fromCursor(cursor: Cursor): WatchNextProgram {
      val values = getContentValues(cursor)
      var index: Int

      if (cursor.getColumnIndex(TvContract.WatchNextPrograms.COLUMN_WATCH_NEXT_TYPE)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(TvContract.WatchNextPrograms.COLUMN_WATCH_NEXT_TYPE, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(
          TvContract.WatchNextPrograms.COLUMN_LAST_ENGAGEMENT_TIME_UTC_MILLIS
        ).also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(
          TvContract.WatchNextPrograms.COLUMN_LAST_ENGAGEMENT_TIME_UTC_MILLIS,
          cursor.getLong(index)
        )
      }
      return WatchNextProgram(values)
    }

    val PROJECTION = BasePreviewProgram.PROJECTION + arrayOf(
      TvContract.WatchNextPrograms.COLUMN_WATCH_NEXT_TYPE,
      TvContract.WatchNextPrograms.COLUMN_LAST_ENGAGEMENT_TIME_UTC_MILLIS
    )
  }
}
