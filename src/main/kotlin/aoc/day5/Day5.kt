package aoc.day5

import aoc.Puzzle
import kotlin.math.sign

/**
 * @author Kris | 05/12/2021
 */
object Day5 : Puzzle<List<Line>>(5) {
    private val regex = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)")
    override fun Sequence<String>.parse(): List<Line> = map {
        val (x1, y1, x2, y2) = regex.find(it)?.destructured?.toList()?.map(String::toInt) ?: error("Invalid input")
        Line(Point(x1, y1), Point(x2, y2))
    }.toList()

    private fun <T> MutableMap<T, Int>.increment(key: T) = merge(key, 1, Int::plus)

    private fun List<Line>.getCountOfOverlappingPoints(threshold: Int = 2): Int = with(mutableMapOf<Point, Int>()) {
        this@getCountOfOverlappingPoints.forEach { line -> line.getCoveredPoints().forEach { increment(it) } }
        count { it.value >= threshold }
    }

    override fun List<Line>.solvePartOne(): Int =
        filter { it.startPoint.x == it.endPoint.x || it.startPoint.y == it.endPoint.y }.getCountOfOverlappingPoints()

    override fun List<Line>.solvePartTwo(): Int = getCountOfOverlappingPoints()
}

data class Line(val startPoint: Point, val endPoint: Point) {
    fun getCoveredPoints(): List<Point> {
        val xSigNum = (endPoint.x - startPoint.x).sign
        val ySigNum = (endPoint.y - startPoint.y).sign
        val points = mutableListOf<Point>()
        var (x, y) = startPoint
        while (x != endPoint.x || y != endPoint.y) {
            points += Point(x, y)
            x += xSigNum
            y += ySigNum
        }
        points += endPoint
        return points
    }
}

data class Point(val x: Int, val y: Int)
