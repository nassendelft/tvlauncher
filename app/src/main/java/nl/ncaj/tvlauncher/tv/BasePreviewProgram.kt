package nl.ncaj.tvlauncher.tv

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import java.util.*

/**
 * Base class for derived classes that want to have common fields for preview programs.
 */
abstract class BasePreviewProgram internal constructor(values: ContentValues) : BaseProgram(values) {

  /**
   * @return The internal provider ID for the program.
   * @see PreviewProgramColumns.COLUMN_INTERNAL_PROVIDER_ID
   */
  val internalProviderId
    get() = values.getAsString(PreviewProgramColumns.COLUMN_INTERNAL_PROVIDER_ID)

  /**
   * @return The preview video URI for the program.
   * @see PreviewProgramColumns.COLUMN_PREVIEW_VIDEO_URI
   */
  val previewVideoUri
    get() = values.getAsString(PreviewProgramColumns.COLUMN_PREVIEW_VIDEO_URI)
      ?.let { Uri.parse(it) }

  /**
   * @return The last playback position of the program in millis.
   * @see androidx.tvprovider.media.tv.TvContractCompat
   * .PreviewProgramColumns.COLUMN_LAST_PLAYBACK_POSITION_MILLIS
   */
  val lastPlaybackPositionMillis
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_LAST_PLAYBACK_POSITION_MILLIS)

  /**
   * @return The duration of the program in millis.
   * @see PreviewProgramColumns.COLUMN_DURATION_MILLIS
   */
  val durationMillis
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_DURATION_MILLIS)

  /**
   * @return The intent URI which is launched when the program is selected.
   * @see PreviewProgramColumns.COLUMN_INTENT_URI
   */
  val intentUri
    get() = values.getAsString(PreviewProgramColumns.COLUMN_INTENT_URI)
      .let { Uri.parse(it) }

  /**
   * @return The intent which is launched when the program is selected.
   * @see PreviewProgramColumns.COLUMN_INTENT_URI
   */
  val intent
    get() = values.getAsString(PreviewProgramColumns.COLUMN_INTENT_URI)
      ?.let { Intent.parseUri(it, Intent.URI_INTENT_SCHEME) }

  /**
   * @return Whether the program is transient or not.
   * @see PreviewProgramColumns.COLUMN_TRANSIENT
   */
  val isTransient
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_TRANSIENT) == IS_TRANSIENT

  /**
   * @return The type of the program.
   * @see PreviewProgramColumns.COLUMN_TYPE
   */
  val type
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_TYPE)

  /**
   * @return The TV series item type for the program.
   */
  val tvSeriesItemType
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_TV_SERIES_ITEM_TYPE)

  /**
   * @return The poster art aspect ratio for the program.
   * @see PreviewProgramColumns.COLUMN_POSTER_ART_ASPECT_RATIO
   */
  val posterArtAspectRatio
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_POSTER_ART_ASPECT_RATIO)

  /**
   * @return The thumbnail aspect ratio for the program.
   * @see PreviewProgramColumns.COLUMN_THUMBNAIL_ASPECT_RATIO
   */
  val thumbnailAspectRatio
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_THUMBNAIL_ASPECT_RATIO)

  /**
   * @return The logo URI for the program.
   * @see PreviewProgramColumns.COLUMN_LOGO_URI
   */
  val logoUri
    get() = values.getAsString(PreviewProgramColumns.COLUMN_LOGO_URI)
      ?.let { Uri.parse(it) }

  /**
   * @return The availability of the program.
   * @see PreviewProgramColumns.COLUMN_AVAILABILITY
   */
  val availability
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_AVAILABILITY)

  /**
   * @return The starting price of the program.
   * @see PreviewProgramColumns.COLUMN_STARTING_PRICE
   */
  val startingPrice
    get() = values.getAsString(PreviewProgramColumns.COLUMN_STARTING_PRICE)
      .takeIf { it.isNotBlank() }

  /**
   * @return The offer price of the program.
   * @see PreviewProgramColumns.COLUMN_OFFER_PRICE
   */
  val offerPrice
    get() = values.getAsString(PreviewProgramColumns.COLUMN_OFFER_PRICE)
      .takeIf { it.isNotBlank() }

  /**
   * @return The release date of the program.
   * @see PreviewProgramColumns.COLUMN_RELEASE_DATE
   */
  val releaseDate
    get() = values.getAsString(PreviewProgramColumns.COLUMN_RELEASE_DATE)
      .takeIf { it.isNotBlank() }

  /**
   * @return The item count for the program.
   * @see PreviewProgramColumns.COLUMN_ITEM_COUNT
   */
  val itemCount
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_ITEM_COUNT)

  /**
   * @return Whether the program is live or not.
   * @see PreviewProgramColumns.COLUMN_LIVE
   */
  val isLive
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_LIVE) == IS_LIVE

  /**
   * @return The interaction type for the program.
   * @see PreviewProgramColumns.COLUMN_INTERACTION_TYPE
   */
  val interactionType
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_INTERACTION_TYPE)

  /**
   * @return The interaction count for the program.
   * @see PreviewProgramColumns.COLUMN_INTERACTION_COUNT
   */
  val interactionCount
    get() = values.getAsLong(PreviewProgramColumns.COLUMN_INTERACTION_COUNT)

  /**
   * @return The author for the program.
   * @see PreviewProgramColumns.COLUMN_AUTHOR
   */
  val author
    get() = values.getAsString(PreviewProgramColumns.COLUMN_AUTHOR)?.takeIf { it.isNotBlank() }

  /**
   * @return Whether the program is browsable or not.
   * @see PreviewProgramColumns.COLUMN_BROWSABLE
   */
  val isBrowsable
    get() = values.getAsInteger(PreviewProgramColumns.COLUMN_BROWSABLE) == IS_BROWSABLE

  /**
   * @return The content ID for the program.
   * @see PreviewProgramColumns.COLUMN_CONTENT_ID
   */
  val contentId
    get() = values.getAsString(PreviewProgramColumns.COLUMN_CONTENT_ID)?.takeIf { it.isNotBlank() }

  /**
   * @return The logo content description for the program.
   * @see PreviewProgramColumns.COLUMN_LOGO_CONTENT_DESCRIPTION
   * @see PreviewProgramColumns.COLUMN_LOGO_URI
   */
  val logoContentDescription
    get() = values.getAsString(PreviewProgramColumns.COLUMN_LOGO_CONTENT_DESCRIPTION)
      .takeIf { it.isNotBlank() }

  /**
   * @return The genre for the program.
   * @see PreviewProgramColumns.COLUMN_GENRE
   */
  val genre
    get() = values.getAsString(PreviewProgramColumns.COLUMN_GENRE)?.takeIf { it.isNotBlank() }

  /**
   * @return The start time for the program.
   * @see PreviewProgramColumns.COLUMN_START_TIME_UTC_MILLIS
   */
  val startTimeUtcMillis
    get() = values.getAsLong(PreviewProgramColumns.COLUMN_START_TIME_UTC_MILLIS)

  /**
   * @return The end time for the program.
   * @see PreviewProgramColumns.COLUMN_END_TIME_UTC_MILLIS
   */
  val endTimeUtcMillis
    get() = values.getAsLong(PreviewProgramColumns.COLUMN_END_TIME_UTC_MILLIS)

  /**
   * @return The preview audio URI for the program.
   * @see PreviewProgramColumns.COLUMN_PREVIEW_AUDIO_URI
   */
  val previewAudioUri: Uri?
    get() = values.getAsString(PreviewProgramColumns.COLUMN_PREVIEW_AUDIO_URI)
      ?.let { Uri.parse(it) }

  override fun equals(other: Any?) = if (other !is BasePreviewProgram) {
    false
  } else values == other.values

  /**
   * @return The fields of the BasePreviewProgram in [ContentValues] format to be easily
   * inserted into the TV Input Framework database.
   */
  override fun toContentValues() = toContentValues(false)

  /**
   * Returns fields of the BasePreviewProgram in the ContentValues format to be easily inserted
   * into the TV Input Framework database.
   *
   * @param includeProtectedFields Whether the fields protected by system is included or not.
   */
  open fun toContentValues(includeProtectedFields: Boolean): ContentValues {
    val values = super.toContentValues()
    if (!includeProtectedFields) {
      values.remove(PreviewProgramColumns.COLUMN_BROWSABLE)
    }
    return values
  }

  companion object {
    private const val IS_TRANSIENT = 1
    private const val IS_LIVE = 1
    private const val IS_BROWSABLE = 1

    /**
     * Sets the fields in the cursor to the given builder instance.
     *
     * @param cursor A row from the TV Input Framework database.
     */
    internal fun getContentValues(cursor: Cursor): ContentValues {
      val values = BaseProgram.getContentValues(cursor)
      var index: Int

      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_INTERNAL_PROVIDER_ID)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_INTERNAL_PROVIDER_ID, cursor.getString(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_PREVIEW_VIDEO_URI)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_PREVIEW_VIDEO_URI, cursor.getString(index))
      }
      if (cursor.getColumnIndex(
          PreviewProgramColumns.COLUMN_LAST_PLAYBACK_POSITION_MILLIS
        ).also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_LAST_PLAYBACK_POSITION_MILLIS, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_DURATION_MILLIS)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_DURATION_MILLIS, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_INTENT_URI)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_INTENT_URI, cursor.getString(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_TRANSIENT)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_TRANSIENT, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_TYPE)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_TYPE, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(
          PreviewProgramColumns.COLUMN_POSTER_ART_ASPECT_RATIO
        ).also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_POSTER_ART_ASPECT_RATIO, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_THUMBNAIL_ASPECT_RATIO)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_THUMBNAIL_ASPECT_RATIO, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_LOGO_URI)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_LOGO_URI, cursor.getString(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_AVAILABILITY)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_AVAILABILITY, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_STARTING_PRICE)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_STARTING_PRICE, cursor.getString(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_OFFER_PRICE)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_OFFER_PRICE, cursor.getString(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_RELEASE_DATE)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_RELEASE_DATE, cursor.getString(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_ITEM_COUNT)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_ITEM_COUNT, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_LIVE)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_LIVE, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_INTERACTION_TYPE)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_INTERACTION_TYPE, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_INTERACTION_COUNT)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_INTERACTION_COUNT, cursor.getLong(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_AUTHOR)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_AUTHOR, cursor.getString(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_BROWSABLE)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_BROWSABLE, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_CONTENT_ID)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_CONTENT_ID, cursor.getString(index))
      }
      if (cursor.getColumnIndex(
          PreviewProgramColumns.COLUMN_LOGO_CONTENT_DESCRIPTION
        ).also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(
          PreviewProgramColumns.COLUMN_LOGO_CONTENT_DESCRIPTION,
          cursor.getString(index)
        )
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_GENRE)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_GENRE, cursor.getString(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_START_TIME_UTC_MILLIS)
          .also { index = it }
        >= 0 && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_START_TIME_UTC_MILLIS, cursor.getLong(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_END_TIME_UTC_MILLIS)
          .also { index = it }
        >= 0 && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_END_TIME_UTC_MILLIS, cursor.getLong(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_PREVIEW_AUDIO_URI)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_PREVIEW_AUDIO_URI, cursor.getString(index))
      }
      if (cursor.getColumnIndex(PreviewProgramColumns.COLUMN_TV_SERIES_ITEM_TYPE)
          .also { index = it }
        >= 0 && !cursor.isNull(index)
      ) {
        values.put(PreviewProgramColumns.COLUMN_TV_SERIES_ITEM_TYPE, cursor.getInt(index))
      }

      return values
    }

    val PROJECTION = BaseProgram.PROJECTION + arrayOf(
      PreviewProgramColumns.COLUMN_INTERNAL_PROVIDER_ID,
      PreviewProgramColumns.COLUMN_PREVIEW_VIDEO_URI,
      PreviewProgramColumns.COLUMN_LAST_PLAYBACK_POSITION_MILLIS,
      PreviewProgramColumns.COLUMN_DURATION_MILLIS,
      PreviewProgramColumns.COLUMN_INTENT_URI,
      PreviewProgramColumns.COLUMN_TRANSIENT,
      PreviewProgramColumns.COLUMN_TYPE,
      PreviewProgramColumns.COLUMN_POSTER_ART_ASPECT_RATIO,
      PreviewProgramColumns.COLUMN_THUMBNAIL_ASPECT_RATIO,
      PreviewProgramColumns.COLUMN_LOGO_URI,
      PreviewProgramColumns.COLUMN_AVAILABILITY,
      PreviewProgramColumns.COLUMN_STARTING_PRICE,
      PreviewProgramColumns.COLUMN_OFFER_PRICE,
      PreviewProgramColumns.COLUMN_RELEASE_DATE,
      PreviewProgramColumns.COLUMN_ITEM_COUNT,
      PreviewProgramColumns.COLUMN_LIVE,
      PreviewProgramColumns.COLUMN_INTERACTION_TYPE,
      PreviewProgramColumns.COLUMN_INTERACTION_COUNT,
      PreviewProgramColumns.COLUMN_AUTHOR,
      PreviewProgramColumns.COLUMN_BROWSABLE,
      PreviewProgramColumns.COLUMN_CONTENT_ID,
      PreviewProgramColumns.COLUMN_LOGO_CONTENT_DESCRIPTION,
      PreviewProgramColumns.COLUMN_GENRE,
      PreviewProgramColumns.COLUMN_START_TIME_UTC_MILLIS,
      PreviewProgramColumns.COLUMN_END_TIME_UTC_MILLIS,
      PreviewProgramColumns.COLUMN_PREVIEW_AUDIO_URI,
      PreviewProgramColumns.COLUMN_TV_SERIES_ITEM_TYPE
    )
  }
}
