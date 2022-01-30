package nl.ncaj.tvlauncher.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette

data class LeanbackCategory(
  val label: String,
  val apps: List<LeanbackApp>
)

class LeanbackApp(
  val name: CharSequence,
  val banner: Painter,
  val packageName: String,
  val isGame: Boolean,
  val palette: Palette
) {
  val strokeColor = Color(palette.getLightVibrantColor(palette.getVibrantColor(Color.White.toArgb())))

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as LeanbackApp

    if (packageName != other.packageName) return false

    return true
  }

  override fun hashCode(): Int {
    return packageName.hashCode()
  }
}
