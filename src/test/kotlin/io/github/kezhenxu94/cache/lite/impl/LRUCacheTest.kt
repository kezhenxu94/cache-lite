package io.github.kezhenxu94.cache.lite.impl

import io.github.kezhenxu94.cache.lite.BaseCacheTest
import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Created by kezhenxu94 on 11/14/17.
 *
 * @author kezhenxu94 (kezhenxu94 at 163 dot com)
 */
class LRUCacheTest : BaseCacheTest() {
	init {
		cache = LRUCache(PerpetualCache(), 31)
	}

	@Test
	fun shouldOnlyCacheSizeItems() {
		for (i in 0..99) {
			cache[i] = i
		}
		Assert.assertEquals(cache.size, 31)
	}

	@Test
	fun shouldCacheItemsWithAccessOrder() {
		for (i in 0..30) {
			cache[i] = i
		}

		val random = Random()
		val indices = IntArray(31)

		for (i in 0..30) {
			indices[i] = random.nextInt(31)
		}

		for (index in indices) {
			cache[index]
		}

		for (index in indices) {
			Assert.assertEquals(index, cache[index])
		}
	}

	@Test
	override fun shouldRemoveEntry() {
		Assert.assertNotNull(cache[69])

		cache.remove(69)

		Assert.assertNull(cache[69])
	}
}