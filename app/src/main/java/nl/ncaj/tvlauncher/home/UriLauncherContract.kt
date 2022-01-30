package nl.ncaj.tvlauncher.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

/**
 * Contract to start open the given URI.
 */
object UriLauncherContract : ActivityResultContract<UriLauncherContract.Input, Int>() {

  override fun createIntent(context: Context, input: Input) =
    Intent.parseUri(input.uri.toString(), input.flags)

  override fun parseResult(resultCode: Int, intent: Intent?) = resultCode

  /**
   * Input for [UriLauncherContract]
   *
   * @param uri the uri to start the intent with
   * @param flags Additional processing flags.
   *
   * @see Uri.parse
   */
  data class Input(
    val uri: Uri,
    val flags: Int = 0
  )

  /**
   * Convenience function for [ManagedActivityResultLauncher.launch] without
   * the need to create a new instance of [Input] ourselves.
   *
   * @param uri the uri to start the intent with
   * @param flags Additional processing flags.
   *
   * @see Uri.parse
   */
  fun ManagedActivityResultLauncher<Input, Int>.launch(uri: Uri, flags: Int = 0) =
    launch(Input(uri, flags))
}

typealias UriLauncher = ManagedActivityResultLauncher<UriLauncherContract.Input, Int>
