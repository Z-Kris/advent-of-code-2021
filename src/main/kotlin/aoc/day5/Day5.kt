@file:Suppress("RemoveExplicitTypeArguments", "EXPERIMENTAL_IS_NOT_ENABLED")

package aoc.day5

import aoc.Point
import aoc.Puzzle
import aoc.forEachFiltered
import java.util.BitSet
import kotlin.math.max
import kotlin.math.sign

/**
 * @author Kris | 05/12/2021
 */
@Suppress("NOTHING_TO_INLINE")
object Day5 : Puzzle<HydrothermalVenture, Int>(5) {
    private val REGEX = Regex("""(\d+),(\d+) -> (\d+),(\d+)""")

    override fun Sequence<String>.parse(): HydrothermalVenture {
        val lines = toList().getLines()
        val dimension = lines.maxOf(Line::highestCoord)
        return HydrothermalVenture(lines, dimension + 1)
    }

    private fun List<String>.getLines() = map {
        val (x1, y1, x2, y2) = it.getLinePoints()
        Line(Point(x1, y1), Point(x2, y2))
    }

    private inline fun String.getLinePoints(): List<Int> = requireNotNull(REGEX.find(this)).destructured.toList().map(String::toInt)

    private inline fun HydrothermalVenture.getCountOfOverlappingPoints(filter: LineFilter = { true }): Int {
        val oceanFloor = Floor(size)
        val markedFloor = Floor(size)
        traverse(dimension, filter) { index -> if (oceanFloor.get(index)) markedFloor.set(index) else oceanFloor.set(index) }
        return markedFloor.cardinality()
    }

    private inline fun HydrothermalVenture.traverse(dimension: Int, filter: LineFilter, consumer: PointConsumer) =
        lines.forEachFiltered(filter) { line -> line.forEachCoveredPoint(dimension) { consumer(it) } }

    override fun HydrothermalVenture.solvePartOne(): Int = getCountOfOverlappingPoints(Line::straight)
    override fun HydrothermalVenture.solvePartTwo(): Int = getCountOfOverlappingPoints()
}

private typealias PointConsumer = (Int) -> Unit
private typealias LineFilter = (Line) -> Boolean
private typealias Lines = List<Line>
private typealias Floor = BitSet

data class HydrothermalVenture(val lines: Lines, val dimension: Int) {
    val size: Int get() = dimension * dimension + dimension
}

data class Line(val startPoint: Point, val endPoint: Point) {
    val straight get() = startPoint.x == endPoint.x || startPoint.y == endPoint.y
    val xSignum = (endPoint.x - startPoint.x).sign
    val ySignum = (endPoint.y - startPoint.y).sign
    val highestCoord get() = max(startPoint.x, max(startPoint.y, max(endPoint.x, endPoint.y)))

    inline fun forEachCoveredPoint(dimension: Int, consumer: PointConsumer) {
        var (x, y) = startPoint
        consumer(x * dimension + y)
        while (x != endPoint.x || y != endPoint.y) {
            x += xSignum
            y += ySignum
            consumer(x * dimension + y)
        }
    }
}
