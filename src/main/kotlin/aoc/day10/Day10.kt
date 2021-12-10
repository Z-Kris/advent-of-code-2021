package aoc.day10

import aoc.Puzzle
import aoc.minus

/**
 * @author Kris | 10/12/2021
 */
object Day10 : Puzzle<List<String>, Long>(10) {
    private val illegalChunks = Regex("(?:\\(([}\\]>])|\\[([})>])|\\{([])>])|<([}\\])]))+")
    private val validChunks = Regex("(?:\\[]|\\(\\)|<>|\\{})+")
    override fun Sequence<String>.parse(): List<String> = toList()

    private tailrec fun String.replaceInvalidChunks(): String {
        val replaced = this - validChunks
        return if (replaced.length == length) this else replaced.replaceInvalidChunks()
    }

    private fun String.computePartTwoPointsSum(): Long = reversed().map { it.chunk.secondPoints }.reduce(Long::computePartTwo)
    private fun List<String>.mapInvalidChunks() = map { it.replaceInvalidChunks() }
    private fun List<String>.mapIncompleteChunks() = mapInvalidChunks().mapNotNull { if (illegalChunks.find(it) == null) it else null }
    private fun List<String>.mapCorruptedChunks() = mapInvalidChunks().mapNotNull { string -> illegalChunks.find(string)?.groupValues?.first()?.last() }
    private val Char.chunk get() = Chunk.values().single { this in it.brackets }

    override fun List<String>.solvePartOne(): Long = mapCorruptedChunks().sumOf { it.chunk.firstPoints }
    override fun List<String>.solvePartTwo(): Long = mapIncompleteChunks().map { it.computePartTwoPointsSum() }.sorted().let { it[it.size / 2] }
}

private fun Long.computePartTwo(value: Long) = this * 5 + value

private enum class Chunk(
    val brackets: String,
    val firstPoints: Long,
    val secondPoints: Long
) {
    Round("()", 3, 1),
    Square("[]", 57, 2),
    Curly("{}", 1_197, 3),
    Angle("<>", 25_137, 4)
}
