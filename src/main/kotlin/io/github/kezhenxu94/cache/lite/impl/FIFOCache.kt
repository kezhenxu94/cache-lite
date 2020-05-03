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

import io.github.kezhenxu94.cache.lite.GenericCache

/**
 * [FIFOCache] caches at most [minimalSize] items that are recently [set].
 */
class FIFOCache<K, V>(private val delegate: GenericCache<K, V>, private val minimalSize: Int = DEFAULT_SIZE) :
  GenericCache<K, V> by delegate {
  private val keyMap = object : LinkedHashMap<K, Boolean>(minimalSize, .75f) {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, Boolean>): Boolean {
      val tooManyCachedItems = size > minimalSize
      if (tooManyCachedItems) eldestKeyToRemove = eldest.key
      return tooManyCachedItems
    }
  }

  private var eldestKeyToRemove: K? = null

  override fun set(key: K, value: V) {
    delegate[key] = value
    cycleKeyMap(key)
  }

  override fun get(key: K): V? {
    keyMap[key]
    return delegate[key]
  }

  override fun clear() {
    keyMap.clear()
    delegate.clear()
  }

  private fun cycleKeyMap(key: K) {
    keyMap[key] = PRESENT
    eldestKeyToRemove?.let { delegate.remove(it) }
    eldestKeyToRemove = null
  }

  companion object {
    private const val DEFAULT_SIZE = 100
    private const val PRESENT = true
  }
}
