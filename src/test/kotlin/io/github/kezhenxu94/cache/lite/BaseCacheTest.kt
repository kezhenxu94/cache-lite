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

package io.github.kezhenxu94.cache.lite

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal abstract class BaseCacheTest {
  protected lateinit var cache: GenericTypedCache<Any, Any>

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
