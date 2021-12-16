package aoc.day9

import aoc.OFFSETS
import aoc.Point
import aoc.Puzzle
import java.util.*
import kotlin.math.max

/**
 * @author Kris | 09/12/2021
 */
object Day9 : Puzzle<LavaTubes, Int>(9) {
    override fun Sequence<String>.parse(): LavaTubes {
        val tubes = toList().map { it.map(Character::getNumericValue) }
        val maxRow = tubes.size
        val maxCol = tubes.first().size
        require(tubes.all { it.size == maxCol })
        return LavaTubes(tubes, maxRow, maxCol)
    }

    override fun LavaTubes.solvePartOne(): Int = findLowPoints().sumOf { this[it] + 1 }
    override fun LavaTubes.solvePartTwo(): Int = findLowPoints().map(::numOfLowPoints).sortedDescending().take(3).reduce(Int::times)

    private fun LavaTubes.findLowPoints(): List<Point> = points.filter { this[it] < requireNotNull(findNeighbours(it).minOrNull()) }
    private fun LavaTubes.findNeighbours(point: Point) = findNeighbouringPoints(point).map(::get)
}

private fun LavaTubes.findNeighbouringPoints(point: Point) = OFFSETS.map(point::merge).filter(::contains)

data class LavaTubes(val tubes: List<List<Int>>, val maxRow: Int, val maxCol: Int) {
    private val dimension = max(maxRow, maxCol)
    private val rowRange = 0 until maxRow
    private val colRange = 0 until maxCol
    val points = rowRange.flatMap { row -> colRange.map { col -> Point(row, col) } }
    operator fun get(point: Point) = tubes[point.x][point.y]
    operator fun contains(point: Point) = point.x in rowRange && point.y in colRange
    private fun emptyVisitedNodes() = BitSet(dimension * dimension)
    operator fun BitSet.get(point: Point) = get(point.x * dimension + point.y)
    operator fun BitSet.set(point: Point, value: Boolean) = set(point.x * dimension + point.y, value)

    fun numOfLowPoints(point: Point): Int = emptyVisitedNodes().apply { visitLowNodes(point, this) }.cardinality()

    private fun visitLowNodes(point: Point, visited: BitSet) {
        if (visited[point] || this[point] == 9) return
        visited[point] = true
        findNeighbouringPoints(point).forEach { visitLowNodes(it, visited) }
    }
}
