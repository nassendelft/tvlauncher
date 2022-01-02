package nl.ncaj.tvlauncher.tv

object PreviewProgramColumns {
  /**
   * The program type for movie.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_MOVIE = 0

  /**
   * The program type for TV series.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_TV_SERIES = 1

  /**
   * The program type for TV season.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_TV_SEASON = 2

  /**
   * The program type for TV episode.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_TV_EPISODE = 3

  /**
   * The program type for clip.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_CLIP = 4

  /**
   * The program type for event.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_EVENT = 5

  /**
   * The program type for channel.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_CHANNEL = 6

  /**
   * The program type for track.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_TRACK = 7

  /**
   * The program type for album.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_ALBUM = 8

  /**
   * The program type for artist.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_ARTIST = 9

  /**
   * The program type for playlist.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_PLAYLIST = 10

  /**
   * The program type for station.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_STATION = 11

  /**
   * The program type for game.
   *
   * @see COLUMN_TYPE
   */
  const val TYPE_GAME = 12

  /**
   * The TV series item type for episode.
   *
   * @see COLUMN_TV_SERIES_ITEM_TYPE
   */
  const val TV_SERIES_ITEM_TYPE_EPISODE = 0

  /**
   * The TV series item type for chapter.
   *
   * @see COLUMN_TV_SERIES_ITEM_TYPE
   */
  const val TV_SERIES_ITEM_TYPE_CHAPTER = 1

  /**
   * The aspect ratio for 16:9.
   *
   * @see COLUMN_POSTER_ART_ASPECT_RATIO
   *
   * @see COLUMN_THUMBNAIL_ASPECT_RATIO
   */
  const val ASPECT_RATIO_16_9 = 0

  /**
   * The aspect ratio for 3:2.
   *
   * @see COLUMN_POSTER_ART_ASPECT_RATIO
   *
   * @see COLUMN_THUMBNAIL_ASPECT_RATIO
   */
  const val ASPECT_RATIO_3_2 = 1

  /**
   * The aspect ratio for 4:3.
   *
   * @see COLUMN_POSTER_ART_ASPECT_RATIO
   *
   * @see COLUMN_THUMBNAIL_ASPECT_RATIO
   */
  const val ASPECT_RATIO_4_3 = 2

  /**
   * The aspect ratio for 1:1.
   *
   * @see COLUMN_POSTER_ART_ASPECT_RATIO
   *
   * @see COLUMN_THUMBNAIL_ASPECT_RATIO
   */
  const val ASPECT_RATIO_1_1 = 3

  /**
   * The aspect ratio for 2:3.
   *
   * @see COLUMN_POSTER_ART_ASPECT_RATIO
   *
   * @see COLUMN_THUMBNAIL_ASPECT_RATIO
   */
  const val ASPECT_RATIO_2_3 = 4

  /**
   * The aspect ratio for movie poster which is 1:1.441.
   *
   * @see COLUMN_POSTER_ART_ASPECT_RATIO
   *
   * @see COLUMN_THUMBNAIL_ASPECT_RATIO
   */
  const val ASPECT_RATIO_MOVIE_POSTER = 5

  /**
   * The availability for "available to this user".
   *
   * @see COLUMN_AVAILABILITY
   */
  const val AVAILABILITY_AVAILABLE = 0

  /**
   * The availability for "free with subscription".
   *
   * @see COLUMN_AVAILABILITY
   */
  const val AVAILABILITY_FREE_WITH_SUBSCRIPTION = 1

  /**
   * The availability for "paid content", either to-own or rental
   * (user has not purchased/rented).
   *
   * @see COLUMN_AVAILABILITY
   */
  const val AVAILABILITY_PAID_CONTENT = 2

  /**
   * The availability for content already purchased by the user.
   *
   * @see COLUMN_AVAILABILITY
   */
  const val AVAILABILITY_PURCHASED = 3

  /**
   * The availability for free content.
   *
   * @see COLUMN_AVAILABILITY
   */
  const val AVAILABILITY_FREE = 4

  /**
   * The interaction type for "views".
   *
   * @see COLUMN_INTERACTION_TYPE
   */
  const val INTERACTION_TYPE_VIEWS = 0

