package me.kezhenxu94.cache.lite

/**
 * Created by kezhenxu94 on 11/14/17.
 *
 * @author kezhenxu94 (kezhenxu94 at 163 dot com)
 */
interface Cache {
	val size: Int

	operator fun set(key: Any, value: Any)

	operator fun get(key: Any): Any?

	fun remove(key: Any): Any?

	fun clear()
}
