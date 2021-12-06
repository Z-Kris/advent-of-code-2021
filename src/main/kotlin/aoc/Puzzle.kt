package aoc

import java.io.FileNotFoundException

/**
 * Base puzzle class, more or less a copy of Graham's puzzle as this is my first time doing AoC and I don't know what to expect from the exercises (:
 * @author Kris | 05/12/2021
 */
abstract class Puzzle<T, R>(val day: Int) {
    fun parse(): T = Puzzle::class.java.getResourceAsStream("day $day.txt").use { inputStream ->
        inputStream?.bufferedReader()?.useLines { it.parse() } ?: throw FileNotFoundException()
    }
    fun parse(input: String): T = input.splitToSequence('\n').parse()
    abstract fun Sequence<String>.parse(): T
    abstract fun T.solvePartOne(): R
    abstract fun T.solvePartTwo(): R
}
