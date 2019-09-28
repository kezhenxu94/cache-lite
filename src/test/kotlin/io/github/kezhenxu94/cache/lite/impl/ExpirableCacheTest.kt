package io.github.kezhenxu94.cache.lite.impl

import io.github.kezhenxu94.cache.lite.BaseCacheTest
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Created by kezhenxu94 on 11/14/17.
 *
 * @author kezhenxu94 (kezhenxu94 at 163 dot com)
 */
class ExpirableCacheTest : BaseCacheTest() {
	init {
		cache = ExpirableCache(PerpetualCache(), TimeUnit.SECONDS.toMillis(5))
	}

	@Test
	fun shouldExpire() {
		Thread.sleep(TimeUnit.SECONDS.toMillis(5))
		Assert.assertEquals(0, cache.size)
	}
}