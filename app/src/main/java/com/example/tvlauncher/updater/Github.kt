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
data class GithubRelease(
  val assets: List<GithubAsset>,
  @SerialName("tag_name")
  val tagName: String,
  @SerialName("published_at")
  @Serializable(with = GithubDateSerializer::class)
  val publishedAt: Date,
)

@Serializable
data class GithubAsset(
  @SerialName("browser_download_url")
  val downloadUrl: String,
  val size: Long,
  val name: String
)

object GithubDateSerializer : KSerializer<Date> {
  private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: Date) =
    encoder.encodeString(formatter.format(value))

  override fun deserialize(decoder: Decoder): Date =
    formatter.parse(decoder.decodeString())!!
}
