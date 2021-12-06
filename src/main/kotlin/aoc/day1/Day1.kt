package aoc.day1

import aoc.Puzzle

/**
 * @author Kris | 05/12/2021
 */
object Day1 : Puzzle<List<Int>>(1) {
    override fun Sequence<String>.parse(): List<Int> = map(String::toInt).toList()
    override fun List<Int>.solvePartOne() = zipWithNext().count { (cur, next) -> next > cur }
    override fun List<Int>.solvePartTwo() = windowed(size = 3).map(List<Int>::sum).solvePartOne()
}
