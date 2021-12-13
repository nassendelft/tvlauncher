package nl.ncaj.tvlauncher.updater

import java.util.Date

data class Release(
    val version: String,
    val date: Date,
    val file: File
) {
  data class File(
    val size: Long,
    val name: String,
    val url: String
  )
}
