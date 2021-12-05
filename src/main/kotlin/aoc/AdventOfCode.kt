package aoc

import aoc.day1.Day1
import aoc.day2.Day2
import aoc.day3.Day3
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

/**
 * @author Kris | 05/12/2021
 */
private val puzzles = listOf<Puzzle<*>>(Day1, Day2, Day3)

@ExperimentalTime
private fun main() = puzzles.forEach { solve(it) }

@ExperimentalTime
private fun <T> solve(puzzle: Puzzle<T>) {
    val input = puzzle.parse()

    val solutionPart1 = measureTimedValue {
        puzzle.solvePartOne(input)
    }
    println("Day ${puzzle.day} Part 1: ${solutionPart1.value} (${solutionPart1.duration})")

    val solutionPart2 = measureTimedValue {
        puzzle.solvePartTwo(input)
    }
    println("Day ${puzzle.day} Part 2: ${solutionPart2.value} (${solutionPart2.duration})")
}
