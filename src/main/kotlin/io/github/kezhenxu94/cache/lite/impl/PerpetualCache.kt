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

/**
 * [PerpetualCache] caches the items perpetually unless they're manually [remove]ed.
 */
class PerpetualCache : Cache {
  private val cache = HashMap<Any, Any>()

  override val size: Int
    get() = cache.size

  override fun set(key: Any, value: Any) {
    cache[key] = value
  }

  override fun remove(key: Any) = cache.remove(key)

  override fun get(key: Any) = cache[key]

  override fun clear() = cache.clear()
}
