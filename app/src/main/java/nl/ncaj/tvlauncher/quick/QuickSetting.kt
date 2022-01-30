package nl.ncaj.tvlauncher.quick

import androidx.annotation.DrawableRes
import nl.ncaj.tvlauncher.R

sealed class QuickSetting(
  val label: String,
  @DrawableRes val iconResId: Int,
) {
  object RunningApps : QuickSetting(
    label = "Running apps",
    iconResId = R.drawable.ic_outline_settings_applications_24
  )
}
