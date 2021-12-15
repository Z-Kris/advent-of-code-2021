package aoc.day15

import aoc.day15.Day15.solvePartOne
import aoc.day15.Day15.solvePartTwo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Kris | 15/12/2021
 */
internal class Day15Test {
    private val testInput = """
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(40, Day15.parse(testInput).solvePartOne())
        assertEquals(602, Day15.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(315, Day15.parse(testInput).solvePartTwo())
        assertEquals(2935, Day15.parse().solvePartTwo())
    }
}
