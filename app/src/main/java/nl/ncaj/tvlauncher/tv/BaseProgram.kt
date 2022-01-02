package nl.ncaj.tvlauncher.tv

import android.content.ContentValues
import android.database.Cursor
import android.media.tv.TvContentRating
import android.media.tv.TvContract.BaseTvColumns
import android.media.tv.TvContract.Programs
import android.net.Uri
import android.util.Log

abstract class BaseProgram(protected val values: ContentValues) {

  /**
   * The ID for the program.
   * @see BaseTvColumns._ID
   */
  val id get() = values.getAsLong(BaseTvColumns._ID)

  /**
   * The package name for the program.
   * @see BaseTvColumns.COLUMN_PACKAGE_NAME
   */
  val packageName get() = values.getAsString(BaseTvColumns.COLUMN_PACKAGE_NAME)

  /**
   * The title for the program.
   * @see Programs.COLUMN_TITLE
   */
  val title get() = values.getAsString(Programs.COLUMN_TITLE)?.takeIf { it.isNotBlank() }

  /**
   * @return The episode title for the program.
   * @see Programs.COLUMN_EPISODE_TITLE
   */
  val episodeTitle
    get() = values.getAsString(Programs.COLUMN_EPISODE_TITLE)?.takeIf { it.isNotBlank() }

  /**
   * @return The season display number for the program.
   * @see Programs.COLUMN_SEASON_DISPLAY_NUMBER
   */
  val seasonNumber
    get() = values.getAsString(Programs.COLUMN_SEASON_DISPLAY_NUMBER)?.takeIf { it.isNotBlank() }

  /**
   * @return The episode display number for the program.
   * @see Programs.COLUMN_EPISODE_DISPLAY_NUMBER
   */
  val episodeNumber
    get() = values.getAsString(Programs.COLUMN_EPISODE_DISPLAY_NUMBER)?.takeIf { it.isNotBlank() }

  /**
   * @return The short description for the program.
   * @see Programs.COLUMN_SHORT_DESCRIPTION
   */
  val description
    get() = values.getAsString(Programs.COLUMN_SHORT_DESCRIPTION)?.takeIf { it.isNotBlank() }

  /**
   * @return The long description for the program.
   * @see Programs.COLUMN_LONG_DESCRIPTION
   */
  val longDescription
    get() = values.getAsString(Programs.COLUMN_LONG_DESCRIPTION)?.takeIf { it.isNotBlank() }

  /**
   * @return The video width for the program.
   * @see Programs.COLUMN_VIDEO_WIDTH
   */
  val videoWidth
    get() = values.getAsInteger(Programs.COLUMN_VIDEO_WIDTH)

  /**
   * @return The video height for the program.
   * @see Programs.COLUMN_VIDEO_HEIGHT
   */
  val videoHeight
    get() = values.getAsInteger(Programs.COLUMN_VIDEO_HEIGHT)

  /**
   * @return The canonical genre for the program.
   * @see Programs.COLUMN_CANONICAL_GENRE
   */
  val canonicalGenres
    get() = values.getAsString(Programs.COLUMN_CANONICAL_GENRE)
      ?.let { Programs.Genres.decode(it) }

  /**
   * @return The content rating for the program.
   * @see Programs.COLUMN_CONTENT_RATING
   */
  val contentRatings
    get() = values.getAsString(Programs.COLUMN_CONTENT_RATING)
      ?.let { stringToContentRatings(it) }

  /**
   * @return The poster art URI for the program.
   * @see Programs.COLUMN_POSTER_ART_URI
   */
  val posterArtUri
    get() = values.getAsString(Programs.COLUMN_POSTER_ART_URI)
      ?.let { Uri.parse(it) }

  /**
   * @return The thumbnail URI for the program.
   * @see Programs.COLUMN_THUMBNAIL_URI
   */
  val thumbnailUri
    get() = values.getAsString(Programs.COLUMN_POSTER_ART_URI)
      ?.let { Uri.parse(it) }

  /**
   * @return The internal provider data for the program.
   * @see Programs.COLUMN_INTERNAL_PROVIDER_DATA
   */
  val internalProviderDataByteArray get() = values.getAsByteArray(Programs.COLUMN_INTERNAL_PROVIDER_DATA)

  /**
   * @return The audio languages for the program.
   * @see Programs.COLUMN_AUDIO_LANGUAGE
   */
  val audioLanguages
    get() = values.getAsString(Programs.COLUMN_AUDIO_LANGUAGE)
      ?.let { stringToAudioLanguages(it) }

