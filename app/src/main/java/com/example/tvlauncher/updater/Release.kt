package com.example.tvlauncher.updater

import java.util.*

internal data class Release(
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
