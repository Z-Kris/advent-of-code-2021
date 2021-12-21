package aoc.puzzles

import aoc.puzzles.Day12.solvePartOne
import aoc.puzzles.Day12.solvePartTwo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 12/12/2021
 */
internal class Day12Test {
    private val testInput1 = """
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent()

    private val testInput2 = """
        dc-end
        HN-start
        start-kj
        dc-start
        dc-HN
        LN-dc
        HN-end
        kj-sa
        kj-HN
        kj-dc
    """.trimIndent()

    private val testInput3 = """
        fs-end
        he-DX
        fs-he
        start-DX
        pj-DX
        end-zg
        zg-sl
        zg-pj
        pj-he
        RW-he
        fs-DX
        pj-RW
        zg-RW
        start-pj
        he-WI
        zg-he
        pj-fs
        start-RW
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(10, Day12.parse(testInput1).solvePartOne())
        assertEquals(19, Day12.parse(testInput2).solvePartOne())
        assertEquals(226, Day12.parse(testInput3).solvePartOne())
        assertEquals(5_252, Day12.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(36, Day12.parse(testInput1).solvePartTwo())
        assertEquals(103, Day12.parse(testInput2).solvePartTwo())
        assertEquals(3509, Day12.parse(testInput3).solvePartTwo())
        assertEquals(147_784, Day12.parse().solvePartTwo())
    }
}
