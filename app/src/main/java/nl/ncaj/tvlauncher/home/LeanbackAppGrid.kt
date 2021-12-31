package nl.ncaj.tvlauncher.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import nl.ncaj.tvlauncher.onUserInteraction

@Composable
fun LeanbackAppGrid(
  categories: List<LeanbackCategory>,
  openApplication: (LeanbackApp) -> Unit,
  modifier: Modifier = Modifier,
  state: LeanbackAppsState = rememberLeanbackAppState(
    rememberLazyListState(),
    categories
  ),
  headerItem: LazyListScope.(Modifier) -> Unit = {},
  footerItem: LazyListScope.(Modifier) -> Unit = {}
) {
  val headerFocusRequester = remember { FocusRequester() }
  val scope = rememberCoroutineScope()
  var lastKeyEvent by remember { mutableStateOf(0L) }

  // focusRequest is needed to give initial focus
  // to first item on composition
  LaunchedEffect(Unit) { state.focusRequesters.firstOrNull()?.firstOrNull()?.requestFocus() }

  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(state.itemSpacing),
    contentPadding = PaddingValues(40.dp),
    state = state.lazyListState,
    modifier = modifier
      .onPreviewKeyEvent {
        if (it.type == KeyEventType.KeyDown
          && it.key == Key.DirectionUp || it.key == Key.DirectionDown
        ) {
          // throttle the up and down directions so we have time to animate the scrolling
          val currentTimeMillis = System.currentTimeMillis()
          val shouldThrottle = lastKeyEvent != 0L && currentTimeMillis - lastKeyEvent < 300
          if (currentTimeMillis - lastKeyEvent > 300 || lastKeyEvent == 0L) lastKeyEvent =
            currentTimeMillis
          return@onPreviewKeyEvent shouldThrottle
        }
        return@onPreviewKeyEvent false
      }
  ) {
    headerItem(Modifier.onFocusChanged {
      if (it.hasFocus) scope.launch { state.lazyListState.animateScrollToItem(0) }
    }.focusOrder(headerFocusRequester) {
      up = headerFocusRequester
    })
    for ((categoryIndex, category) in categories.withIndex()) {
      leanbackAppCategoryItem(
        category = category,
        state = state,
        onItemClick = openApplication,
        categoryIndex = categoryIndex,
      )
    }
    footerItem(Modifier)
  }
}

private fun LazyListScope.leanbackAppCategoryItem(
  state: LeanbackAppsState,
  category: LeanbackCategory,
  onItemClick: (LeanbackApp) -> Unit,
  categoryIndex: Int,
  modifier: Modifier = Modifier,
) {
  itemsIndexed(
    items = category.apps.chunked(state.columns)
  ) { rowIndex, rowItems ->
    Column(
      modifier = modifier
    ) {
      if (rowIndex == 0) {
        BasicText(
          text = category.label,
          style = TextStyle.Default.copy(
            color = Color.White,
            fontSize = 16.sp
          ),
          modifier = Modifier.padding(bottom = state.itemSpacing)
        )
      }
      LeanbackAppRow(
        state = state,
        rowItems = rowItems,
        onItemClick = onItemClick,
        categoryIndex = categoryIndex,
        rowIndex = rowIndex
      )
    }
  }
}

@Composable
private fun LeanbackAppRow(
  state: LeanbackAppsState,
  rowItems: List<LeanbackApp>,
  onItemClick: (LeanbackApp) -> Unit,
  categoryIndex: Int,
  rowIndex: Int,
  modifier: Modifier = Modifier
) {
  val scope = rememberCoroutineScope()

  BoxWithConstraints(
    modifier = modifier
  ) {
    val cellWidth = remember {
      (maxWidth - (state.itemSpacing * (state.columns - 1))) / state.columns
    }
    val cellHeight = remember { cellWidth * state.itemRatio }
    Row(
      horizontalArrangement = Arrangement.spacedBy(state.itemSpacing),
      modifier = modifier.fillMaxWidth()
    ) {
      for ((columnIndex, item) in rowItems.withIndex()) {
        LeanbackAppItem(
          app = item,
          onClick = onItemClick,
          modifier = Modifier
            .size(cellWidth, cellHeight)
            .leanbackAppsFocusOrder(
              state = state,
              categoryIndex = categoryIndex,
              rowIndex = rowIndex,
              columnIndex = columnIndex
            )
            .onFocusChanged {
              if (it.isFocused) {
                scope.launch {
                  state.setFocusedRow(categoryIndex, rowIndex)
                }
              }
            }
        )
      }
    }
  }
}

@Composable
private fun LeanbackAppItem(
  app: LeanbackApp,
  onClick: (LeanbackApp) -> Unit,
  modifier: Modifier = Modifier,
) {
  var focused by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(if (focused) 1.3f else 1f)
  val selectedPaddingVertical = remember {
    // make sure that we have the matching padding based on the size ratio
    4.dp * (app.banner.intrinsicSize.height / app.banner.intrinsicSize.width)
  }

  Box(
    modifier = modifier
      .then(if (focused) Modifier.zIndex(1f) else Modifier)
      .scale(scale)
      .onFocusChanged { focused = it.isFocused }
      .focusable()
      .onUserInteraction { onClick(app) }
  ) {
    if (focused) {
      OuterGlow(
        color = Color.Black.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxSize()
      )
    }
    Box(
      modifier = Modifier.padding(
        horizontal = if (focused) 4.dp else 0.dp,
        vertical = if (focused) selectedPaddingVertical else 0.dp
      )
    ) {
      Image(
        painter = app.banner,
        contentDescription = app.name.toString(),
        modifier = Modifier.fillMaxSize()
      )
      AnimatedVisibility(
        visible = focused,
        enter = fadeIn(),
        exit = fadeOut(),
      ) {
        Canvas(
          modifier = Modifier.fillMaxSize()
        ) {
          drawRect(
            color = app.strokeColor,
            style = Stroke(4.0f)
          )
        }
      }
    }
  }
}