  /**
   * The interaction type for "listens".
   *
   * @see COLUMN_INTERACTION_TYPE
   */
  const val INTERACTION_TYPE_LISTENS = 1

  /**
   * The interaction type for "followers".
   *
   * @see COLUMN_INTERACTION_TYPE
   */
  const val INTERACTION_TYPE_FOLLOWERS = 2

  /**
   * The interaction type for "fans".
   *
   * @see COLUMN_INTERACTION_TYPE
   */
  const val INTERACTION_TYPE_FANS = 3

  /**
   * The interaction type for "likes".
   *
   * @see COLUMN_INTERACTION_TYPE
   */
  const val INTERACTION_TYPE_LIKES = 4

  /**
   * The interaction type for "thumbs".
   *
   * @see COLUMN_INTERACTION_TYPE
   */
  const val INTERACTION_TYPE_THUMBS = 5

  /**
   * The interaction type for "viewers".
   *
   * @see COLUMN_INTERACTION_TYPE
   */
  const val INTERACTION_TYPE_VIEWERS = 6

  /**
   * The type of this program content.
   *
   *
   * The value should match one of the followings:
   * [TYPE_MOVIE],
   * [TYPE_TV_SERIES],
   * [TYPE_TV_SEASON],
   * [TYPE_TV_EPISODE],
   * [TYPE_CLIP],
   * [TYPE_EVENT],
   * [TYPE_CHANNEL],
   * [TYPE_TRACK],
   * [TYPE_ALBUM],
   * [TYPE_ARTIST],
   * [TYPE_PLAYLIST],
   * [TYPE_STATION], and
   * [TYPE_GAME].
   *
   *
   * This is a required field if the program is from a [Channels.TYPE_PREVIEW]
   * channel.
   *
   *
   * Type: INTEGER
   */
  const val COLUMN_TYPE = "type"

  /**
   * The TV series item type. Can be one of the followings:
   * [.TV_SERIES_ITEM_TYPE_EPISODE],
   * [.TV_SERIES_ITEM_TYPE_CHAPTER]
   *
   *
   * This is used to indicate whether a TV season has the sub-division as episode
   * or chapter. If this is not set, it is default to [.TV_SERIES_ITEM_TYPE_EPISODE]
   *
   *
   * Type: INTEGER
   */
  const val COLUMN_TV_SERIES_ITEM_TYPE = "tv_series_item_type"

  /**
   * The aspect ratio of the poster art for this TV program.
   *
   *
   * The value should match one of the followings:
   * [.ASPECT_RATIO_16_9],
   * [.ASPECT_RATIO_3_2],
   * [.ASPECT_RATIO_4_3],
   * [.ASPECT_RATIO_1_1],
   * [.ASPECT_RATIO_2_3], and
   * [.ASPECT_RATIO_MOVIE_POSTER].
   *
   *
   * Type: INTEGER
   */
  const val COLUMN_POSTER_ART_ASPECT_RATIO = "poster_art_aspect_ratio"

  /**
   * The aspect ratio of the thumbnail for this TV program.
   *
   *
   * The value should match one of the followings:
   * [.ASPECT_RATIO_16_9],
   * [.ASPECT_RATIO_3_2],
   * [.ASPECT_RATIO_4_3],
   * [.ASPECT_RATIO_1_1],
   * [.ASPECT_RATIO_2_3], and
   * [.ASPECT_RATIO_MOVIE_POSTER].
   *
   *
   * Type: INTEGER
   */
  const val COLUMN_THUMBNAIL_ASPECT_RATIO = "poster_thumbnail_aspect_ratio"

  /**
   * The URI for the logo of this TV program.
   *
   *
   * This is a small badge shown on top of the poster art or thumbnail representing the
   * source of the content.
   *
   *
   * The data in the column must be a URL, or a URI in one of the following formats:
   *
   *
   *  * content ([android.content.ContentResolver.SCHEME_CONTENT])
   *  * android.resource ([android.content.ContentResolver.SCHEME_ANDROID_RESOURCE])
   *
   *  * file ([android.content.ContentResolver.SCHEME_FILE])
   *
   *
   *
   * Can be empty.
   *
   *
   * Type: TEXT
   */
  const val COLUMN_LOGO_URI = "logo_uri"

