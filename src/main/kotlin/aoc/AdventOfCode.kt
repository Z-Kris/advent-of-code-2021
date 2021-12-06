package aoc

import aoc.day1.Day1
import aoc.day2.Day2
import aoc.day3.Day3
import aoc.day4.Day4
import aoc.day5.Day5
import aoc.day6.Day6
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

/**
 * @author Kris | 05/12/2021
 */
private val puzzles = listOf(Day1, Day2, Day3, Day4, Day5, Day6)

@ExperimentalTime
private fun main() = puzzles.forEach { solve(it) }

@ExperimentalTime
private fun <T> solve(puzzle: Puzzle<T>) = with(puzzle) {
    val input = parse()
    val solutionPart1 = measureTimedValue {
        input.solvePartOne()
    }
    println("Day ${puzzle.day} Part 1: ${solutionPart1.value} (${solutionPart1.duration})")

    val solutionPart2 = measureTimedValue {
        input.solvePartTwo()
    }
    println("Day ${puzzle.day} Part 2: ${solutionPart2.value} (${solutionPart2.duration})")
}
