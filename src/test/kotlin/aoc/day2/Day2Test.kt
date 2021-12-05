package aoc.day2

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
        assertEquals(150, Day2.solvePartOne(testInput))
        assertEquals(2_036_120, Day2.solvePartOne(Day2.parse()))
    }

    @Test
    fun partTwo() {
        assertEquals(900, Day2.solvePartTwo(testInput))
        assertEquals(2_015_547_716, Day2.solvePartTwo(Day2.parse()))
    }
}
