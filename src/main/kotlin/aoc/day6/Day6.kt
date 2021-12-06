package aoc.day6

import aoc.Puzzle

/**
 * @author Kris | 06/12/2021
 */
object Day6 : Puzzle<List<Int>>(6) {
    private const val LAST_STATE = 8
    private const val INTERVAL = 6
    override fun Sequence<String>.parse(): List<Int> = single().split(',').map(String::toInt)

    override fun List<Int>.solvePartOne() = calculateProgression(80)
    override fun List<Int>.solvePartTwo() = calculateProgression(256)

    private fun List<Int>.calculateProgression(days: Int): Long = LongArray(LAST_STATE + 1) { count(it) }.progress(days).sum()
    private fun List<Int>.count(value: Int) = this@count.count { it == value }.toLong()
    private fun LongArray.progress(days: Int): LongArray = apply { repeat(days) { advanceDay() } }
    private fun LongArray.rotate() = System.arraycopy(this, 1, this, 0, LAST_STATE)

    private fun LongArray.advanceDay() = first().let { value ->
        rotate()
        this[LAST_STATE] = value
        this[INTERVAL] += value
    }
}
