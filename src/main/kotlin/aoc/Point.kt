package aoc

/**
 * @author Kris | 05/12/2021
 */
data class Point(val x: Int, val y: Int) {
    constructor(x: String, y: String) : this(x.toInt(), y.toInt())
    constructor(x: Int, y: String) : this(x, y.toInt())
    constructor(x: String, y: Int) : this(x.toInt(), y)
    fun merge(other: Point) = Point(x + other.x, y + other.y)
}
