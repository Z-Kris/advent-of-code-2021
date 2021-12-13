package aoc.day13

import aoc.day13.Day13.solvePartOne
import aoc.day13.Day13.solvePartTwo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 13/12/2021
 */
internal class Day13Test {
    private val testInput = """
        6,10
        0,14
        9,10
        0,3
        10,4
        4,11
        6,0
        6,12
        4,1
        0,13
        10,12
        3,4
        3,0
        8,4
        1,10
        2,14
        8,10
        9,0

        fold along y=7
        fold along x=5
    """.trimIndent()

    private val testResult = """
        █████
        █   █
        █   █
        █   █
        █████
    """.trimIndent()

    private val realResult = """
        ███  █    █  █   ██ ███  ███   ██   ██ 
        █  █ █    █ █     █ █  █ █  █ █  █ █  █
        ███  █    ██      █ █  █ ███  █  █ █   
        █  █ █    █ █     █ ███  █  █ ████ █ ██
        █  █ █    █ █  █  █ █ █  █  █ █  █ █  █
        ███  ████ █  █  ██  █  █ ███  █  █  ███
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(SingleFoldOrigami(17), Day13.parse(testInput).solvePartOne())
        assertEquals(SingleFoldOrigami(755), Day13.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        Day13.parse(testInput).solvePartTwo()
        assertEquals(FullyFoldedOrigami(testResult), Day13.parse(testInput).solvePartTwo())
        assertEquals(FullyFoldedOrigami(realResult), Day13.parse().solvePartTwo())
    }
}
