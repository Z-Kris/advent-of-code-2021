package aoc.puzzles

import aoc.puzzles.Day17.solvePartOne
import aoc.puzzles.Day17.solvePartTwo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Kris | 17/12/2021
 */
internal class Day17Test {
    private val testInput = "target area: x=20..30, y=-10..-5"

    @Test
    fun partOne() {
        assertEquals(45, Day17.parse(testInput).solvePartOne())
        assertEquals(17_766, Day17.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(112, Day17.parse(testInput).solvePartTwo())
        assertEquals(1_733, Day17.parse().solvePartTwo())
    }
}
