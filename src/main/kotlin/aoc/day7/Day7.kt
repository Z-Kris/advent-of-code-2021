package aoc.day7

import aoc.Puzzle
import aoc.meanValues
import aoc.medianValues
import aoc.self
import kotlin.math.abs

/**
 * @author Kris | 07/12/2021
 */
@Suppress("NOTHING_TO_INLINE")
object Day7 : Puzzle<Crabs, Int>(7) {
    override fun Sequence<String>.parse() = single().split(',').map(String::toInt).sorted()

    private inline fun Crabs.minSumOf(middle: MidCrabs, transformer: FuelTransformer) = middle.minOf { sum(it, transformer) }
    private inline fun Crabs.sum(position: Int, transformer: FuelTransformer) = sumOf { transformer(abs(it - position)) }

    override fun Crabs.solvePartOne() = minSumOf(medianValues(), Int::self)
    override fun Crabs.solvePartTwo() = minSumOf(meanValues(), Int::incrementingSum)
}
private val Int.incrementingSum get() = this * (this + 1) / 2
private typealias FuelTransformer = (distance: Int) -> Int
private typealias Crabs = List<Int>
private typealias MidCrabs = List<Int>
