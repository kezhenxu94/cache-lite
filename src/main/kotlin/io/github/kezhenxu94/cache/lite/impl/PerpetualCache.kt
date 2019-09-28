package io.github.kezhenxu94.cache.lite.impl

import io.github.kezhenxu94.cache.lite.Cache
import java.util.*

/**
 * Created by kezhenxu94 on 11/14/17.
 *
 * @author kezhenxu94 (kezhenxu94 at 163 dot com)
 */
class PerpetualCache : Cache {
	private val cache = HashMap<Any, Any>()

	override val size: Int
		get() = cache.size

	override fun set(key: Any, value: Any) {
		this.cache[key] = value
	}

	override fun remove(key: Any) = this.cache.remove(key)

	override fun get(key: Any) = this.cache[key]

	override fun clear() = this.cache.clear()
}
