package aoc.puzzles

/**
 * @author Kris | 05/12/2021
 */
sealed class Puzzle<T, R>(val day: Int) {
    fun parse(): T = Puzzle::class.java.getResourceAsStream("day $day.txt").use { inputStream ->
        requireNotNull(inputStream) { "File not found." }
        inputStream.bufferedReader().readLines().parse()
    }
    fun parse(input: String): T = input.split('\n').parse()
    abstract fun List<String>.parse(): T
    abstract fun T.solvePartOne(): R
    abstract fun T.solvePartTwo(): R
}
