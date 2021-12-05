package aoc

import java.io.FileNotFoundException

/**
 * Base puzzle class, more or less a copy of Graham's puzzle as this is my first time doing AoC and I don't know what to expect from the exercises (:
 * @author Kris | 05/12/2021
 */
abstract class Puzzle<T>(val day: Int) {
    fun parse(): T = Puzzle::class.java.getResourceAsStream("day $day.txt").use { inputStream ->
        inputStream?.bufferedReader()?.useLines(this::parse) ?: throw FileNotFoundException()
    }
    fun parse(input: String): T = parse(input.splitToSequence('\n'))
    abstract fun parse(input: Sequence<String>): T
    abstract fun solvePartOne(input: T): Any
    abstract fun solvePartTwo(input: T): Any
}
