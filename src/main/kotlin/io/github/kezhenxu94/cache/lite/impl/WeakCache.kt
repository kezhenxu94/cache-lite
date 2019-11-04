package io.github.kezhenxu94.cache.lite.impl

import io.github.kezhenxu94.cache.lite.Cache

import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

/**
 * Created by kezhenxu94 on 11/14/17.
 *
 * @author kezhenxu94 (kezhenxu94 at 163 dot com)
 */
class WeakCache(private val delegate: Cache) : Cache by delegate {
	private val referenceQueue = ReferenceQueue<Any>()

	private class WeakEntry internal constructor(
			internal val key: Any,
			value: Any,
			referenceQueue: ReferenceQueue<Any>) : WeakReference<Any>(value, referenceQueue)

	override fun set(key: Any, value: Any) {
		removeUnreachableItems()
		val weakEntry = WeakEntry(key, value, referenceQueue)
		delegate[key] = weakEntry
	}

	override fun remove(key: Any) {
		delegate.remove(key)
		removeUnreachableItems()
	}

	override fun get(key: Any): Any? {
		val weakEntry = delegate[key] as WeakEntry?
		weakEntry?.get()?.let { return it }
		delegate.remove(key)
		return null
	}

	private fun removeUnreachableItems() {
		var weakEntry = referenceQueue.poll() as WeakEntry?
		while (weakEntry != null) {
			val key = weakEntry.key
			delegate.remove(key)
			weakEntry = referenceQueue.poll() as WeakEntry?
		}
	}
}
