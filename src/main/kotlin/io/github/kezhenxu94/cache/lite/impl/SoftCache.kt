package io.github.kezhenxu94.cache.lite.impl

import io.github.kezhenxu94.cache.lite.Cache

import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference

/**
 * Created by kezhenxu94 on 11/14/17.
 *
 * @author kezhenxu94 (kezhenxu94 at 163 dot com)
 */
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
