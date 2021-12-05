package aoc.day5

import aoc.Puzzle
import kotlin.math.sign

/**
 * @author Kris | 05/12/2021
 */
object Day5 : Puzzle<List<Line>>(5) {
    override fun parse(input: Sequence<String>): List<Line> = input.map { line ->
        val (pointA, pointB) = line.split(" -> ").map { point ->
            val (x, y) = point.split(',')
            Point(x.toInt(), y.toInt())
        }
        Line(pointA, pointB)
    }.toList()

    private fun <T> MutableMap<T, Int>.increment(key: T) = merge(key, 1, Int::plus)

    private fun List<Line>.getCountOfOverlappingPoints(threshold: Int = 2): Int {
        val pointMap = mutableMapOf<Point, Int>()
        forEach { line -> line.getCoveredPoints().forEach { pointMap.increment(it) } }
        return pointMap.count { it.value >= threshold }
    }

    override fun solvePartOne(input: List<Line>): Int =
        input.filter { it.startPoint.x == it.endPoint.x || it.startPoint.y == it.endPoint.y }.getCountOfOverlappingPoints()

    override fun solvePartTwo(input: List<Line>): Int = input.getCountOfOverlappingPoints()
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
