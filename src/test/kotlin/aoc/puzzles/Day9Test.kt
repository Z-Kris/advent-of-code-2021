package aoc.puzzles

import aoc.puzzles.Day9.solvePartOne
import aoc.puzzles.Day9.solvePartTwo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 09/12/2021
 */
internal class Day9Test {
    private val testInput = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(15, Day9.parse(testInput).solvePartOne())
        assertEquals(475, Day9.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(1_134, Day9.parse(testInput).solvePartTwo())
        assertEquals(1_092_012, Day9.parse().solvePartTwo())
    }
}
