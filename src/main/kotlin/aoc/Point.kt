package aoc

/**
 * @author Kris | 05/12/2021
 */
data class Point(val x: Int, val y: Int) {
    fun merge(other: Point) = Point(x + other.x, y + other.y)
}
