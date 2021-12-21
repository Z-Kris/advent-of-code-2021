package aoc.puzzles

import aoc.puzzles.Day20.solvePartOne
import aoc.puzzles.Day20.solvePartTwo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Kris | 20/12/2021
 */
internal class Day20Test {
    private val enhancementInput = "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##" +
        "#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###" +
        ".######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#." +
        ".#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#....." +
        ".#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.." +
        "...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#....." +
        "..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#"

    private val testInput = """
        $enhancementInput

        #..#.
        #....
        ##..#
        ..#..
        ..###
    """.trimIndent()

    @Test
    fun partOne() {
        assertEquals(35, Day20.parse(testInput).solvePartOne())
        assertEquals(5291, Day20.parse().solvePartOne())
    }

    @Test
    fun partTwo() {
        assertEquals(3351, Day20.parse(testInput).solvePartTwo())
        assertEquals(16665, Day20.parse().solvePartTwo())
    }
}