  /**
   * @return Whether the program is searchable or not.
   * @see Programs.COLUMN_SEARCHABLE
   */
  val searchable
    get() = values.getAsInteger(Programs.COLUMN_SEARCHABLE)
      ?.let { it == IS_SEARCHABLE } ?: true

  /**
   * @return The first internal provider flag for the program.
   * @see Programs.COLUMN_INTERNAL_PROVIDER_FLAG1
   */
  val internalProviderFlag1
    get() = values.getAsLong(Programs.COLUMN_INTERNAL_PROVIDER_FLAG1)

  /**
   * @return The second internal provider flag for the program.
   * @see Programs.COLUMN_INTERNAL_PROVIDER_FLAG2
   */
  val internalProviderFlag2 get() = values.getAsLong(Programs.COLUMN_INTERNAL_PROVIDER_FLAG2)

  /**
   * @return The third internal provider flag for the program.
   * @see Programs.COLUMN_INTERNAL_PROVIDER_FLAG3
   */
  val internalProviderFlag3 get() = values.getAsLong(Programs.COLUMN_INTERNAL_PROVIDER_FLAG3)

  /**
   * @return The forth internal provider flag for the program.
   * @see Programs.COLUMN_INTERNAL_PROVIDER_FLAG4
   */
  val internalProviderFlag4 get() = values.getAsLong(Programs.COLUMN_INTERNAL_PROVIDER_FLAG4)

  /**
   * @return The season title for the program.
   * @see Programs.COLUMN_SEASON_TITLE
   */
  val getSeasonTitle
    get() = values.getAsString(Programs.COLUMN_SEASON_TITLE)?.takeIf { it.isNotBlank() }

  /**
   * @return The review rating style for the program.
   * @see android.media.tv.TvContract.Programs.COLUMN_REVIEW_RATING_STYLE
   */
  val reviewRatingStyle
    get() = values.getAsInteger(Programs.COLUMN_REVIEW_RATING_STYLE)

  /**
   * @return The review rating for the program.
   * @see Programs.COLUMN_REVIEW_RATING
   */
  val reviewRating
    get() = values.getAsString(Programs.COLUMN_REVIEW_RATING)?.takeIf { it.isNotBlank() }

  override fun hashCode() = values.hashCode()

  override fun equals(other: Any?): Boolean {
    return if (other !is BaseProgram) {
      false
    } else values == other.values
  }

  override fun toString() = "BaseProgram{$values}"

  /**
   * @return The fields of the BaseProgram in [ContentValues] format to be easily inserted
   * into the TV Input Framework database.
   */
  open fun toContentValues() = ContentValues(values)

