package io.github.kezhenxu94.cache.lite

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal abstract class BaseCacheTestTyped {
  protected lateinit var cache: GenericTypedCache<Int, Int>

  @Before
  fun setup() {
    for (i in 0..99) {
      cache[i] = i
    }
  }

  @After
  fun tearDown() = cache.clear()

  @Test
  fun shouldClearAllEntries() {
    Assert.assertTrue(cache.size > 0)

    cache.clear()

    Assert.assertTrue(cache.size == 0)
  }

  @Test
  open fun shouldRemoveEntry() {
    Assert.assertNotNull(cache[1])

    cache.remove(1)

    Assert.assertNull(cache[1])
  }
}