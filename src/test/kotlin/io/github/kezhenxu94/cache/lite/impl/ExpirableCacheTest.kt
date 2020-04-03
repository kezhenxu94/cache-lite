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
import java.util.concurrent.TimeUnit

class ExpirableCacheTest : BaseCacheTest() {
	init {
		cache = ExpirableCache(PerpetualCache(), TimeUnit.SECONDS.toMillis(5))
	}

	@Test
	fun shouldExpire() {
		Thread.sleep(TimeUnit.SECONDS.toMillis(5))
		Assert.assertEquals(0, cache.size)
	}

    @Test
    fun shouldExpireMultipleTimes() {
        Thread.sleep(TimeUnit.SECONDS.toMillis(5))
        Assert.assertEquals(0, cache.size)
        cache[1] = 1
        Assert.assertEquals(1, cache.size)
        Thread.sleep(TimeUnit.SECONDS.toMillis(5))
        Assert.assertEquals(0, cache.size)
    }
}
