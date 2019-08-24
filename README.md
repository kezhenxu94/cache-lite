Cache-Lite - An extremely light-weight cache framework for Kotlin
=================================================================

[![Build Status](https://img.shields.io/travis/kezhenxu94/cache-lite/master.svg)](https://travis-ci.org/kezhenxu94/cache-lite)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%20v2.0-blue.svg)](https://apache.org)
[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.2.40-blue.svg)](https://kotlinlang.org)
[![Kotlin Version](https://img.shields.io/codecov/c/github/kezhenxu94/cache-lite/master.svg)](https://codecov.io/gh/kezhenxu94/cache-lite) [![Join the chat at https://gitter.im/cache-lite/Lobby](https://badges.gitter.im/cache-lite/Lobby.svg)](https://gitter.im/cache-lite/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

The Cache-Lite is an extremely light-weight cache framework implemented in Kotlin, it is for study case.

You can check out the source code to learn how caching works and learn how to implement a simple cache framework by yourself.

## Background

Caching is a critical technology in many high performance scalable applications. There are many choices in caching framework, including [Ehcache](http://www.ehcache.org/), [Memcache](https://memcached.org/), [cache2k](https://cache2k.org/) etc. But today we are going to build one on our own, to learn what cache really does. Let's get started.

## Naive Version

A cache is typically a key-value store, and in Kotlin/Java there is exactly a class representing this kind of data structure: `Map`. There are chances that we have already leveraged this class to do some caching tasks. So the very first thought to build a caching framework is simply using the class `java.util.Map`.

```kotlin
val cache = HashMap<Any, Any>()
cache["key"] = "Frequently used value taken from database"
val v = cache["key"]
println(v)
cache.remove("key")
```

Quite simple, isn't it? But according to the programming principle, "Program to Interface, not Implementation", we should not couple our caching framework to the specific implementation, which is `java.util.Map` here. You may argue that `java.util.Map` **is an interface, not an implementation**. True, but not applicable here. Here we are talking about a caching system, which means that, for end users, **caching is an interface and `java.util.Map` is an implementation that they don't care**. We have to define an interface that only describes what a cache does.

## Defining `Cache` Interface

There are some basic operations on a cache, you may want to `put` a value into it, `get` a value by `key` from it, `remove` a value by `key` from it, `clear` it and know what's the `size` of it. After saying the sentence, our interface `Cache` is almost done.

```kotlin
interface Cache {
	val size: Int

	operator fun set(key: Any, value: Any)

	operator fun get(key: Any): Any?

	fun remove(key: Any): Any?

	fun clear()
}
```

Here is one thing different from what we just said, the method `set`. The reason why we name it `set` is that we want to use operator `[]` to put a value by key into the cache. By using `operator function set`, we are able to put a value into cache like this: `cache["key"] = "value"`, instead of `cache.put("key", "value")`.

## Cache Forever

One of the challenges in designing a caching framework is to deal with the expiration of the **cached items**. But for simplicity, we are ignoring this **now** and build our first cache that caches items forever until we remove it manually.

It is true that we should program to interface not implementation, but when we are **implementing an interface**, we could leverage some other implementations, here we will use `java.util.Map` to implement our first cache, `PerpetualCache`:

```kotlin
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
```

All of the methods are **delegated** to `cache`, it is fine because `cache` is `private` so that we can change our implementation without effecting our end users. Now that we have our own cache interface `Cache` and one of  implementations of it, `PerpetualCache`, we want to add more functionalities.

## LRU Cache

By now our cache will keep all the entries until we remove them manually, it can be very memory-intensive. In many caching scenarios, we assume that **the entries we recently used will be used again soon**, if that is true (mostly it is), we can keep only a certain number of entries that are recently used and remove others, this kind of flush strategy is called **[Least Recently Used](https://en.wikipedia.org/wiki/Cache_replacement_policies#LRU)**.

Since we already have `PerpetualCache` and we want to **add responsibilitis** to this class, the **[Decorator Pattern](https://en.wikipedia.org/wiki/Decorator_pattern)** is the best choice here.

```kotlin
class LRUCache(private val delegate: Cache, private val minimalSize: Int = DEFAULT_SIZE) : Cache {
	private val keyMap = object : LinkedHashMap<Any, Any>(minimalSize, .75f, true) {
		override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Any, Any>): Boolean {
			val tooManyCachedItems = size > minimalSize
			if (tooManyCachedItems) eldestKeyToRemove = eldest.key
			return tooManyCachedItems
		}
	}

	private var eldestKeyToRemove: Any? = null

	override val size: Int
		get() = delegate.size

	override fun set(key: Any, value: Any) {
		delegate[key] = value
		cycleKeyMap(key)
	}

	override fun remove(key: Any) = delegate.remove(key)

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
```

We only keep `minimalSize` entries at most. Here we leverage the class `java.util.LinkedHashMap` to trace the usage of entries, and `eldestKeyToRemove` is the one to be removed, which is the eldest entry ordered by used frequency. Method `cycleKeyMap` is responsible for removing entries that are too old and less used. Simple and straightforward.

## Expirable Cache

As we said above, expiration is critical in caching framework because it prevent our cache from growing infinitely. With the experience of `LRUCache` implementation, we know how and when to remove entries, it's time to implement an expirable cache.

```kotlin
class ExpirableCache(private val delegate: Cache,
                     private val flushInterval: Long = TimeUnit.MINUTES.toMillis(1)) : Cache {
	private var lastFlushTime = System.nanoTime()

	override val size: Int
		get() = delegate.size

	override fun set(key: Any, value: Any) {
		delegate[key] = value
	}

	override fun remove(key: Any): Any? {
		recycle()
		return delegate.remove(key)
	}

	override fun get(key: Any): Any? {
		recycle()
		return delegate[key]
	}

	override fun clear() = delegate.clear()

	private fun recycle() {
		val shouldRecycle = System.nanoTime() - lastFlushTime >= TimeUnit.MILLISECONDS.toNanos(flushInterval)
		if (!shouldRecycle) return
		delegate.clear()
	}
}
```

To make it simple, by saying expirable, we means that the **cache** is expirable, **not a single entry** in the cache is expirable. However, after knowing how the entire cache is expired, it's easy to implement a cache where entries are expired respectively.

We are given a `flushInterval`, and we will clear the cache every `flushInterval` milliseconds. It's typically a **scheduled task**, we can use a background thread to do the task, but again, to make it simple, we just `recycle` before every operation in our cache.

## Other Implementations

Besides the three implementations we discussed above, here are several implementations such as `FIFOCache`, `SoftCache` and `WeakCache`, implemented with **[First-in-first-out algorithm](https://en.wikipedia.org/wiki/FIFO_%28computing_and_electronics%29)**, **[Soft Reference](https://en.wikipedia.org/wiki/Soft_reference)**, and **[Weak Reference](https://en.wikipedia.org/wiki/Weak_reference)** respectively.

You can check out the source code [here in GitHub](https://github.com/kezhenxu94/cache-lite).
