package aoc.day6

import aoc.Puzzle

/**
 * @author Kris | 06/12/2021
 */
@Suppress("NOTHING_TO_INLINE")
object Day6 : Puzzle<LongArray>(6) {
    private const val LAST_STATE = 8
    private const val INTERVAL = 6
    override fun Sequence<String>.parse(): LongArray = single().split(',').map(String::toInt).let { longList ->
        longList.toSet().associateWith { key -> longList.count { it == key }.toLong() }
    }.let { LongArray(LAST_STATE + 1) { index -> it.getOrDefault(index, 0) } }

    override fun LongArray.solvePartOne() = calculateProgression(80)
    override fun LongArray.solvePartTwo() = calculateProgression(256)

    private inline fun LongArray.calculateProgression(days: Int): Long = progress(days).sum()
    private inline fun LongArray.progress(days: Int): LongArray = apply { repeat(days) { advanceDay() } }
    private inline fun LongArray.rotate() = System.arraycopy(this, 1, this, 0, LAST_STATE)

    private inline fun LongArray.advanceDay() = first().let { value ->
        rotate()
        this[LAST_STATE] = value
        this[INTERVAL] += value
    }
}
