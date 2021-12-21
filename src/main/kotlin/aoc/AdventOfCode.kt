@file:Suppress("ConvertLambdaToReference")

package aoc

import aoc.puzzles.Puzzle
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

/**
 * @author Kris | 05/12/2021
 */
private val puzzles = sealedClassObjectInstances<Puzzle<*, *>>()
private val List<Puzzle<*, *>>.lastPuzzle get() = maxByOrNull { it.day } ?: error("No puzzles found.")

@ExperimentalTime
private fun main() = puzzles.lastPuzzle.solve()

@ExperimentalTime
private fun <T, R> Puzzle<T, R>.solve() {
    val parseMeasurement = measureTimedValue { parse() }
    val input = parseMeasurement.value
    println("Day $day parsing: ${parseMeasurement.duration}")
    println()

    val solutionPart1 = measureTimedValue {
        input.solvePartOne()
    }
    printSolution(solutionPart1, 1)

    val solutionPart2 = measureTimedValue {
        input.solvePartTwo()
    }
    printSolution(solutionPart2, 2)
}

@OptIn(ExperimentalTime::class)
private fun <T, R> Puzzle<T, R>.printSolution(value: TimedValue<R>, part: Int) {
    println("Day $day Part $part")
    println("Duration: ${value.duration}")
    if ('\n' in value.value.toString()) {
        println("Solution:")
        println(value.value)
    } else {
        println("Solution: ${value.value}")
    }
    println()
}
