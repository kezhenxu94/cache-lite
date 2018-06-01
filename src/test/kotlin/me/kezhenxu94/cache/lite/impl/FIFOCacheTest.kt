package me.kezhenxu94.cache.lite.impl

import me.kezhenxu94.cache.lite.BaseCacheTest
import org.junit.Assert
import org.junit.Test

/**
 * Created by kezhenxu94 on 11/14/17.
 *
 * @author kezhenxu94 (kezhenxu94 at 163 dot com)
 */
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