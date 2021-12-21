@file:Suppress("UNNECESSARY_SAFE_CALL")

package aoc.puzzles

import kotlin.math.max

/**
 * @author Kris | 21/12/2021
 */
object Day21 : Puzzle<DiceStartingPositions, Long>(21) {
    private val STARTING_POSITION_REGEX = Regex("""Player (\d+) starting position: (\d+)""")
    private const val NORMAL_GAME_WIN_THRESHOLD = 1000
    private const val QUANTUM_GAME_WIN_THRESHOLD = 21
    private const val MAX_PLAYER_POSITION = 10
    private val INITIAL_SCORE = 0L to 0L
    private val QUANTUM_ROLLS = 1..3
    private val QUANTUM_ROLL_POSSIBILITIES =
        QUANTUM_ROLLS.flatMap { first -> QUANTUM_ROLLS.flatMap { second -> QUANTUM_ROLLS.map { third -> first + second + third } } }

    override fun List<String>.parse(): DiceStartingPositions {
        val positions = mutableMapOf<Int, Int>()
        for (line in this) {
            val match = STARTING_POSITION_REGEX.matchEntire(line) ?: error("Corrupted line: $line")
            val (id, pos) = match.groupValues?.drop(1).map(String::toInt)
            require(positions.put(id, pos) == null) { "Overlapping player" }
        }
        return positions.toSortedMap().values.toList()
    }

    private fun DiceStartingPositions.toPlayers(): List<Player> = map(::Player)

    override fun DiceStartingPositions.solvePartOne(): Long {
        val (first, second) = toPlayers()
        val die = Die()
        val loser = DiracDiceGame(first, second).findLoser(die)
        val rolls = die.rollCount
        return loser.score.toLong() * rolls
    }

    private tailrec fun DiracDiceGame.findLoser(die: Die): Player {
        val firstPlayer = firstPlayer.advance(die.rollsSum())
        if (firstPlayer.score >= NORMAL_GAME_WIN_THRESHOLD) return secondPlayer
        val secondPlayer = secondPlayer.advance(die.rollsSum())
        if (secondPlayer.score >= NORMAL_GAME_WIN_THRESHOLD) return firstPlayer
        val nextGame = DiracDiceGame(firstPlayer, secondPlayer)
        return nextGame.findLoser(die)
    }

    private fun Player.advance(roll: Int): Player {
        val position = (position + roll - 1) % MAX_PLAYER_POSITION + 1
        val score = score + position
        return Player(position, score)
    }

    override fun DiceStartingPositions.solvePartTwo(): Long {
        val (first, second) = toPlayers()
        val game = DiracDiceGame(first, second)
        val (firstWins, secondWins) = game.getScore()
        return max(firstWins, secondWins)
    }

    private fun DiracDiceGame.getScore(cache: DiceGameCache = mutableMapOf()): Score = cache.getOrPut(this) { roll(cache) }

    private fun DiracDiceGame.roll(cache: DiceGameCache): Score = QUANTUM_ROLL_POSSIBILITIES.fold(INITIAL_SCORE) { acc, roll ->
        val firstPlayer = firstPlayer.advance(roll)
        if (firstPlayer.score >= QUANTUM_GAME_WIN_THRESHOLD) return@fold acc.first.inc() to acc.second
        val nextGame = DiracDiceGame(secondPlayer, firstPlayer)
        val (first, second) = nextGame.getScore(cache)
        acc.first.plus(second) to acc.second.plus(first)
    }
}

private typealias Score = Pair<Long, Long>
private typealias DiceGameCache = MutableMap<DiracDiceGame, Score>
private typealias DiceStartingPositions = List<Int>

private data class DiracDiceGame(val firstPlayer: Player, val secondPlayer: Player)
private data class Player(val position: Int, val score: Int = 0)

private class Die {
    var rollCount: Int = 0
        private set

    fun rollsSum(rolls: Int = NORMAL_GAME_ROLLS): Int = generateSequence(::nextRoll).take(rolls).sum()
    private fun nextRoll(): Int = rollCount++ % ROLL_THRESHOLD + 1

    companion object {
        private const val NORMAL_GAME_ROLLS = 3
        private const val ROLL_THRESHOLD = 100
    }
}
