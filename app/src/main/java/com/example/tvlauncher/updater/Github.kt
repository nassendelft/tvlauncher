package com.example.tvlauncher.updater

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

@Serializable
internal data class GithubRelease(
  val assets: List<GithubAsset>,
  @SerialName("tag_name")
  val tagName: String,
  @SerialName("published_at")
  @Serializable(with = GithubDateSerializer::class)
  val publishedAt: Date,
)

@Serializable
internal data class GithubAsset(
  @SerialName("browser_download_url")
  val downloadUrl: String,
  @SerialName("created_at")
  val size: Long,
  val name: String
)

internal object GithubDateSerializer : KSerializer<Date> {
  private val formatter = SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ", Locale.US)
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: Date) =
    encoder.encodeString(formatter.format(value))

  override fun deserialize(decoder: Decoder): Date =
    formatter.parse(decoder.decodeString())!!
}
