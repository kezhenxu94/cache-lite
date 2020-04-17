package io.github.kezhenxu94.cache.lite.impl.typed

import io.github.kezhenxu94.cache.lite.BaseCacheTestTyped
import org.junit.Assert
import org.junit.Test

internal class PerpetualCacheTypedTest : BaseCacheTestTyped() {
  init {
      cache = PerpetualCacheTyped()
  }

  @Test
  fun shouldKeepAllEntries() {
      for (i in 0..99) {
          Assert.assertNotNull(cache[i])
      }
  }
}