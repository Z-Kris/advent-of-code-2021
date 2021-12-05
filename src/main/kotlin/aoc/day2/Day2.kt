package aoc.day2

import aoc.Puzzle

/**
 * @author Kris | 05/12/2021
 */
object Day2 : Puzzle<List<String>>(2) {
    private val UP = Instruction("up")
    private val DOWN = Instruction("down")
    private val FORWARD = Instruction("forward")
    override fun parse(input: Sequence<String>) = input.toList()

    override fun solvePartOne(input: List<String>): Long {
        val forwardSum = input.sumOf(Regex("$FORWARD (\\d+)"))
        val upSum = input.sumOf(Regex("$UP (\\d+)"))
        val downSum = input.sumOf(Regex("$DOWN (\\d+)"))
        return forwardSum.toLong() * (downSum - upSum)
    }

    override fun solvePartTwo(input: List<String>): Long = with(Submarine()) {
        input.toInstructions().forEach { (instruction, count) ->
            when (instruction) {
                UP -> aim -= count
                DOWN -> aim += count
                FORWARD -> {
                    horizontalPosition += count
                    depth += aim * count
                }
            }
        }
        return horizontalPosition * depth
    }

    private fun List<String>.toInstructions(): List<Pair<Instruction, Int>> = map {
        val (instruction, count) = it.split(' ')
        Instruction(instruction) to count.toInt()
    }

    private fun List<String>.sumOf(regex: Regex): Int = this@sumOf.sumOf { regex.find(it)?.groupValues?.get(1)?.toInt() ?: 0 }
}

private data class Submarine(
    var aim: Long = 0,
    var horizontalPosition: Long = 0,
    var depth: Long = 0
)

@JvmInline
private value class Instruction(private val instruction: String) {
    override fun toString() = instruction
}
