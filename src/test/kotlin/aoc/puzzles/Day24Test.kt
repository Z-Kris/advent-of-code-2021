package aoc.puzzles

import aoc.puzzles.Day24.solvePartOne
import aoc.puzzles.Day24.solvePartTwo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Kris | 24/12/2021
 */
internal class Day24Test {
    @Test
    fun partOne() {
        assertEquals(97_919_997_299_495, Day24.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(51_619_131_181_131, Day24.parse().solvePartTwo())
    }
}
