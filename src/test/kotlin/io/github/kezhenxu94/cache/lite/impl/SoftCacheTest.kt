/**
 * Copyright 2020 kezhenxu94
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.kezhenxu94.cache.lite.impl

import io.github.kezhenxu94.cache.lite.BaseCacheTest
import org.junit.Assert
import org.junit.Test

internal class SoftCacheTest : BaseCacheTest() {
  init {
    cache = SoftCache(PerpetualCache())
  }

  @Test
  fun shouldClearUnreachableItems() {
    val size = 2048 * 2
    for (i in 0 until size) {
      cache[i] = ByteArray(ONE_MEGABYTE)
    }
    System.gc()
    Assert.assertTrue(cache.size < size)
  }

  companion object {
    private const val ONE_MEGABYTE = 1024 * 1024
  }
}
