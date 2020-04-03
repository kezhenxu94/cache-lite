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

class FIFOCacheTest : BaseCacheTest() {

	init {
		cache = FIFOCache(PerpetualCache(), 31)
	}

	@Test
	fun shouldOnlyCacheSizeItems() {
		Assert.assertEquals(cache.size, 31)
	}

	@Test
	fun shouldOnlyCacheLatestSizeItems() {
		for (i in 0..99) {
			if (i < 100 - 31) { // Only the last 31 items are cached
				Assert.assertNull(cache[i])
			} else {
				Assert.assertNotNull(cache[i])
			}
		}
	}

	@Test
	override fun shouldRemoveEntry() {
		Assert.assertNotNull(cache[69])

		cache.remove(69)

		Assert.assertNull(cache[69])
	}
}
