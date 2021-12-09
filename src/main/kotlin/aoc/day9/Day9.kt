package aoc.day9

import aoc.Point
import aoc.Puzzle

/**
 * @author Kris | 09/12/2021
 */
@OptIn(ExperimentalStdlibApi::class)
object Day9 : Puzzle<LavaTubes, Int>(9) {
    private val offsets = listOf(Point(0, 1), Point(0, -1), Point(1, 0), Point(-1, 0))
    override fun Sequence<String>.parse(): LavaTubes {
        val tubes = toList().map { it.map(Character::getNumericValue) }
        val maxRow = tubes.size
        val maxCol = tubes.first().size
        require(tubes.all { it.size == maxCol })
        return LavaTubes(tubes, maxRow, maxCol)
    }

    override fun LavaTubes.solvePartOne(): Int = findLowPoints().sumOf { this[it] + 1 }
    override fun LavaTubes.solvePartTwo(): Int =
        findLowPoints().map { computeConnectedPoints(it, emptyVisitedNodes()) }.sortedDescending().take(3).reduce(Int::times)

    private fun LavaTubes.findLowPoints(): List<Point> = points.filter { this[it] < requireNotNull(findNeighbours(it).minOrNull()) }
    private fun LavaTubes.findNeighbours(point: Point) = findNeighbouringPoints(point).map { pos -> tubes[pos.x][pos.y] }
    private fun LavaTubes.findNeighbouringPoints(point: Point) = offsets.map(point::merge).filter(::contains)

    private fun LavaTubes.computeConnectedPoints(point: Point, visited: Array<BooleanArray>): Int {
        if (visited[point.x][point.y] || this[point] == 9) return 0
        visited[point.x][point.y] = true
        return 1 + findNeighbouringPoints(point).sumOf { computeConnectedPoints(it, visited) }
    }
}

data class LavaTubes(val tubes: List<List<Int>>, val maxRow: Int, val maxCol: Int) {
    private val rowRange get() = 0 until maxRow
    private val colRange get() = 0 until maxCol
    val points get() = rowRange.flatMap { row -> colRange.map { col -> Point(row, col) } }
    operator fun get(x: Int, y: Int) = tubes[x][y]
    operator fun get(point: Point) = tubes[point.x][point.y]
    operator fun contains(point: Point) = point.x in rowRange && point.y in colRange
    fun emptyVisitedNodes() = Array(maxRow) { BooleanArray(maxCol) }
}
