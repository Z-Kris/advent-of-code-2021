package aoc.puzzles

import aoc.chainRepeat
import aoc.getOrZero

/**
 * @author Kris | 06/12/2021
 */
@Suppress("NOTHING_TO_INLINE")
object Day6 : Puzzle<LanternfishSchool, Long>(6) {
    private const val NEWBORN_AGE = 8
    private const val RESET_AGE = 6
    private fun List<LanternfishAge>.toAgeCounts() = toSet().associateWith(this::countOf).toArray()
    private fun LanternfishCountMap.toArray() = LanternfishSchool(NEWBORN_AGE + 1, ::getOrZero)
    override fun Sequence<String>.parse() = single().split(',').map(String::toInt).toAgeCounts()

    override fun LanternfishSchool.solvePartOne() = calculateProgression(80)
    override fun LanternfishSchool.solvePartTwo() = calculateProgression(256)

    private inline fun LanternfishSchool.calculateProgression(days: Int) = advance(days).sum()
    private inline fun LanternfishSchool.rotateLeft() = System.arraycopy(this, 1, this, 0, NEWBORN_AGE)

    private inline fun LanternfishSchool.advance(days: Int) = chainRepeat(days) {
        val newborns = first()
        rotateLeft()
        this[NEWBORN_AGE] = newborns
        this[RESET_AGE] += newborns
    }
}
private fun List<LanternfishAge>.countOf(value: Int) = count { it == value }.toLong()
private typealias LanternfishSchool = LongArray
private typealias LanternfishCountMap = Map<Int, Long>
private typealias LanternfishAge = Int
