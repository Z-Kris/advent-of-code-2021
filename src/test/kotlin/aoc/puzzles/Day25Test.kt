package aoc.puzzles

import aoc.puzzles.Day25.solvePartOne
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Kris | 25/12/2021
 */
internal class Day25Test {
    private val testInput = """
        v...>>.vv>
        .vv>>.vv..
        >>.>v>...v
        >>v>>.>.v.
        v>v.vv.v..
        >.>>..v...
        .vv..>.>v.
        v.v..>>v.v
        ....v..v.>
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(58, Day25.parse(testInput).solvePartOne())
        assertEquals(432, Day25.parse().solvePartOne())
    }
}
