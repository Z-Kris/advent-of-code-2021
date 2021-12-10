package aoc.day10

import aoc.Puzzle

/**
 * @author Kris | 10/12/2021
 */
object Day10 : Puzzle<List<String>, Long>(10) {
    private val illegalChunks = Regex("(?:\\(([}\\]>])|\\[([})>])|\\{([])>])|<([}\\])]))+")
    private val validChunks = Regex("(?:\\[]|\\(\\)|<>|\\{})+")
    override fun Sequence<String>.parse(): List<String> = toList()

    private tailrec fun String.replaceInvalidChunks(): String {
        with(replace(validChunks, "")) { return if (this == this@replaceInvalidChunks) this else this.replaceInvalidChunks() }
    }

    private fun String.computePartTwoPointsSum(): Long = reversed().map { it.chunk.secondPoints }.reduce { acc, l -> acc * 5 + l }
    private fun List<String>.mapInvalidChunks() = map { it.replaceInvalidChunks() }
    private fun List<String>.mapIncompleteChunks() = mapNotNull { if (illegalChunks.find(it) == null) it else null }
    private val Char.chunk get() = Chunk.values().single { it.openingChar == this || it.closingChar == this }

    override fun List<String>.solvePartOne(): Long =
        mapInvalidChunks().mapNotNull { string -> illegalChunks.find(string)?.groupValues?.first()?.last() }.sumOf { it.chunk.firstPoints }

    override fun List<String>.solvePartTwo(): Long =
        with(mapInvalidChunks().mapIncompleteChunks().map { it.computePartTwoPointsSum() }.sorted()) { this[size / 2] }
}

private enum class Chunk(
    val openingChar: Char,
    val closingChar: Char,
    val firstPoints: Long,
    val secondPoints: Long
) {
    Round('(', ')', 3, 1),
    Square('[', ']', 57, 2),
    Curly('{', '}', 1_197, 3),
    Angle('<', '>', 25_137, 4)
}
