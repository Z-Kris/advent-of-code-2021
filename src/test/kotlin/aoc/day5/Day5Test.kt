package aoc.day5

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 05/12/2021
 */
internal class Day5Test {

    private val testInput = """
        0,9 -> 5,9
        8,0 -> 0,8
        9,4 -> 3,4
        2,2 -> 2,1
        7,0 -> 7,4
        6,4 -> 2,0
        0,9 -> 2,9
        3,4 -> 1,4
        0,0 -> 8,8
        5,5 -> 8,2
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(5, Day5.solvePartOne(Day5.parse(testInput)))
        assertEquals(6_267, Day5.solvePartOne(Day5.parse()))
    }

    @Test
    fun partTwo() {
        assertEquals(12, Day5.solvePartTwo(Day5.parse(testInput)))
        assertEquals(20_196, Day5.solvePartTwo(Day5.parse()))
    }
}
