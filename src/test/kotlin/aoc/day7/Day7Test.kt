package aoc.day7

import aoc.day7.Day7.solvePartOne
import aoc.day7.Day7.solvePartTwo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 07/12/2021
 */
internal class Day7Test {
    private val testInput = "16,1,2,0,4,2,7,1,2,14"

    @Test
    fun partOne() {
        assertEquals(37, Day7.parse(testInput).solvePartOne())
        assertEquals(347_509, Day7.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(168, Day7.parse(testInput).solvePartTwo())
        assertEquals(98_257_206, Day7.parse().solvePartTwo())
    }
}
