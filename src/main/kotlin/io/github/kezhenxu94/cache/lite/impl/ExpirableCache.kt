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

import io.github.kezhenxu94.cache.lite.Cache
import java.util.concurrent.TimeUnit

class ExpirableCache(private val delegate: Cache,
                     private val flushInterval: Long = TimeUnit.MINUTES.toMillis(1)) : Cache by delegate {
  private var lastFlushTime = System.nanoTime()

  override val size: Int
    get() {
      recycle()
      return delegate.size
    }

  override fun remove(key: Any): Any? {
    recycle()
    return delegate.remove(key)
  }

  override fun get(key: Any): Any? {
    recycle()
    return delegate[key]
  }

  private fun recycle() {
    val shouldRecycle = System.nanoTime() - lastFlushTime >= TimeUnit.MILLISECONDS.toNanos(flushInterval)
    if (shouldRecycle) {
      delegate.clear()
      lastFlushTime = System.nanoTime()
    }
  }
}
