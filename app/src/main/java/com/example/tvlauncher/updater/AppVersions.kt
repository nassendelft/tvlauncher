package com.example.tvlauncher.updater

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

internal class AppVersions(
  private val httpClient: OkHttpClient = OkHttpClient(),
  private val json: Json = Json { ignoreUnknownKeys = true }
) {

  /**
   * Retrieves the latest published version information.
   * This method makes a network call so it runs on the [IO] dispatcher.
   *
   * @throws okio.IOException if network call fails for whatever reason
   * @throws kotlin.IllegalStateException if no release could be found
   *
   * @return [Release] object with information about the latest release
   */
  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun getLatestRelease() = withContext(IO) {
    val request = Request.Builder()
      .url("https://api.github.com/repos/nassendelft/tvlauncher/releases")
      .header("Accept", "application/vnd.github.v3+json")
      .build()

    val response = httpClient.newCall(request).execute()
    val githubReleases = response.body?.let {
      json.decodeFromString<List<GithubRelease>>(it.string())
    } ?: emptyList()
    return@withContext githubReleases.firstOrNull()?.asRelease
      ?: error("Could not find any release")
  }

  private val GithubRelease.asRelease
    get() = this.assets.find { it.name.endsWith(".apk") }?.let { asset ->
      Release(
        version = this.tagName.substring(1), // removes the 'v' prefix in tag
        date = this.publishedAt,
        file = Release.File(
          size = asset.size,
          name = asset.name,
          url = asset.downloadUrl
        )
      )
    }
}
