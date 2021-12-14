package aoc.day14

import aoc.day14.Day14.solvePartOne
import aoc.day14.Day14.solvePartTwo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 14/12/2021
 */
internal class Day14Test {
    private val testInput = """
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(1_588, Day14.parse(testInput).solvePartOne())
        assertEquals(2_988, Day14.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(2_188_189_693_529, Day14.parse(testInput).solvePartTwo())
        assertEquals(3_572_761_917_024, Day14.parse().solvePartTwo())
    }
}
