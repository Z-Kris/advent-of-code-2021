package aoc.day6

import aoc.Puzzle

/**
 * @author Kris | 06/12/2021
 */
object Day6 : Puzzle<List<Int>>(6) {
    private const val STATES = 9
    private const val LAST_STATE = STATES - 1
    private const val INTERVAL = 6
    override fun Sequence<String>.parse(): List<Int> = single().split(',').map(String::toInt)

    override fun List<Int>.solvePartOne() = calculateProgression(80)
    override fun List<Int>.solvePartTwo() = calculateProgression(256)

    private fun List<Int>.calculateProgression(days: Int): Long = LongArray(STATES) { index -> count { it == index }.toLong() }.progress(days).sum()
    private fun LongArray.progress(days: Int): LongArray = apply { repeat(days) { advanceDay() } }

    private fun LongArray.advanceDay() {
        val newborns = first()
        repeat(INTERVAL) { this[it] = this[it + 1] }
        this[INTERVAL] = this[INTERVAL + 1] + newborns
        for (index in (INTERVAL + 1)..LAST_STATE) this[index] = if (index == LAST_STATE) newborns else this[LAST_STATE]
    }
}
