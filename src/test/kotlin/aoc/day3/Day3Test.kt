package aoc.day3

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 05/12/2021
 */
internal class Day3Test {

    private val testInput = listOf(
        0b00100,
        0b11110,
        0b10110,
        0b10111,
        0b10101,
        0b01111,
        0b00111,
        0b11100,
        0b10000,
        0b11001,
        0b00010,
        0b01010
    )

    @Test
    fun partOne() {
        assertEquals(198L, Day3.solvePartOne(testInput))
    }

    @Test
    fun partTwo() {
        assertEquals(230L, Day3.solvePartTwo(testInput))
    }
}
