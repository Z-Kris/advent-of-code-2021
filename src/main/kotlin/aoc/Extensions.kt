@file:Suppress("NOTHING_TO_INLINE")

package aoc

/**
 * @author Kris | 07/12/2021
 */
inline fun <T> T.chainRepeat(times: Int, action: (Int) -> Unit): T {
    repeat(times, action)
    return this // Return itself to allow for chained code without the use of apply and the likes.
}

inline fun <K> Map<K, Long>.getOrZero(key: K) = getOrDefault(key, 0)

inline fun <T> Iterable<T>.forEachFiltered(filter: (T) -> Boolean, action: (T) -> Unit) = forEach { element -> if (filter(element)) action(element) }
