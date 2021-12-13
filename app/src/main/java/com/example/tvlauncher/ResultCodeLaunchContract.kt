package com.example.tvlauncher

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable

/**
 * Contract to just start an activity without expecting any specific result, returns
 * just the `resultCode` instead.
 * Can be useful when used in a composable function without the need of a context object.
 *
 * @see rememberActivityLauncher
 */
internal class ResultCodeLaunchContract(
  private val intent: Intent
) : ActivityResultContract<Unit, Int>() {
  override fun createIntent(context: Context, input: Unit) = intent
  override fun parseResult(resultCode: Int, intent: Intent?) = resultCode

  companion object {
    /**
     * Starts activity for result without expecting any result value
     * @see rememberLauncherForActivityResult
     */
    @Composable
    fun rememberActivityLauncher(
      contract: ActivityResultContract<Unit, Int>,
      onResult: (Int) -> Unit = {}
    ) = rememberLauncherForActivityResult(contract) { onResult(it) }
  }
}
