package aoc.day11

import aoc.Point
import aoc.Puzzle

/**
 * @author Kris | 11/12/2021
 */
object Day11 : Puzzle<Grid, Int>(11) {
    private const val PART_1_REPETITIONS = 100
    override fun Sequence<String>.parse(): Grid {
        val list = toList()
        val elements = list.flatMap { it.map(Character::getNumericValue) }
        require(list.size == elements.size / list.size)
        return Grid(elements, list.size)
    }

    override fun Grid.solvePartOne(): Int = DumboOctupuses(list.toMutableList(), dimension).sumOfRepetitions(PART_1_REPETITIONS)
    override fun Grid.solvePartTwo() = DumboOctupuses(list.toMutableList(), dimension).firstOccurrenceOfFullyLitGrid()
}

private const val MIN_ENERGY = 0
private const val MAX_ENERGY = 10
private val NEARBY_NODES_RANGE = -1..1
private inline val Int.isExhausted get() = this == MIN_ENERGY

data class Grid(val list: List<Int>, val dimension: Int)
private data class DumboOctupuses(
    private val octopuses: MutableList<Int>,
    private val dimension: Int
) : MutableList<Int> by octopuses {
    private val nodesRange = 0 until dimension
    private fun reset() = forEachIndexed { index, value -> if (value == MAX_ENERGY) this[index] = MIN_ENERGY }
    private fun exhaustedCount() = count(Int::isExhausted)
    private inline val Int.toPoint get() = Point(this / dimension, this % dimension)
    private inline val Point.toIndex get() = x * dimension + y

    private inline fun forEachWithinRangeNode(point: Point, consumer: (point: Point) -> Unit) {
        NEARBY_NODES_RANGE.forEach { x -> NEARBY_NODES_RANGE.forEach { y -> consumer(Point(point.x + x, point.y + y)) } }
    }

    private fun increment(index: Int) = increment(index.toPoint)
    private fun increment(point: Point) {
        if (point.x !in nodesRange || point.y !in nodesRange || this[point.toIndex] == MAX_ENERGY) return
        if (++this[point.toIndex] == MAX_ENERGY) forEachWithinRangeNode(point, ::increment)
    }

    private fun cycle(): Int {
        indices.forEach(::increment)
        reset()
        return exhaustedCount()
    }

    fun sumOfRepetitions(repetitions: Int): Int = (0 until repetitions).sumOf { cycle() }
    fun firstOccurrenceOfFullyLitGrid(): Int = generateSequence(1, Int::inc).indexOfFirst { cycle() == size } + 1
}
