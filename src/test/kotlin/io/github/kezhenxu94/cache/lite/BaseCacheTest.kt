package io.github.kezhenxu94.cache.lite

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by kezhenxu94 on 11/14/17.
 *
 * @author kezhenxu94 (kezhenxu94 at 163 dot com)
 */
abstract class BaseCacheTest {
	protected lateinit var cache: Cache

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
