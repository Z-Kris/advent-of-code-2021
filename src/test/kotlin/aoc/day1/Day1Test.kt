package aoc.day1

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 05/12/2021
 */
internal class Day1Test {
    private val testInput = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)

    @Test
    fun partOne() {
        assertEquals(7, Day1.solvePartOne(testInput))
        assertEquals(1139, Day1.solvePartOne(Day1.parse()))
    }

    @Test
    fun partTwo() {
        assertEquals(5, Day1.solvePartTwo(testInput))
        assertEquals(1103, Day1.solvePartTwo(Day1.parse()))
    }
}
