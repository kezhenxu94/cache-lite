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

class FIFOCache(private val delegate: Cache, private val minimalSize: Int = DEFAULT_SIZE) : Cache by delegate {
	private val keyMap = object : LinkedHashMap<Any, Any>(minimalSize, .75f) {
		override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Any, Any>): Boolean {
			val tooManyCachedItems = size > minimalSize
			if (tooManyCachedItems) eldestKeyToRemove = eldest.key
			return tooManyCachedItems
		}
	}

	private var eldestKeyToRemove: Any? = null

	override fun set(key: Any, value: Any) {
		delegate[key] = value
		cycleKeyMap(key)
	}

	override fun get(key: Any): Any? {
		keyMap[key]
		return delegate[key]
	}

	override fun clear() {
		keyMap.clear()
		delegate.clear()
	}

	private fun cycleKeyMap(key: Any) {
		keyMap[key] = PRESENT
		eldestKeyToRemove?.let { delegate.remove(it) }
		eldestKeyToRemove = null
	}

	companion object {
		private const val DEFAULT_SIZE = 100
		private const val PRESENT = true
	}
}
