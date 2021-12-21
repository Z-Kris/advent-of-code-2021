package aoc.puzzles

import aoc.OFFSETS
import aoc.Point
import java.util.*

/**
 * @author Kris | 15/12/2021
 */
object Day15 : Puzzle<Cavern, Int>(15) {
    override fun List<String>.parse(): Cavern = map { row -> row.map(Character::getNumericValue) }
    private const val MAX_RISK = 9
    private val EXPAND_RANGE = 1 until 5
    private val Cavern.dimensions: Dimensions get() = size to first().size
    private fun Cavern.validateHeight(size: Int) = require(all { it.size == size })

    private fun Cavern.computeRiskOfShortestPath(): Int {
        val (width, height) = dimensions
        validateHeight(height)
        val distances = IntArray(width * height) { Int.MAX_VALUE }
        val queue = PriorityQueue<Point> { a, b -> distances[a, width].compareTo(distances[b, width]) }
        initializeStart(distances, queue, width)
        visitAll(distances, queue, BitSet(width * height))
        return distances[distances.lastIndex]
    }

    private fun initializeStart(distances: Distances, queue: PriorityQueue<Point>, width: Int) {
        queue += Point.ZERO
        distances[Point.ZERO, width] = 0
    }

    private fun Cavern.visitAll(distances: Distances, queue: PriorityQueue<Point>, visited: BitSet) {
        val dimensions = this.dimensions
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            visited += pointIndex(next)
            for (point in next.findNeighbouringPoints()) {
                if (point !in dimensions || pointIndex(point) in visited) continue
                val distance = distances[next, dimensions] + this[point]
                if (distance >= distances[point, dimensions]) continue
                distances[point, dimensions] = distance
                queue += point
            }
        }
    }

    private fun Point.findNeighbouringPoints() = OFFSETS.map(::merge)
    private fun Cavern.pointIndex(point: Point): Int = point.x * size + point.y
    private operator fun Dimensions.contains(point: Point): Boolean = let { (width, height) -> point.inBounds(width, height) }
    private operator fun BitSet.contains(pointIndex: Int): Boolean = get(pointIndex)
    private operator fun BitSet.plusAssign(pointIndex: Int) = set(pointIndex, true)
    private operator fun Cavern.get(point: Point) = this[point.x][point.y]
    private operator fun Distances.get(point: Point, dimensions: Pair<Int, Int>) = get(point, dimensions.first)
    private operator fun Distances.get(point: Point, width: Int) = this[point.index(width)]
    private operator fun Distances.set(point: Point, dimensions: Pair<Int, Int>, value: Int) = set(point, dimensions.first, value)
    private operator fun Distances.set(point: Point, width: Int, value: Int) {
        this[point.index(width)] = value
    }

    private fun Cavern.expand(): Cavern = expandRight().let { EXPAND_RANGE.fold(it) { acc, step -> acc + it.expandBelow(step) } }
    private fun Cavern.expandRight() = map { EXPAND_RANGE.fold(it) { acc, step -> acc + it.expand(step) } }
    private fun Cavern.expandBelow(value: Int) = map { it.expand(value) }
    private fun List<Int>.expand(value: Int) = map { (it + value).wrapped }
    private val Int.wrapped get() = if (this > MAX_RISK) (this - MAX_RISK) else this

    override fun Cavern.solvePartOne(): Int = computeRiskOfShortestPath()
    override fun Cavern.solvePartTwo(): Int = expand().computeRiskOfShortestPath()
}

private typealias Cavern = List<List<Int>>
private typealias Distances = IntArray
private typealias Dimensions = Pair<Int, Int>
