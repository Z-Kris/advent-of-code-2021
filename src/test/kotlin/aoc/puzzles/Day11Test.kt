package aoc.puzzles

import aoc.puzzles.Day11.solvePartOne
import aoc.puzzles.Day11.solvePartTwo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 11/12/2021
 */
internal class Day11Test {
    private val testInput = """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(1_656, Day11.parse(testInput).solvePartOne())
        assertEquals(1_599, Day11.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(195, Day11.parse(testInput).solvePartTwo())
        assertEquals(418, Day11.parse().solvePartTwo())
    }
}
