package com.example.tvlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val intents = ApplicationResolver(application)
    val apps = intents.getLeanbackLaunchApplications().map { it.asLeanbackApp }

    setContent {
      LeanbackAppGrid(apps)
    }
  }

  override fun onBackPressed() {
    // no-op - for now, to prevent closing this activity
  }
}
