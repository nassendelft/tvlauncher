package nl.ncaj.tvlauncher.tv

import android.content.ContentValues
import android.media.tv.TvContract.PreviewPrograms.*
import android.net.Uri
import androidx.core.net.toUri

data class PreviewProgram(

  val program: Program,

  /**
   * @return The internal provider ID for the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_INTERNAL_PROVIDER_ID
   */
  val internalProviderId: String?,

  /**
   * @return The preview video URI for the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_PREVIEW_VIDEO_URI
   */
  val previewVideoUri: Uri?,

  /**
   * @return The last playback position of the program in millis.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_LAST_PLAYBACK_POSITION_MILLIS
   */
  val lastPlaybackPositionMillis: Int?,

  /**
   * @return The duration of the program in millis.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_DURATION_MILLIS
   */
  val durationMillis: Int?,

  /**
   * @return The intent URI which is launched when the program is selected.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_INTENT_URI
   */
  val intentUri: Uri?,

  /**
   * @return Whether the program is transient or not.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_TRANSIENT
   */
  val isTransient: Boolean,

  /**
   * @return The type of the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_TYPE
   */
  val type: Int?,

  /**
   * @return The TV series item type for the program.
   */
  val tvSeriesItemType: Int?,

  /**
   * @return The poster art aspect ratio for the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_POSTER_ART_ASPECT_RATIO
   */
  val posterArtAspectRatio: Int?,

  /**
   * @return The thumbnail aspect ratio for the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_THUMBNAIL_ASPECT_RATIO
   */
  val thumbnailAspectRatio: Int?,

  /**
   * @return The logo URI for the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_LOGO_URI
   */
  val logoUri: Uri?,

  /**
   * @return The availability of the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_AVAILABILITY
   */
  val availability: Int?,

  /**
   * @return The starting price of the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_STARTING_PRICE
   */
  val startingPrice: String?,

  /**
   * @return The offer price of the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_OFFER_PRICE
   */
  val offerPrice: String?,

  /**
   * @return The release date of the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_RELEASE_DATE
   */
  val releaseDate: String?,

  /**
   * @return The item count for the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_ITEM_COUNT
   */
  val itemCount: Int?,

  /**
   * @return Whether the program is live or not.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_LIVE
   */
  val isLive: Boolean,

  /**
   * @return The interaction type for the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_INTERACTION_TYPE
   */
  val interactionType: Int?,

  /**
   * @return The interaction count for the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_INTERACTION_COUNT
   */
  val interactionCount: Long?,

  /**
   * @return The author for the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_AUTHOR
   */
  val author: String?,

  /**
   * @return Whether the program is browsable or not.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_BROWSABLE
   */
  val isBrowsable: Boolean,

  /**
   * @return The content ID for the program.
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_CONTENT_ID
   */
  val contentId: String?,

  /**
   * @return The logo content description for the program.
   * @see PreviewProgramColumns.COLUMN_LOGO_CONTENT_DESCRIPTION
   * @see PreviewProgramColumns.COLUMN_LOGO_URI
   */
  val logoContentDescription: String?,

  /**
   * @return The genre for the program.
   * @see PreviewProgramColumns.COLUMN_GENRE
   */
  val genre: String?,

  /**
   * @return The start time for the program.
   * @see PreviewProgramColumns.COLUMN_START_TIME_UTC_MILLIS
   */
  val startTimeUtcMillis: Long?,

  /**
   * @return The end time for the program.
   * @see PreviewProgramColumns.COLUMN_END_TIME_UTC_MILLIS
   */
  val endTimeUtcMillis: Long?,

  /**
   * @return The preview audio URI for the program.
   * @see PreviewProgramColumns.COLUMN_PREVIEW_AUDIO_URI
   */
  val previewAudioUri: Uri?
) {
  companion object {

    private const val IS_TRANSIENT = 1
    private const val IS_LIVE = 1
    private const val IS_BROWSABLE = 1

    fun fromContentValues(values: ContentValues) = PreviewProgram(
      Program.fromContentValues(values),
      values.getAsString(COLUMN_INTERNAL_PROVIDER_ID),
      values.getAsString(COLUMN_PREVIEW_VIDEO_URI)?.let { Uri.parse(it) },
      values.getAsInteger(COLUMN_LAST_PLAYBACK_POSITION_MILLIS),
      values.getAsInteger(COLUMN_DURATION_MILLIS),
      values.getAsString(COLUMN_INTENT_URI).let { Uri.parse(it) },
      values.getAsInteger(COLUMN_TRANSIENT) == IS_TRANSIENT,
      values.getAsInteger(COLUMN_TYPE),
      values.getAsInteger(PreviewProgramColumns.COLUMN_TV_SERIES_ITEM_TYPE),
      values.getAsInteger(COLUMN_POSTER_ART_ASPECT_RATIO),
      values.getAsInteger(COLUMN_THUMBNAIL_ASPECT_RATIO),
      values.getAsString(COLUMN_LOGO_URI)?.let { Uri.parse(it) },
      values.getAsInteger(COLUMN_AVAILABILITY),
      values.getAsString(COLUMN_STARTING_PRICE)?.takeIf { it.isNotBlank() },
      values.getAsString(COLUMN_OFFER_PRICE)?.takeIf { it.isNotBlank() },
      values.getAsString(COLUMN_RELEASE_DATE)?.takeIf { it.isNotBlank() },
      values.getAsInteger(COLUMN_ITEM_COUNT),
      values.getAsInteger(COLUMN_LIVE) == IS_LIVE,
      values.getAsInteger(COLUMN_INTERACTION_TYPE),
      values.getAsLong(COLUMN_INTERACTION_COUNT),
      values.getAsString(COLUMN_AUTHOR)?.takeIf { it.isNotBlank() },
      values.getAsInteger(COLUMN_BROWSABLE) == IS_BROWSABLE,
      values.getAsString(COLUMN_CONTENT_ID)?.takeIf { it.isNotBlank() },
      values.getAsString(PreviewProgramColumns.COLUMN_LOGO_CONTENT_DESCRIPTION)
        ?.takeIf { it.isNotBlank() },
      values.getAsString(PreviewProgramColumns.COLUMN_GENRE)?.takeIf { it.isNotBlank() },
      values.getAsLong(PreviewProgramColumns.COLUMN_START_TIME_UTC_MILLIS),
      values.getAsLong(PreviewProgramColumns.COLUMN_END_TIME_UTC_MILLIS),
      values.getAsString(PreviewProgramColumns.COLUMN_PREVIEW_AUDIO_URI)
        ?.takeIf { it.isNotBlank() }?.toUri()
    )

    val PROJECTION = Program.PROJECTION + arrayOf(
      COLUMN_INTERNAL_PROVIDER_ID,
      COLUMN_PREVIEW_VIDEO_URI,
      COLUMN_LAST_PLAYBACK_POSITION_MILLIS,
      COLUMN_DURATION_MILLIS,
      COLUMN_INTENT_URI,
      COLUMN_TRANSIENT,
      COLUMN_TYPE,
      COLUMN_POSTER_ART_ASPECT_RATIO,
      COLUMN_THUMBNAIL_ASPECT_RATIO,
      COLUMN_LOGO_URI,
      COLUMN_AVAILABILITY,
      COLUMN_STARTING_PRICE,
      COLUMN_OFFER_PRICE,
      COLUMN_RELEASE_DATE,
      COLUMN_INTERACTION_TYPE,
      COLUMN_INTERACTION_COUNT,
      COLUMN_AUTHOR,
      COLUMN_BROWSABLE,
      COLUMN_CONTENT_ID,
      PreviewProgramColumns.COLUMN_LOGO_CONTENT_DESCRIPTION,
      PreviewProgramColumns.COLUMN_GENRE,
      PreviewProgramColumns.COLUMN_START_TIME_UTC_MILLIS,
      PreviewProgramColumns.COLUMN_END_TIME_UTC_MILLIS,
      PreviewProgramColumns.COLUMN_PREVIEW_AUDIO_URI,
      PreviewProgramColumns.COLUMN_TV_SERIES_ITEM_TYPE
    )
  }
}
