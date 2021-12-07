package aoc.day7

import aoc.Puzzle
import aoc.meanValues
import aoc.medianValues
import kotlin.math.abs

/**
 * @author Kris | 07/12/2021
 */
@Suppress("NOTHING_TO_INLINE")
object Day7 : Puzzle<Crabs, Int>(7) {
    override fun Sequence<String>.parse(): Crabs = Crabs(single().split(',').map(String::toInt).sorted())

    private inline fun Crabs.computeLeastAmountOfSteps(inputs: List<Int>, transformer: FuelTransformer): Int =
        inputs.minOf { position -> positions.sumOf { transformer(abs(it - position)) } }

    override fun Crabs.solvePartOne() = computeLeastAmountOfSteps(positions.medianValues(), Int::toInt)
    override fun Crabs.solvePartTwo() = computeLeastAmountOfSteps(positions.meanValues(), Int::incrementingSum)
}

data class Crabs(val positions: List<Int>)
private val Int.incrementingSum get() = this * (this + 1) / 2
private typealias FuelTransformer = (distance: Int) -> Int
