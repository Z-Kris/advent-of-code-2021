package aoc.day15

import aoc.OFFSETS
import aoc.Point
import aoc.Puzzle
import java.util.*

/**
 * @author Kris | 15/12/2021
 */
object Day15 : Puzzle<Cavern, Int>(15) {
    override fun Sequence<String>.parse(): Cavern = toList().map { row -> row.map(Character::getNumericValue) }
    private const val MAX_RISK = 9
    private val EXPAND_RANGE = 1 until 5
    private val Cavern.dimensions get() = size to first().size
    private fun Cavern.validateHeight(size: Int) = require(all { it.size == size })

    private fun Cavern.computeRiskOfShortestDistance(): Int {
        val (width, height) = dimensions
        validateHeight(height)
        val distances = Array(width) { IntArray(height) { Int.MAX_VALUE } }
        val queue = PriorityQueue<Point>(width * height) { a, b -> distances[a].compareTo(distances[b]) }
        initializeStart(distances, queue)
        pathfind(distances, queue, BitSet(width * height))
        return distances.lastValue()
    }

    private fun initializeStart(distances: Distances, queue: PriorityQueue<Point>) {
        queue += Point.ZERO
        distances[Point.ZERO] = 0
    }

    private fun Cavern.pathfind(distances: Distances, queue: PriorityQueue<Point>, visited: BitSet) {
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            visited += pointIndex(next)
            for (point in next.findNeighbouringPoints()) {
                if (point !in this || pointIndex(point) in visited) continue
                val distance = distances[next] + this[point]
                if (distance >= distances[point]) continue
                distances[point] = distance
                queue += point
            }
        }
    }

    private fun Point.findNeighbouringPoints() = OFFSETS.map(::merge)
    private fun Cavern.pointIndex(point: Point): Int = point.x * dimensions.first + point.y
    private operator fun Cavern.contains(point: Point): Boolean = dimensions.let { (width, height) -> point.inBounds(width, height) }
    private operator fun BitSet.contains(pointIndex: Int): Boolean = get(pointIndex)
    private operator fun BitSet.plusAssign(pointIndex: Int) = set(pointIndex, true)
    private operator fun Cavern.get(point: Point) = this[point.x][point.y]
    private fun Distances.lastValue() = this[lastIndex][last().lastIndex]
    private operator fun Distances.get(point: Point) = this[point.x][point.y]
    private operator fun Distances.set(point: Point, value: Int) {
        this[point.x][point.y] = value
    }

    private fun Cavern.expand(): Cavern = expandRight().let { EXPAND_RANGE.fold(it) { acc, step -> acc + it.expandBelow(step) } }
    private fun Cavern.expandRight() = map { EXPAND_RANGE.fold(it) { acc, step -> acc + it.expand(step) } }
    private fun Cavern.expandBelow(value: Int) = map { row -> row.expand(value) }
    private fun List<Int>.expand(value: Int) = map { (it + value).wrapped }
    private val Int.wrapped get() = if (this > MAX_RISK) (this - MAX_RISK) else this

    override fun Cavern.solvePartOne(): Int = computeRiskOfShortestDistance()
    override fun Cavern.solvePartTwo(): Int = expand().computeRiskOfShortestDistance()
}

private typealias Cavern = List<List<Int>>
private typealias Distances = Array<IntArray>
