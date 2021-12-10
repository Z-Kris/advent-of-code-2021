package aoc.day10

import aoc.day10.Day10.solvePartOne
import aoc.day10.Day10.solvePartTwo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Kris | 10/12/2021
 */
internal class Day10Test {
    private val testInput = """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(26_397, Day10.parse(testInput).solvePartOne())
        assertEquals(339_411, Day10.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(288_957, Day10.parse(testInput).solvePartTwo())
        assertEquals(2_289_754_624, Day10.parse().solvePartTwo())
    }
}