  /**
   * The availability of this TV program.
   *
   *
   * The value should match one of the followings:
   * [.AVAILABILITY_AVAILABLE],
   * [.AVAILABILITY_FREE_WITH_SUBSCRIPTION],
   * [.AVAILABILITY_PAID_CONTENT],
   * [.AVAILABILITY_PURCHASED] and
   * [.AVAILABILITY_FREE].
   *
   *
   * Type: INTEGER
   */
  const val COLUMN_AVAILABILITY = "availability"

  /**
   * The starting price of this TV program.
   *
   *
   * This indicates the lowest regular acquisition cost of the content. It is only used
   * if the availability of the program is [.AVAILABILITY_PAID_CONTENT] or
   * [.AVAILABILITY_FREE].
   *
   *
   * Type: TEXT
   *
   * @see COLUMN_OFFER_PRICE
   */
  const val COLUMN_STARTING_PRICE = "starting_price"

  /**
   * The offer price of this TV program.
   *
   *
   * This is the promotional cost of the content. It is only used if the availability of
   * the program is [.AVAILABILITY_PAID_CONTENT].
   *
   *
   * Type: TEXT
   *
   * @see COLUMN_STARTING_PRICE
   */
  const val COLUMN_OFFER_PRICE = "offer_price"

  /**
   * The release date of this TV program.
   *
   *
   * The value should be in one of the following formats:
   * "yyyy", "yyyy-MM-dd", and "yyyy-MM-ddTHH:mm:ssZ" (UTC in ISO 8601).
   *
   *
   * Type: TEXT
   */
  const val COLUMN_RELEASE_DATE = "release_date"

  /**
   * The count of the items included in this TV program.
   *
   *
   * This is only relevant if the program represents a collection of items such as series,
   * episodes, or music tracks.
   *
   *
   * Type: INTEGER
   */
  const val COLUMN_ITEM_COUNT = "item_count"

  /**
   * The flag indicating whether this TV program is live or not.
   *
   *
   * A value of 1 indicates that the content is airing and should be consumed now, a value
   * of 0 indicates that the content is off the air and does not need to be consumed at the
   * present time. If not specified, the value is set to 0 (not live) by default.
   *
   *
   * Type: INTEGER (boolean)
   */
  const val COLUMN_LIVE = "live"

  /**
   * The internal ID used by individual TV input services.
   *
   *
   * This is internal to the provider that inserted it, and should not be decoded by other
   * apps.
   *
   *
   * Can be empty.
   *
   *
   * Type: TEXT
   */
  const val COLUMN_INTERNAL_PROVIDER_ID = "internal_provider_id"

  /**
   * The URI for the preview video.
   *
   *
   * The data in the column must be a URL, or a URI in one of the following formats:
   *
   *
   *  * content ([android.content.ContentResolver.SCHEME_CONTENT])
   *  * android.resource ([android.content.ContentResolver.SCHEME_ANDROID_RESOURCE])
   *
   *  * file ([android.content.ContentResolver.SCHEME_FILE])
   *
   *
   *
   * Can be empty.
   *
   *
   * Type: TEXT
   */
  const val COLUMN_PREVIEW_VIDEO_URI = "preview_video_uri"

  /**
   * The last playback position (in milliseconds) of the original content of this preview
   * program.
   *
   *
   * Can be empty.
   *
   *
   * Type: INTEGER
   */
  const val COLUMN_LAST_PLAYBACK_POSITION_MILLIS = "last_playback_position_millis"

  /**
   * The duration (in milliseconds) of the original content of this preview program.
   *
   *
   * Can be empty.
   *
   *
   * Type: INTEGER
   */
  const val COLUMN_DURATION_MILLIS = "duration_millis"

  /**
   * The intent URI which is launched when the preview program is selected.
   *
   *
   * The URI is created using [Intent.toUri] with [Intent.URI_INTENT_SCHEME]
   * and converted back to the original intent with [Intent.parseUri]. The intent is
   * launched when the user selects the preview program item.
   *
   *
   * Can be empty.
   *
   *
   * Type: TEXT
   */
  const val COLUMN_INTENT_URI = "intent_uri"

  /**
   * The flag indicating whether this program is transient or not.
   *
   *
   * A value of 1 indicates that the channel will be automatically removed by the system on
   * reboot, and a value of 0 indicates that the channel is persistent across reboot. If not
   * specified, this value is set to 0 (not transient) by default.
   *
   *
   * Type: INTEGER (boolean)
   *
   * @see Channels.COLUMN_TRANSIENT
   */
  const val COLUMN_TRANSIENT = "transient"

