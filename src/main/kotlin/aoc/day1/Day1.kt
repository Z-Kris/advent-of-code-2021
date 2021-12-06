@file:Suppress("NOTHING_TO_INLINE")

package aoc.day1

import aoc.Puzzle

/**
 * @author Kris | 05/12/2021
 */
object Day1 : Puzzle<List<Int>>(1) {
    override fun Sequence<String>.parse(): List<Int> = map(String::toInt).toList()
    override fun List<Int>.solvePartOne() = countIncrements(1)

    /**
     * Instead of taking the traditional approach of:
     * ```kt
     * windowed(size = 3).map(List<Int>::sum)
     * ```
     * We can reduce the number of computations necessary to solve the equation by checking **nth** element against **nth + offset**.
     * This is possible because **(a, b, c)** <= **(b, c, d)** has two common values, them being **b** & **c**. Since the three would be summed,
     * we can skip that part entirely and just check **a** against **d**.
     */
    override fun List<Int>.solvePartTwo() = countIncrements(3)
    private inline fun <T : Comparable<T>> List<T>.countIncrements(offset: Int) = (0 until (size - offset)).count { get(it + offset) > get(it) }
}
