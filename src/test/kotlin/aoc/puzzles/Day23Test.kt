package aoc.puzzles

import aoc.puzzles.Day23.solvePartOne
import aoc.puzzles.Day23.solvePartTwo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Kris | 23/12/2021
 */
internal class Day23Test {
    private val testInput = """
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(12_521, Day23.parse(testInput).solvePartOne())
        assertEquals(14_350, Day23.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(44_169, Day23.parse(testInput).solvePartTwo())
        assertEquals(49_742, Day23.parse().solvePartTwo())
    }
}
