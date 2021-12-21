package aoc.puzzles

import aoc.puzzles.Day2.solvePartOne
import aoc.puzzles.Day2.solvePartTwo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 05/12/2021
 */
internal class Day2Test {

    private val testInput = listOf(
        "forward 5",
        "down 5",
        "forward 8",
        "up 3",
        "down 8",
        "forward 2"
    )

    @Test
    fun partOne() {
        assertEquals(150, testInput.solvePartOne())
        assertEquals(2_036_120, Day2.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(900, testInput.solvePartTwo())
        assertEquals(2_015_547_716, Day2.parse().solvePartTwo())
    }
}
