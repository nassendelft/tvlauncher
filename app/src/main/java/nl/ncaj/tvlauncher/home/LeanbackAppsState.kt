package nl.ncaj.tvlauncher.home

import androidx.annotation.Px
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlin.math.ceil

@Composable
fun rememberLeanbackAppState(
  lazyListState: LazyListState,
  categories: List<LeanbackCategory>,
  rowSpacing: Dp = 20.dp,
  columns: Int = 5,
  itemRatio: Float = 0.5625f,
) = with(LocalDensity.current) {
  remember {
    LeanbackAppsState(
      rowSpacing.toPx(),
      lazyListState,
      categories.map { it.apps.map { FocusRequester() } },
      columns,
      itemRatio
    )
  }
}

class LeanbackAppsState(
  @Px private val rowSpacing: Float,
  val lazyListState: LazyListState,
  val focusRequesters: List<List<FocusRequester>>,
  val columns: Int,
  val itemRatio: Float,
) {
  var focusedRow = 0
    private set

  var focusedCategory = 0
    private set

  val itemSpacing = rowSpacing.dp

  private var savedOffset = 0f
  private var scrolledToEnd = false

  /**
   * Helper to map between different type of indices.
   */
  val indexMapper = CategoryIndexMapper(focusRequesters.map { it.size }, columns)

  /**
   * Sets the focused row of category.
   * Calling this function will animate the grid so that the
   * focus will stay in screen.
   */
  suspend fun setFocusedRow(category: Int, row: Int) {
    if (focusedCategory == category && focusedRow == row) return
    val selectedRow = indexMapper.getAbsoluteRow(category, row)

    val direction = selectedRow.compareTo(focusedRow)

    if (direction == -1 && scrolledToEnd
      && selectedRow > lazyListState.firstVisibleItemIndex
    ) {
      // don't scroll up when we hit the bottom
      return
    } else if (direction == -1 && scrolledToEnd) {
      // we were at the end of the list before
      // so lets set the scroll position to what it was
      // before scrolling down
      scrolledToEnd = false
      lazyListState.animateScrollBy(-savedOffset)
      return
    }

    val scrollBy = when {
      lazyListState.firstVisibleItemIndex == 0
        && lazyListState.firstVisibleItemScrollOffset == 0
        && direction == 1 -> {
        // make sure that the first scroll is half the size of the
        // first item in the list so we know there's another item above
        (lazyListState.layoutInfo.visibleItemsInfo.first().size.toFloat() + (rowSpacing * 2)) / 2
      }
      direction == 1 -> {
        // scrolling down, scroll the size of the previous
        // selected row height
        lazyListState.layoutInfo.visibleItemsInfo[1].size + rowSpacing
      }
      direction == -1 -> {
        // scrolling up, just scroll the size of the first visible item height
        -(lazyListState.layoutInfo.visibleItemsInfo.first().size + rowSpacing)
      }
      else -> 0f
    }

    val actualScrolled = try {
      lazyListState.animateScrollBy(scrollBy, tween())
    } catch (e: CancellationException) {
      0f
    }

    if (indexMapper.getAbsoluteRow(category, row) > indexMapper.totalNrOfRows - 2) {
      scrolledToEnd = true
    }

    savedOffset = actualScrolled
    focusedRow = selectedRow
    focusedCategory = category
  }

  class CategoryIndexMapper internal constructor(
    private val categorySizes: List<Int>,
    private val columns: Int
  ) {

    /**
     * The sum of all category rows
     */
    val totalNrOfRows = categorySizes.indices.sumOf { getNrOfRows(it) }

    /**
     * Gets the absolute index of a row.
     * An absolute row index is the index of all previous categories rows + given [rowIndex]
     *
     * @param categoryIndex the index of the category we are trying to get the absolute index for
     * @param rowIndex the relative row index of the given [categoryIndex]
     */
    fun getAbsoluteRow(categoryIndex: Int, rowIndex: Int) = categorySizes
      .subList(0, categoryIndex)
      .sumOf { cat -> ceil(cat.toFloat() / columns).toInt() } + rowIndex

    /**
     * Gets an absolute index of given row and column.
     * An absolute index is a linear index instead of a grid one that uses rows and columns.
     */
    fun getAbsoluteIndex(rowIndex: Int, columnIndex: Int) = (rowIndex * columns) + columnIndex

    /**
     * Get the number of rows for the given category
     *
     * @param categoryIndex the index of the category where we return the nr of rows for
     */
    fun getNrOfRows(categoryIndex: Int) =
      ceil(categorySizes[categoryIndex].toFloat() / columns).toInt()

    /**
     * Gets the number of items found in the last row for the given category
     * This functions is needed because the last row of a category can have
     * less items then [LeanbackAppsState.columns].
     *
     * @param categoryIndex the index of the category where we want to know the number or items for
     */
    fun getNrOfItemsInLastRow(categoryIndex: Int) =
      (categorySizes[categoryIndex] % columns).let { if (it == 0) columns else it }
  }
}
