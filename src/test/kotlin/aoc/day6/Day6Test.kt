package aoc.day6

import aoc.day6.Day6.solvePartOne
import aoc.day6.Day6.solvePartTwo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 06/12/2021
 */
internal class Day6Test {

    private val testInput = "3,4,3,1,2"

    @Test
    fun partOne() {
        assertEquals(5_934, Day6.parse(testInput).solvePartOne())
        assertEquals(387_413, Day6.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(26_984_457_539L, Day6.parse(testInput).solvePartTwo())
        assertEquals(1_738_377_086_345L, Day6.parse().solvePartTwo())
    }
}
