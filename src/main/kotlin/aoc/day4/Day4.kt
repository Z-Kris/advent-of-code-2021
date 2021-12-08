package aoc.day4

import aoc.Puzzle

/**
 * @author Kris | 05/12/2021
 */
object Day4 : Puzzle<Bingo, Long>(4) {
    private const val DIMENSION = 5
    private val range = 0 until DIMENSION
    private fun List<List<String>>.getBingoBoards() = map { it.convertToIntList() }.map(::BingoBoard)
    private fun List<String>.convertToIntList() = flatMap(String::lines).joinToString(separator = " ").trim().split(Regex("\\s+")).map(String::toInt)

    override fun Sequence<String>.parse(): Bingo = with(toMutableList().apply { removeIf(String::isEmpty) }) {
        val winningNumbers = removeFirst().split(',').map(String::toInt)
        val bingoBoards = chunked(DIMENSION).getBingoBoards()
        Bingo(winningNumbers.withIndex().associate { it.value to winningNumbers.subList(0, it.index + 1).toSet() }, bingoBoards)
    }

    override fun Bingo.solvePartOne(): Long = getWinningBoards().first().getScore()
    override fun Bingo.solvePartTwo(): Long = getWinningBoards().last().getScore()

    private fun Bingo.getWinningBoards(): List<WinningBingoDraw> {
        val remainingBoards = boards.toMutableList()
        val winningDraws = mutableListOf<WinningBingoDraw>()
        for ((drawnNumber, drawnNumbers) in draws) {
            remainingBoards.filter { it.hasWon(drawnNumber, drawnNumbers) }.forEach { board ->
                remainingBoards -= board
                winningDraws += WinningBingoDraw(drawnNumber, board, drawnNumbers)
            }
        }
        require(winningDraws.isNotEmpty())
        return winningDraws
    }

    private fun WinningBingoDraw.getScore(): Long {
        val sumOfUnmarkedNumbers = board.sumOf { if (it !in drawnNumbers) it else 0 }.toLong()
        return sumOfUnmarkedNumbers * winningNumber
    }

    private fun BingoBoard.hasWon(drawnNumber: Int, drawnNumbers: Set<Int>): Boolean =
        drawnNumber in board && range.any { index -> allInRow(index, drawnNumbers) || allInColumn(index, drawnNumbers) }

    private fun BingoBoard.allInRow(row: Int, numbers: Set<Int>) = range.all { col -> this[row, col] in numbers }
    private fun BingoBoard.allInColumn(col: Int, numbers: Set<Int>) = range.all { row -> this[row, col] in numbers }
    private operator fun BingoBoard.get(row: Int, column: Int): Int = get((row * DIMENSION) + column)
}

data class Bingo(val draws: Map<Int, Set<Int>>, val boards: List<BingoBoard>)
data class BingoBoard(val board: List<Int>) : List<Int> by board
data class WinningBingoDraw(val winningNumber: Int, val board: BingoBoard, val drawnNumbers: Set<Int>)
