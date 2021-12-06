package aoc.day1

import aoc.day1.Day1.solvePartOne
import aoc.day1.Day1.solvePartTwo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 05/12/2021
 */
internal class Day1Test {
    private val testInput = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)

    @Test
    fun partOne() {
        assertEquals(7, testInput.solvePartOne())
        assertEquals(1139, Day1.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(5, testInput.solvePartTwo())
        assertEquals(1103, Day1.parse().solvePartTwo())
    }
}