  companion object {

    private const val IS_SEARCHABLE = 1

    /**
     * Sets the fields in the cursor to the given builder instance.
     *
     * @param cursor A row from the TV Input Framework database.
     */
    internal fun getContentValues(cursor: Cursor): ContentValues {
      val values = ContentValues()

      var index: Int
      if (cursor.getColumnIndex(BaseTvColumns._ID)
          .also { index = it } >= 0 && !cursor.isNull(index)
      ) {
        values.put(BaseTvColumns._ID, cursor.getLong(index))
      }
      if (cursor.getColumnIndex(BaseTvColumns.COLUMN_PACKAGE_NAME).also {
          index = it
        } >= 0
        && !cursor.isNull(index)) {
        values.put(BaseTvColumns.COLUMN_PACKAGE_NAME, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_TITLE).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_TITLE, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_EPISODE_TITLE).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_EPISODE_TITLE, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_SEASON_DISPLAY_NUMBER).also {
          index = it
        } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_SEASON_DISPLAY_NUMBER, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_EPISODE_DISPLAY_NUMBER).also {
          index = it
        } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_EPISODE_DISPLAY_NUMBER, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_SHORT_DESCRIPTION).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_SHORT_DESCRIPTION, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_LONG_DESCRIPTION).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_LONG_DESCRIPTION, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_POSTER_ART_URI).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_POSTER_ART_URI, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_THUMBNAIL_URI).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_THUMBNAIL_URI, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_AUDIO_LANGUAGE).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_AUDIO_LANGUAGE, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_CANONICAL_GENRE).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(
          Programs.COLUMN_CANONICAL_GENRE,
          Programs.Genres.encode(cursor.getString(index))
        )
      }
      if (cursor.getColumnIndex(Programs.COLUMN_CONTENT_RATING).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_CONTENT_RATING, cursor.getString(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_VIDEO_WIDTH).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_VIDEO_WIDTH, cursor.getLong(index).toInt())
      }
      if (cursor.getColumnIndex(Programs.COLUMN_VIDEO_HEIGHT).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_VIDEO_HEIGHT, cursor.getLong(index).toInt())
      }
      if (cursor.getColumnIndex(Programs.COLUMN_INTERNAL_PROVIDER_DATA)
          .also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(Programs.COLUMN_INTERNAL_PROVIDER_DATA, cursor.getBlob(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_SEARCHABLE).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_SEARCHABLE, cursor.getInt(index) == IS_SEARCHABLE)
      }
      if (cursor.getColumnIndex(Programs.COLUMN_INTERNAL_PROVIDER_FLAG1).also {
          index = it
        } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_INTERNAL_PROVIDER_FLAG1, cursor.getLong(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_INTERNAL_PROVIDER_FLAG2).also {
          index = it
        } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_INTERNAL_PROVIDER_FLAG2, cursor.getLong(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_INTERNAL_PROVIDER_FLAG3).also {
          index = it
        } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_INTERNAL_PROVIDER_FLAG3, cursor.getLong(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_INTERNAL_PROVIDER_FLAG4).also {
          index = it
        } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_INTERNAL_PROVIDER_FLAG4, cursor.getLong(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_SEASON_TITLE).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_SEASON_TITLE, cursor.getString(index))
      }
      if (cursor.getColumnIndex(
          Programs.COLUMN_REVIEW_RATING_STYLE
        ).also { index = it } >= 0
        && !cursor.isNull(index)
      ) {
        values.put(Programs.COLUMN_REVIEW_RATING_STYLE, cursor.getInt(index))
      }
      if (cursor.getColumnIndex(Programs.COLUMN_REVIEW_RATING).also { index = it } >= 0
        && !cursor.isNull(index)) {
        values.put(Programs.COLUMN_REVIEW_RATING, cursor.getString(index))
      }

      return values
    }

    val PROJECTION = arrayOf(
      BaseTvColumns._ID,
      BaseTvColumns.COLUMN_PACKAGE_NAME,
      Programs.COLUMN_TITLE,
      Programs.COLUMN_EPISODE_TITLE,
      Programs.COLUMN_SEASON_DISPLAY_NUMBER,
      Programs.COLUMN_EPISODE_DISPLAY_NUMBER,
      Programs.COLUMN_SHORT_DESCRIPTION,
      Programs.COLUMN_LONG_DESCRIPTION,
      Programs.COLUMN_POSTER_ART_URI,
      Programs.COLUMN_THUMBNAIL_URI,
      Programs.COLUMN_AUDIO_LANGUAGE,
      Programs.COLUMN_CANONICAL_GENRE,
      Programs.COLUMN_CONTENT_RATING,
      Programs.COLUMN_VIDEO_WIDTH,
      Programs.COLUMN_VIDEO_HEIGHT,
      Programs.COLUMN_INTERNAL_PROVIDER_DATA,
      Programs.COLUMN_SEARCHABLE,
      Programs.COLUMN_INTERNAL_PROVIDER_FLAG1,
      Programs.COLUMN_INTERNAL_PROVIDER_FLAG2,
      Programs.COLUMN_INTERNAL_PROVIDER_FLAG3,
      Programs.COLUMN_INTERNAL_PROVIDER_FLAG4,
      Programs.COLUMN_SEASON_TITLE,
      Programs.COLUMN_REVIEW_RATING,
      Programs.COLUMN_REVIEW_RATING_STYLE
    )

    /**
     * Parses a string of comma-separated ratings into an array of [TvContentRating].
     *
     * Invalid strings are droppped. Duplicates are not removed. The order is preserved.
     *
     * @param commaSeparatedRatings String containing various ratings, separated by commas.
     * @return An array of TvContentRatings.
     */
    fun stringToContentRatings(commaSeparatedRatings: String): List<TvContentRating> {
      if (commaSeparatedRatings.isBlank()) {
        return emptyList()
      }
      return commaSeparatedRatings.split("\\s*,\\s*")
        .dropLastWhile { it.isEmpty() }
        .mapNotNull {
          try {
            TvContentRating.unflattenFromString(it)
          } catch (e: IllegalArgumentException) {
            Log.w("TV", "Can't parse the content rating: '$it', skipping", e)
            null
          }
        }
    }

    /**
     * Parses a string of comma-separated audio languages into an array of audio language strings.
     *
     * @param commaSeparatedString String containing audio languages, separated by commas.
     * @return An array of audio language.
     */
    fun stringToAudioLanguages(commaSeparatedString: String) =
      commaSeparatedString.split("\\s*,\\s*")
  }
}
