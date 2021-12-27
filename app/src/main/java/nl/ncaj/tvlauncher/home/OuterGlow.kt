package nl.ncaj.tvlauncher.home

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb

@Composable
fun OuterGlow(
  color: Color,
  modifier: Modifier = Modifier,
  stroke: Stroke = Stroke(4.0f)
) {
  val paint = remember { getPaint(stroke, color) }
  Canvas(
    modifier = modifier
  ) {
    drawIntoCanvas { canvas ->
      canvas.nativeCanvas.drawRect(0f, 0f, size.width, size.height, paint)
    }
  }
}

private fun getPaint(
  stroke: Stroke,
  glowColor: Color
) = Paint().asFrameworkPaint().apply {
  isAntiAlias = true
  color = glowColor.toArgb()
  maskFilter = BlurMaskFilter(
    stroke.width * 2.0f,
    BlurMaskFilter.Blur.NORMAL
  )
  strokeWidth = stroke.width
  style = android.graphics.Paint.Style.STROKE
}
