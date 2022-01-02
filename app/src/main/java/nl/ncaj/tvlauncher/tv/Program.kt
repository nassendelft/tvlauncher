package nl.ncaj.tvlauncher.tv

import android.content.ContentValues
import android.database.Cursor
import android.media.tv.TvContentRating
import android.media.tv.TvContract.BaseTvColumns
import android.media.tv.TvContract.Programs
import android.net.Uri
import android.util.Log
import androidx.core.database.getBlobOrNull
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.core.net.toUri

data class Program(
  /**
   * The ID for the program.
   * @see BaseTvColumns._ID
   */
  val id: Long,

  /**
   * The package name for the program.
   * @see BaseTvColumns.COLUMN_PACKAGE_NAME
   */
  val packageName: String,

  /**
   * The title for the program.
   * @see Programs.COLUMN_TITLE
   */
  val title: String?,

  /**
   * @return The episode title for the program.
   * @see Programs.COLUMN_EPISODE_TITLE
   */
  val episodeTitle: String?,

  /**
   * @return The season display number for the program.
   * @see Programs.COLUMN_SEASON_DISPLAY_NUMBER
   */
  val seasonNumber: String?,

  /**
   * @return The episode display number for the program.
   * @see Programs.COLUMN_EPISODE_DISPLAY_NUMBER
   */
  val episodeNumber: String?,

  /**
   * @return The short description for the program.
   * @see Programs.COLUMN_SHORT_DESCRIPTION
   */
  val description: String?,

  /**
   * @return The long description for the program.
   * @see Programs.COLUMN_LONG_DESCRIPTION
   */
  val longDescription: String?,

  /**
   * @return The video width for the program.
   * @see Programs.COLUMN_VIDEO_WIDTH
   */
  val videoWidth: Int?,

  /**
   * @return The video height for the program.
   * @see Programs.COLUMN_VIDEO_HEIGHT
   */
  val videoHeight: Int?,

  /**
   * @return The canonical genre for the program.
   * @see Programs.COLUMN_CANONICAL_GENRE
   */
  val canonicalGenres: List<String>,

  /**
   * @return The content rating for the program.
   * @see Programs.COLUMN_CONTENT_RATING
   */
  val contentRatings: List<TvContentRating>,

  /**
   * @return The poster art URI for the program.
   * @see Programs.COLUMN_POSTER_ART_URI
   */
  val posterArtUri: Uri?,

  /**
   * @return The thumbnail URI for the program.
   * @see Programs.COLUMN_THUMBNAIL_URI
   */
  val thumbnailUri: Uri?,

  /**
   * @return The internal provider data for the program.
   * @see Programs.COLUMN_INTERNAL_PROVIDER_DATA
   */
  val internalProviderDataByteArray: ByteArray,

  /**
   * @return The audio languages for the program.
   * @see Programs.COLUMN_AUDIO_LANGUAGE
   */
  val audioLanguages: List<String>,

  /**
   * @return Whether the program is searchable or not.
   * @see Programs.COLUMN_SEARCHABLE
   */
  val searchable: Boolean,

  /**
   * @return The first internal provider flag for the program.
   * @see Programs.COLUMN_INTERNAL_PROVIDER_FLAG1
   */
  val internalProviderFlag1: Long?,

  /**
   * @return The first internal provider flag for the program.
   * @see Programs.COLUMN_INTERNAL_PROVIDER_FLAG2
   */
  val internalProviderFlag2: Long?,

  /**
   * @return The first internal provider flag for the program.
   * @see Programs.COLUMN_INTERNAL_PROVIDER_FLAG3
   */
  val internalProviderFlag3: Long?,

  /**
   * @return The first internal provider flag for the program.
   * @see Programs.COLUMN_INTERNAL_PROVIDER_FLAG4
   */
  val internalProviderFlag4: Long?,

  /**
   * @return The season title for the program.
   * @see Programs.COLUMN_SEASON_TITLE
   */
  val getSeasonTitle: String?,

  /**
   * @return The review rating style for the program.
   * @see android.media.tv.TvContract.Programs.COLUMN_REVIEW_RATING_STYLE
   */
  val reviewRatingStyle: Int?,

  /**
   * @return The review rating for the program.
   * @see Programs.COLUMN_REVIEW_RATING
   */
  val reviewRating: String?
) {

  companion object {

    fun fromContentValues(values: ContentValues) = Program(
      values.getAsLong(BaseTvColumns._ID),
      values.getAsString(BaseTvColumns.COLUMN_PACKAGE_NAME),
      values.getAsString(Programs.COLUMN_TITLE)?.takeIf { it.isNotBlank() },
      values.getAsString(Programs.COLUMN_EPISODE_TITLE)?.takeIf { it.isNotBlank() },
      values.getAsString(Programs.COLUMN_SEASON_DISPLAY_NUMBER)?.takeIf { it.isNotBlank() },
      values.getAsString(Programs.COLUMN_EPISODE_DISPLAY_NUMBER)?.takeIf { it.isNotBlank() },
      values.getAsString(Programs.COLUMN_SHORT_DESCRIPTION)?.takeIf { it.isNotBlank() },
      values.getAsString(Programs.COLUMN_LONG_DESCRIPTION)?.takeIf { it.isNotBlank() },
      values.getAsInteger(Programs.COLUMN_VIDEO_WIDTH),
      values.getAsInteger(Programs.COLUMN_VIDEO_HEIGHT),
      values.getAsString(Programs.COLUMN_CANONICAL_GENRE)
        ?.let { Programs.Genres.decode(it) }?.toList() ?: emptyList(),
      values.getAsString(Programs.COLUMN_CONTENT_RATING)
        ?.let { stringToContentRatings(it) } ?: emptyList(),
      values.getAsString(Programs.COLUMN_POSTER_ART_URI)?.takeIf { it.isNotBlank() }?.toUri(),
      values.getAsString(Programs.COLUMN_POSTER_ART_URI)?.takeIf { it.isNotBlank() }?.toUri(),
      values.getAsByteArray(Programs.COLUMN_INTERNAL_PROVIDER_DATA)
        ?: ByteArray(0),
      values.getAsString(Programs.COLUMN_AUDIO_LANGUAGE)
        ?.let { stringToAudioLanguages(it) } ?: emptyList(),
      values.getAsInteger(Programs.COLUMN_SEARCHABLE)?.let { it == IS_SEARCHABLE } ?: true,
      values.getAsLong(Programs.COLUMN_INTERNAL_PROVIDER_FLAG1),
      values.getAsLong(Programs.COLUMN_INTERNAL_PROVIDER_FLAG2),
      values.getAsLong(Programs.COLUMN_INTERNAL_PROVIDER_FLAG3),
      values.getAsLong(Programs.COLUMN_INTERNAL_PROVIDER_FLAG4),
      values.getAsString(Programs.COLUMN_SEASON_TITLE)?.takeIf { it.isNotBlank() },
      values.getAsInteger(Programs.COLUMN_REVIEW_RATING_STYLE),
      values.getAsString(Programs.COLUMN_REVIEW_RATING)?.takeIf { it.isNotBlank() }
    )

    private const val IS_SEARCHABLE = 1

    /**
     * Sets the fields in the cursor to the given builder instance.
     *
     * @param cursor A row from the TV Input Framework database.
     */
    fun getContentValues(cursor: Cursor): ContentValues {
      val values = ContentValues()

      (0 until cursor.columnCount).forEach {
        when (cursor.getType(it)) {
          Cursor.FIELD_TYPE_BLOB -> values.put(cursor.getColumnName(it), cursor.getBlobOrNull(it))
          Cursor.FIELD_TYPE_FLOAT -> values.put(cursor.getColumnName(it), cursor.getFloatOrNull(it))
          Cursor.FIELD_TYPE_INTEGER -> values.put(cursor.getColumnName(it), cursor.getIntOrNull(it))
          Cursor.FIELD_TYPE_STRING -> values.put(
            cursor.getColumnName(it),
            cursor.getStringOrNull(it)
          )
          Cursor.FIELD_TYPE_NULL -> values.putNull(cursor.getColumnName(it))
        }
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
    private fun stringToContentRatings(commaSeparatedRatings: String): List<TvContentRating> {
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
    private fun stringToAudioLanguages(commaSeparatedString: String) =
      commaSeparatedString.split("\\s*,\\s*")
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Program

    if (id != other.id) return false
    if (packageName != other.packageName) return false
    if (title != other.title) return false
    if (episodeTitle != other.episodeTitle) return false
    if (seasonNumber != other.seasonNumber) return false
    if (episodeNumber != other.episodeNumber) return false
    if (description != other.description) return false
    if (longDescription != other.longDescription) return false
    if (videoWidth != other.videoWidth) return false
    if (videoHeight != other.videoHeight) return false
    if (canonicalGenres != other.canonicalGenres) return false
    if (contentRatings != other.contentRatings) return false
    if (posterArtUri != other.posterArtUri) return false
    if (thumbnailUri != other.thumbnailUri) return false
    if (!internalProviderDataByteArray.contentEquals(other.internalProviderDataByteArray)) return false
    if (audioLanguages != other.audioLanguages) return false
    if (searchable != other.searchable) return false
    if (internalProviderFlag1 != other.internalProviderFlag1) return false
    if (internalProviderFlag2 != other.internalProviderFlag2) return false
    if (internalProviderFlag3 != other.internalProviderFlag3) return false
    if (internalProviderFlag4 != other.internalProviderFlag4) return false
    if (getSeasonTitle != other.getSeasonTitle) return false
    if (reviewRatingStyle != other.reviewRatingStyle) return false
    if (reviewRating != other.reviewRating) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + packageName.hashCode()
    result = 31 * result + (title?.hashCode() ?: 0)
    result = 31 * result + (episodeTitle?.hashCode() ?: 0)
    result = 31 * result + (seasonNumber?.hashCode() ?: 0)
    result = 31 * result + (episodeNumber?.hashCode() ?: 0)
    result = 31 * result + (description?.hashCode() ?: 0)
    result = 31 * result + (longDescription?.hashCode() ?: 0)
    result = 31 * result + (videoWidth ?: 0)
    result = 31 * result + (videoHeight ?: 0)
    result = 31 * result + canonicalGenres.hashCode()
    result = 31 * result + contentRatings.hashCode()
    result = 31 * result + (posterArtUri?.hashCode() ?: 0)
    result = 31 * result + (thumbnailUri?.hashCode() ?: 0)
    result = 31 * result + internalProviderDataByteArray.contentHashCode()
    result = 31 * result + audioLanguages.hashCode()
    result = 31 * result + searchable.hashCode()
    result = 31 * result + internalProviderFlag1.hashCode()
    result = 31 * result + internalProviderFlag2.hashCode()
    result = 31 * result + internalProviderFlag3.hashCode()
    result = 31 * result + internalProviderFlag4.hashCode()
    result = 31 * result + (getSeasonTitle?.hashCode() ?: 0)
    result = 31 * result + (reviewRatingStyle ?: 0)
    result = 31 * result + (reviewRating?.hashCode() ?: 0)
    return result
  }
}
