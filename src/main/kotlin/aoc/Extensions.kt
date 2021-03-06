@file:Suppress("NOTHING_TO_INLINE", "NO_REFLECTION_IN_CLASS_PATH")

package aoc

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.reflect.KClass

/**
 * @author Kris | 07/12/2021
 */
inline fun <T> T.chainRepeat(times: Int, action: (Int) -> Unit): T {
    repeat(times, action)
    return this // Return itself to allow for chained code without the use of apply and the likes.
}
inline val <T> T.self: T get() = this
inline val Boolean.toInt get() = if (this) 1 else 0

inline operator fun <T> List<T>.component6(): T = get(5)

inline operator fun String.minus(regex: Regex) = replace(regex, "")

inline fun <T> List<T>.concatenate() = joinToString(separator = "")

inline fun <K> Map<K, Long>.getOrZero(key: K) = getOrDefault(key, 0)
inline fun <K> Map<K, Int>.getOrZero(key: K) = getOrDefault(key, 0)

inline val <A, B> Pair<A, B>.inv: Pair<B, A> get() = second to first

inline fun <T> Iterable<T>.forEachFiltered(filter: (T) -> Boolean, action: (T) -> Unit) = forEach { element -> if (filter(element)) action(element) }

inline fun <K> MutableMap<K, Long>.increment(key: K, count: Long) = put(key, getOrZero(key) + count)
inline fun <K> MutableMap<K, Int>.increment(key: K, count: Int) = put(key, getOrZero(key) + count)

inline fun <T> Iterable<T>.requireMax() where T : Comparable<T> = requireNotNull(maxOrNull())
inline fun <T> Iterable<T>.requireMin() where T : Comparable<T> = requireNotNull(minOrNull())
val OFFSETS = listOf(Point(0, 1), Point(0, -1), Point(1, 0), Point(-1, 0))
inline fun <T, R> Iterable<T>.permutationSearch(other: Iterable<T>, consumer: (a: T, b: T) -> R?): R? {
    for (a in this) {
        for (b in other) {
            val result = consumer(a, b)
            if (result != null) return result
        }
    }
    return null
}

inline fun Iterable<String>.chunkedBy(selector: (String) -> Boolean): List<List<String>> = buildList {
    val iterator = this@chunkedBy.iterator()
    val entries = mutableListOf<String>()
    while (iterator.hasNext()) {
        val line = iterator.next()
        if (selector(line)) {
            add(entries.toList())
            entries.clear()
            continue
        }
        entries += line
    }
    if (entries.isNotEmpty()) add(entries.toList())
}

infix fun Int.greaterThanOrEqual(other: Int) = this >= other
infix fun Int.lesserThanOrEqual(other: Int) = this <= other

inline fun <T> runIfNotNull(element: T?, function: (T) -> Unit): Boolean {
    if (element == null) return false
    function(element)
    return true
}

inline val Int.gaussSum get() = this * (this + 1) / 2

inline fun <T> SortedList<T>.medianValues(): List<T> = if (size and 0x1 == 0x1) {
    listOf(get((size + 1) / 2))
} else {
    listOf(get((size - 1) / 2), get(size / 2))
}

inline fun String.toCharPair(): Pair<Char, Char> {
    require(length == 2)
    val (a, b) = toList()
    return a to b
}

inline fun SortedList<Int>.meanValues(): List<Int> = with(sum() / size.toDouble()) {
    if (this.rem(1) == 0.0) listOf(floor(this).toInt()) else listOf(floor(this).toInt(), ceil(this).toInt())
}

inline fun <reified T> singleSealedInstance(function: (T) -> Boolean): T =
    T::class.sealedSubclasses.firstNotNullOf { subClass -> subClass.objectInstance?.let { if (function(it)) it else null } }

inline fun <reified T : Any> sealedClassObjectInstances(): List<T> = T::class.sealedSubclasses.mapNotNull(KClass<out T>::objectInstance)
