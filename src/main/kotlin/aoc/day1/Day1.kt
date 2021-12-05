package aoc.day1

import aoc.Puzzle

/**
 * @author Kris | 05/12/2021
 */
object Day1 : Puzzle<List<Int>>(1) {
    override fun parse(input: Sequence<String>): List<Int> = input.map(String::toInt).toList()
    override fun solvePartOne(input: List<Int>) = input.zipWithNext().count { (cur, next) -> next > cur }
    override fun solvePartTwo(input: List<Int>) = solvePartOne(input.windowed(size = 3).map(List<Int>::sum))
}