  /**
   * The type of interaction for this TV program.
   *
   *
   *  The value should match one of the followings:
   * [.INTERACTION_TYPE_LISTENS],
   * [.INTERACTION_TYPE_FOLLOWERS],
   * [.INTERACTION_TYPE_FANS],
   * [.INTERACTION_TYPE_LIKES],
   * [.INTERACTION_TYPE_THUMBS],
   * [.INTERACTION_TYPE_VIEWS], and
   * [.INTERACTION_TYPE_VIEWERS].
   *
   *
   * Type: INTEGER
   *
   * @see COLUMN_INTERACTION_COUNT
   */
  const val COLUMN_INTERACTION_TYPE = "interaction_type"

  /**
   * The interaction count for this program.
   *
   *
   * This indicates the number of times interaction has happened.
   *
   *
   * Type: INTEGER (long)
   *
   * @see COLUMN_INTERACTION_TYPE
   */
  const val COLUMN_INTERACTION_COUNT = "interaction_count"

  /**
   * The author or artist of this content.
   *
   *
   * Type: TEXT
   */
  const val COLUMN_AUTHOR = "author"

  /**
   * The flag indicating whether this TV program is browsable or not.
   *
   *
   * This column can only be set by applications having proper system permission. For
   * other applications, this is a read-only column.
   *
   *
   * A value of 1 indicates that the program is browsable and can be shown to users in
   * the UI. A value of 0 indicates that the program should be hidden from users and the
   * application who changes this value to 0 should send
   * [.ACTION_WATCH_NEXT_PROGRAM_BROWSABLE_DISABLED] to the owner of the program
   * to notify this change.
   *
   *
   * This value is set to 1 (browsable) by default.
   *
   *
   * Type: INTEGER (boolean)
   */
  const val COLUMN_BROWSABLE = "browsable"

  /**
   * The content ID of this TV program.
   *
   *
   * A public ID of the content which allows the application to apply the same operation to
   * all the program copies in different channels.
   *
   *
   * Can be empty.
   *
   *
   * Type: TEXT
   */
  const val COLUMN_CONTENT_ID = "content_id"

  /**
   * The content description of the logo of this TV program.
   *
   *
   * A description of the logo shown on the program used in accessibility mode.
   *
   *
   * Can be empty.
   *
   *
   * Type: TEXT
   *
   * @see COLUMN_LOGO_URI
   */
  const val COLUMN_LOGO_CONTENT_DESCRIPTION = "logo_content_description"

  /**
   * A genre(s) that are related to this TV program.
   *
   *
   * A short freeform description of the genre(s) of the program. Usually a comma seperated
   * list of a few genres. For example: Drama, Sci-Fi.
   *
   *
   * Can be empty.
   *
   *
   * Type: TEXT
   */
  const val COLUMN_GENRE = "genre"

  /**
   * The start time of this TV program, in milliseconds since the epoch.
   *
   *
   * Should be empty if this program is not live.
   *
   *
   * Type: INTEGER (long)
   *
   * @see COLUMN_LIVE
   */
  const val COLUMN_START_TIME_UTC_MILLIS = "start_time_utc_millis"

  /**
   * The end time of this TV program, in milliseconds since the epoch.
   *
   *
   * Should be empty if this program is not live.
   *
   *
   * Type: INTEGER (long)
   *
   * @see COLUMN_LIVE
   */
  const val COLUMN_END_TIME_UTC_MILLIS = "end_time_utc_millis"

  /**
   * The URI for the preview audio.
   *
   *
   * The data in the column must be a URL, or a URI in one of the following formats:
   *
   *
   *  * content ([android.content.ContentResolver.SCHEME_CONTENT])
   *  * android.resource ([android.content.ContentResolver.SCHEME_ANDROID_RESOURCE])
   *
   *  * file ([android.content.ContentResolver.SCHEME_FILE])
   *
   *
   *
   * Can be empty.
   *
   *
   * Type: TEXT
   */
  const val COLUMN_PREVIEW_AUDIO_URI = "preview_audio_uri"
}
