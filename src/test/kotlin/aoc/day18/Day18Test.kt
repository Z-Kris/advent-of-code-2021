package aoc.day18

import aoc.day18.Day18.solvePartOne
import aoc.day18.Day18.solvePartTwo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Kris | 18/12/2021
 */
internal class Day18Test {
    private val testInput = """
        [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
        [[[5,[2,8]],4],[5,[[9,9],0]]]
        [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
        [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
        [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
        [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
        [[[[5,4],[7,7]],8],[[8,3],8]]
        [[9,3],[[9,9],[6,[4,9]]]]
        [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
        [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(4_140, Day18.parse(testInput).solvePartOne())
        assertEquals(4_176, Day18.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(3_993, Day18.parse(testInput).solvePartTwo())
        assertEquals(4_633, Day18.parse().solvePartTwo())
    }
}
