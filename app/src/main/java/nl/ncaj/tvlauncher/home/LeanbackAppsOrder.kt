package nl.ncaj.tvlauncher.home

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder


fun Modifier.leanbackAppsFocusOrder(
  state: LeanbackAppsState,
  categoryIndex: Int,
  rowIndex: Int,
  columnIndex: Int,
): Modifier {
  val mapper = state.indexMapper
  val index = mapper.getAbsoluteIndex(rowIndex, columnIndex)
  val currentCategory = state.focusRequesters[categoryIndex]
  val nextCategory = state.focusRequesters.getOrNull(categoryIndex + 1)

  return this.focusOrder(currentCategory[index]) {
    left = if (columnIndex > 0) {
      currentCategory[index - 1]
    } else {
      currentCategory[index]
    }

    right = if (rowIndex == mapper.getNrOfRows(categoryIndex) - 1
      && columnIndex == mapper.getNrOfItemsInLastRow(categoryIndex) - 1
    ) {
      currentCategory[index]
    } else if (columnIndex < state.columns - 1) {
      currentCategory[index + 1]
    } else {
      currentCategory[index]
    }

    up = if (rowIndex == 0 && categoryIndex > 0) {
      val prev = state.focusRequesters[categoryIndex - 1]
      val requestedIndex = columnIndex.coerceAtMost(mapper.getNrOfItemsInLastRow(categoryIndex-1)-1)
      val nrOfRows = mapper.getNrOfRows(categoryIndex - 1) -1
      val absoluteIndex = mapper.getAbsoluteIndex(nrOfRows, requestedIndex)
      prev[absoluteIndex]
    } else if (rowIndex > 0) {
      currentCategory[index - state.columns]
    } else if (rowIndex == 0 && categoryIndex == 0 ){
      FocusRequester.Default // allow the header to be focused
    } else {
      currentCategory[index]
    }

    down = if (rowIndex == mapper.getNrOfRows(categoryIndex) - 1
      && categoryIndex < state.focusRequesters.size - 1
    ) {
      val index1 = columnIndex.coerceAtMost(state.focusRequesters[categoryIndex + 1].size - 1)
      nextCategory?.get(index1) ?: currentCategory[index]
    } else if (rowIndex < mapper.getNrOfRows(categoryIndex) - 1) {
      currentCategory[(index + state.columns).coerceAtMost(currentCategory.size - 1)]
    } else {
      currentCategory[index]
    }
  }
}
