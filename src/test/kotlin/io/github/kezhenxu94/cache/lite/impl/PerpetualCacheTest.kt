package io.github.kezhenxu94.cache.lite.impl

import io.github.kezhenxu94.cache.lite.BaseCacheTest
import org.junit.Assert
import org.junit.Test

/**
 * Created by kezhenxu94 on 11/14/17.
 *
 * @author kezhenxu94 (kezhenxu94 at 163 dot com)
 */
class PerpetualCacheTest : BaseCacheTest() {
	init {
		cache = PerpetualCache()
	}

	@Test
	fun shouldKeepAllEntries() {
		for (i in 0..99) {
			Assert.assertNotNull(cache[i])
		}
	}
}
