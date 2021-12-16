@file:Suppress("ConvertLambdaToReference")

package aoc

import aoc.day1.Day1
import aoc.day10.Day10
import aoc.day11.Day11
import aoc.day12.Day12
import aoc.day13.Day13
import aoc.day14.Day14
import aoc.day15.Day15
import aoc.day16.Day16
import aoc.day2.Day2
import aoc.day3.Day3
import aoc.day4.Day4
import aoc.day5.Day5
import aoc.day6.Day6
import aoc.day7.Day7
import aoc.day8.Day8
import aoc.day9.Day9
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

/**
 * @author Kris | 05/12/2021
 */
private val puzzles = listOf(Day1, Day2, Day3, Day4, Day5, Day6, Day7, Day8, Day9, Day10, Day11, Day12, Day13, Day14, Day15, Day16)

@ExperimentalTime
private fun main() = puzzles.forEach { solve(it) }

@ExperimentalTime
private fun <T, R> solve(puzzle: Puzzle<T, R>) = with(puzzle) {
    val parseMeasurement = measureTimedValue { parse() }
    val input = parseMeasurement.value
    println("Day ${puzzle.day} parsing: ${parseMeasurement.duration}")
    println()

    val solutionPart1 = measureTimedValue {
        input.solvePartOne()
    }
    printSolution(puzzle, solutionPart1, 1)

    val solutionPart2 = measureTimedValue {
        input.solvePartTwo()
    }
    printSolution(puzzle, solutionPart2, 2)
}

@OptIn(ExperimentalTime::class)
private fun <T, R> printSolution(puzzle: Puzzle<T, R>, value: TimedValue<R>, part: Int) {
    println("Day ${puzzle.day} Part $part")
    println("Duration: ${value.duration}")
    if ('\n' in value.value.toString()) {
        println("Solution:")
        println(value.value)
    } else {
        println("Solution: ${value.value}")
    }
    println()
}
