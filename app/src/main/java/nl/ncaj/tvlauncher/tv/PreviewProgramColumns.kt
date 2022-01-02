package nl.ncaj.tvlauncher.tv

/**
 * These column names are added *after* API 28
 */
object PreviewProgramColumns {

  /**
   * The TV series item type. Can be one of the followings:
   * [.TV_SERIES_ITEM_TYPE_EPISODE],
   * [.TV_SERIES_ITEM_TYPE_CHAPTER]
   *
   * This is used to indicate whether a TV season has the sub-division as episode
   * or chapter. If this is not set, it is default to [.TV_SERIES_ITEM_TYPE_EPISODE]
   *
   * Type: INTEGER
   */
  const val COLUMN_TV_SERIES_ITEM_TYPE = "tv_series_item_type"

  /**
   * The URI for the logo of this TV program.
   *
   * This is a small badge shown on top of the poster art or thumbnail representing the
   * source of the content.
   *
   * The data in the column must be a URL, or a URI in one of the following formats:
   *
   *  * content ([android.content.ContentResolver.SCHEME_CONTENT])
   *  * android.resource ([android.content.ContentResolver.SCHEME_ANDROID_RESOURCE])
   *  * file ([android.content.ContentResolver.SCHEME_FILE])
   *
   * Can be empty.
   *
   * Type: TEXT
   */
  const val COLUMN_LOGO_URI = "logo_uri"

  /**
   * The content description of the logo of this TV program.
   *
   * A description of the logo shown on the program used in accessibility mode.
   *
   * Can be empty.
   *
   * Type: TEXT
   *
   * @see COLUMN_LOGO_URI
   */
  const val COLUMN_LOGO_CONTENT_DESCRIPTION = "logo_content_description"

  /**
   * A genre(s) that are related to this TV program.
   *
   * A short freeform description of the genre(s) of the program. Usually a comma separated
   * list of a few genres. For example: Drama, Sci-Fi.
   *
   * Can be empty.
   *
   * Type: TEXT
   */
  const val COLUMN_GENRE = "genre"

  /**
   * The start time of this TV program, in milliseconds since the epoch.
   *
   * Should be empty if this program is not live.
   *
   * Type: INTEGER (long)
   *
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_LIVE
   */
  const val COLUMN_START_TIME_UTC_MILLIS = "start_time_utc_millis"

  /**
   * The end time of this TV program, in milliseconds since the epoch.
   *
   * Should be empty if this program is not live.
   *
   * Type: INTEGER (long)
   *
   * @see android.media.tv.TvContract.PreviewPrograms.COLUMN_LIVE
   */
  const val COLUMN_END_TIME_UTC_MILLIS = "end_time_utc_millis"

  /**
   * The URI for the preview audio.
   *
   * The data in the column must be a URL, or a URI in one of the following formats:
   *
   *  * content ([android.content.ContentResolver.SCHEME_CONTENT])
   *  * android.resource ([android.content.ContentResolver.SCHEME_ANDROID_RESOURCE])
   *  * file ([android.content.ContentResolver.SCHEME_FILE])
   *
   * Can be empty.
   *
   * Type: TEXT
   */
  const val COLUMN_PREVIEW_AUDIO_URI = "preview_audio_uri"
}
