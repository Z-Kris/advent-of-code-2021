@file:Suppress("NOTHING_TO_INLINE")

package aoc.puzzles

/**
 * @author Kris | 05/12/2021
 */
object Day1 : Puzzle<List<Int>, Int>(1) {
    override fun List<String>.parse(): List<Int> = map(String::toInt)
    override fun List<Int>.solvePartOne() = countIncrements(1)
    override fun List<Int>.solvePartTwo() = countIncrements(3)
    private inline fun <T : Comparable<T>> List<T>.countIncrements(offset: Int) = (0 until (size - offset)).count { get(it + offset) > get(it) }
}
