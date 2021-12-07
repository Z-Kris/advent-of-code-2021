@file:Suppress("NOTHING_TO_INLINE")

package aoc

import kotlin.math.ceil
import kotlin.math.floor

/**
 * @author Kris | 07/12/2021
 */
inline fun <T> T.chainRepeat(times: Int, action: (Int) -> Unit): T {
    repeat(times, action)
    return this // Return itself to allow for chained code without the use of apply and the likes.
}

inline fun <K> Map<K, Long>.getOrZero(key: K) = getOrDefault(key, 0)

inline fun <T> Iterable<T>.forEachFiltered(filter: (T) -> Boolean, action: (T) -> Unit) = forEach { element -> if (filter(element)) action(element) }

inline fun <T> List<T>.medianValues(): List<T> = if (size and 0x1 == 0x1) {
    listOf(get((size + 1) / 2))
} else {
    listOf(get((size - 1) / 2), get(size / 2))
}

inline fun List<Int>.meanValues(): List<Int> = with(sum() / size.toDouble()) {
    if (this.rem(1) == 0.0) listOf(floor(this).toInt()) else listOf(floor(this).toInt(), ceil(this).toInt())
}
