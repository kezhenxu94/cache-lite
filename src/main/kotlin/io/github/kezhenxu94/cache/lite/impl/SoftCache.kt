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

import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference

class SoftCache(private val delegate: Cache) : Cache by delegate {
  private val referenceQueue = ReferenceQueue<Any>()

  private class SoftEntry internal constructor(
      internal val key: Any,
      value: Any,
      referenceQueue: ReferenceQueue<Any>) : SoftReference<Any>(value, referenceQueue)

  override fun set(key: Any, value: Any) {
    removeUnreachableItems()
    val softEntry = SoftEntry(key, value, referenceQueue)
    delegate[key] = softEntry
  }

  override fun remove(key: Any) {
    delegate.remove(key)
    removeUnreachableItems()
  }

  override fun get(key: Any): Any? {
    val softEntry = delegate[key] as SoftEntry?
    softEntry?.get()?.let { return it }
    delegate.remove(key)
    return null
  }

  private fun removeUnreachableItems() {
    var softEntry = referenceQueue.poll() as SoftEntry?
    while (softEntry != null) {
      val key = softEntry.key
      delegate.remove(key)
      softEntry = referenceQueue.poll() as SoftEntry?
    }
  }
}
