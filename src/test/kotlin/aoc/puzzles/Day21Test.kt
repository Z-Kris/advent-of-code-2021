package aoc.puzzles

import aoc.puzzles.Day21.solvePartOne
import aoc.puzzles.Day21.solvePartTwo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Kris | 21/12/2021
 */
internal class Day21Test {
    private val testInput = """
        Player 1 starting position: 4
        Player 2 starting position: 8
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(739_785, Day21.parse(testInput).solvePartOne())
        assertEquals(855_624, Day21.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(444_356_092_776_315, Day21.parse(testInput).solvePartTwo())
        assertEquals(187_451_244_607_486, Day21.parse().solvePartTwo())
    }
}
