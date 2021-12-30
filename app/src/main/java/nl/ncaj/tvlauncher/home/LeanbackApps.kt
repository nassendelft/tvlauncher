package nl.ncaj.tvlauncher.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.palette.graphics.Palette

data class LeanbackCategory(
  val label: String,
  val apps: List<LeanbackApp>
)

data class LeanbackApp(
  val name: CharSequence,
  val banner: Painter,
  val packageName: String,
  val isGame: Boolean,
  val palette: Palette
) {
  val strokeColor = Color(palette.getLightVibrantColor(palette.getVibrantColor(0)))
}
