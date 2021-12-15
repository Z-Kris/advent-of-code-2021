package aoc

/**
 * @author Kris | 05/12/2021
 */
data class Point(val x: Int, val y: Int) {
    constructor(x: String, y: String) : this(x.toInt(), y.toInt())
    constructor(x: Int, y: String) : this(x, y.toInt())
    constructor(x: String, y: Int) : this(x.toInt(), y)
    fun merge(other: Point) = Point(x + other.x, y + other.y)
    fun inBounds(width: Int, height: Int) = x in 0 until width && y in 0 until height
    fun index(width: Int) = x * width + y
    companion object {
        val ZERO = Point(0, 0)
    }
}
