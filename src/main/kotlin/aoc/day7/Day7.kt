package aoc.day7

import aoc.Puzzle
import kotlin.math.abs

/**
 * @author Kris | 07/12/2021
 */
object Day7 : Puzzle<Crabs, Int>(7) {
    override fun Sequence<String>.parse(): Crabs {
        val positions = single().split(',').map(String::toInt)
        val farthestPosition = positions.maxOf { it }
        return Crabs(positions, farthestPosition)
    }

    private inline fun Crabs.computeLeastAmountOfSteps(transformer: FuelTransformer) =
        positionRange.minOf { position -> positions.sumOf { transformer(abs(it - position)) } }

    override fun Crabs.solvePartOne() = computeLeastAmountOfSteps(Int::toInt)
    override fun Crabs.solvePartTwo() = computeLeastAmountOfSteps(Int::incrementingSum)
}

data class Crabs(val positions: List<Int>, private val farthestPosition: Int) {
    val positionRange: IntRange get() = 0 until farthestPosition
}
private val Int.incrementingSum get() = this * (this + 1) / 2
private typealias FuelTransformer = (distance: Int) -> Int
