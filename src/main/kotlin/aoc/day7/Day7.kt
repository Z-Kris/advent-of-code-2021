package aoc.day7

import aoc.*
import aoc.SortedList.Companion.toSortedList
import kotlin.math.abs

/**
 * @author Kris | 07/12/2021
 */
@Suppress("NOTHING_TO_INLINE")
object Day7 : Puzzle<Crabs, Int>(7) {
    override fun Sequence<String>.parse() = single().split(',').map(String::toInt).toSortedList()

    private inline fun Crabs.minSumOf(positions: List<Int>, transformer: FuelTransformer) = positions.minOf { sum(it, transformer) }
    private inline fun Crabs.sum(position: Int, transformer: FuelTransformer) = sumOf { transformer(abs(it - position)) }

    override fun Crabs.solvePartOne() = minSumOf(medianValues(), Int::self)
    override fun Crabs.solvePartTwo() = minSumOf(meanValues(), Int::incrementingSum)
}
private val Int.incrementingSum get() = this * (this + 1) / 2
private typealias FuelTransformer = (distance: Int) -> Int
private typealias Crabs = SortedList<Int>
