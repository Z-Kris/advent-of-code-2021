package aoc.day4

import aoc.Puzzle

/**
 * @author Kris | 05/12/2021
 */
object Day4 : Puzzle<Bingo>(4) {
    private const val DIMENSION = 5
    private val range = 0 until DIMENSION
    private fun List<List<String>>.getBingoBoards() = map { it.convertToIntList() }.map(::BingoBoard)
    private fun List<String>.convertToIntList() = flatMap(String::lines).joinToString(separator = " ").trim().split(Regex("\\s+")).map(String::toInt)

    override fun Sequence<String>.parse(): Bingo = with(toMutableList().apply { removeIf(String::isEmpty) }) {
        val winningNumbers = removeFirst().split(',').map(String::toInt)
        val bingoBoards = chunked(DIMENSION).getBingoBoards()
        Bingo(winningNumbers, bingoBoards)
    }

    override fun Bingo.solvePartOne(): Long = getWinningBoards().first().getScore()
    override fun Bingo.solvePartTwo(): Long = getWinningBoards().last().getScore()

    private fun Bingo.getWinningBoards(): List<WinningBingoDraw> {
        val remainingNumbers = winningNumbers.toMutableList()
        val remainingBoards = boards.toMutableList()
        val drawnNumbers = mutableSetOf<Int>()
        val winningDraws = mutableListOf<WinningBingoDraw>()
        while (remainingNumbers.isNotEmpty()) {
            val drawnNumber = remainingNumbers.removeFirst()
            drawnNumbers += drawnNumber
            val winningBoards = remainingBoards.filter { it.hasWon(drawnNumbers) }.toSet()
            winningBoards.forEach { board -> winningDraws += WinningBingoDraw(drawnNumber, board, drawnNumbers.toSet()) }
            remainingBoards -= winningBoards
        }
        require(winningDraws.isNotEmpty())
        return winningDraws
    }

    private fun WinningBingoDraw.getScore(): Long {
        val sumOfUnmarkedNumbers = board.sumOf { if (it !in drawnNumbers) it else 0 }.toLong()
        return sumOfUnmarkedNumbers * drawnNumber
    }

    private fun BingoBoard.hasWon(drawnNumbers: Set<Int>): Boolean =
        range.any { index -> drawnNumbers.containsAll(getRow(index)) || drawnNumbers.containsAll(getColumn(index)) }

    private fun BingoBoard.getRow(row: Int): List<Int> = range.map { col -> this[row, col] }
    private fun BingoBoard.getColumn(col: Int): List<Int> = range.map { row -> this[row, col] }
    private operator fun BingoBoard.get(row: Int, column: Int): Int = get((row * DIMENSION) + column)
}
data class Bingo(val winningNumbers: List<Int>, val boards: List<BingoBoard>)
data class BingoBoard(val board: List<Int>) : List<Int> by board
data class WinningBingoDraw(val drawnNumber: Int, val board: BingoBoard, val drawnNumbers: Set<Int>)
