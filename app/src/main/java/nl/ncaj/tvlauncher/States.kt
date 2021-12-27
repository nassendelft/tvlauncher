package nl.ncaj.tvlauncher

/**
 * Generic state class that can be used when when requesting some data.
 * It can be either [Successful] or [Failed] as a result.
 */
sealed class RequestState<T> {
  /**
   * Requesting data
   */
  class Requesting<T>: RequestState<T>()

  /**
   * Data successful requested
   */
  class Successful<T>(val value: T): RequestState<T>()

  /**
   * Data failed to retrieve
   */
  class Failed<T>(val error: Exception): RequestState<T>()
}

/**
 * Generic state class that can be used to fetch some data asynchronously.
 */
sealed class FetchDataState<T> {
  /**
   * Fetching data
   */
  class Fetching<T>: FetchDataState<T>()

  /**
   * Data that was fetched
   */
  class Data<T>(val value: T): FetchDataState<T>()
}
