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
        val positions = buildMap {
            this@parse.forEach { line ->
                val match = STARTING_POSITION_REGEX.matchEntire(line) ?: error("Corrupted line: $line")
                val (id, pos) = match.groupValues?.drop(1).map(String::toInt)
                require(put(id, pos) == null) { "Overlapping player" }
            }
        }
        return positions.toSortedMap().values
    }

    private fun DiceStartingPositions.toDiracDiceGame(): DiracDiceGame {
        val (playerA, playerB) = map(::Player)
        return playerA play playerB
    }

    override fun DiceStartingPositions.solvePartOne(): Long {
        val die = Die()
        val loser = toDiracDiceGame().findLoser(die)
        return loser.score.toLong() * die.rollCount
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
        val (firstWins, secondWins) = toDiracDiceGame().getScore()
        return max(firstWins, secondWins)
    }

    private fun DiracDiceGame.getScore(cache: DiceGameCache = mutableMapOf()): Score = cache.getOrPut(this) { roll(cache) }

    private fun DiracDiceGame.roll(cache: DiceGameCache): Score = QUANTUM_ROLL_POSSIBILITIES.fold(INITIAL_SCORE) { acc, roll ->
        fold(acc, roll, cache)
    }

    private fun DiracDiceGame.fold(acc: Score, roll: Int, cache: DiceGameCache): Score = firstPlayer.advance(roll).let { firstPlayer ->
        if (firstPlayer.score >= QUANTUM_GAME_WIN_THRESHOLD) acc.incrementFirst()
        else acc plus secondPlayer.play(firstPlayer).getScore(cache).inv()
    }

    private fun Score.inv() = second to first
    private fun Score.incrementFirst() = first.inc() to second
    private infix operator fun Score.plus(other: Score) = first.plus(other.first) to second.plus(other.second)
    private infix fun Player.play(other: Player) = DiracDiceGame(this, other)
}

private typealias Score = Pair<Long, Long>
private typealias DiceGameCache = MutableMap<DiracDiceGame, Score>
private typealias DiceStartingPositions = Collection<Int>

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
