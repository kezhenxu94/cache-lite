package me.kezhenxu94.cache.lite.impl

import me.kezhenxu94.cache.lite.BaseCacheTest
import org.junit.Assert
import org.junit.Test

/**
 * Created by kezhenxu94 on 11/14/17.
 *
 * @author kezhenxu94 (kezhenxu94 at 163 dot com)
 */
class WeakCacheTest : BaseCacheTest() {
	init {
		cache = WeakCache(PerpetualCache())
	}

	@Test
	fun shouldClearUnreachableItems() {
		val size = 2048
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