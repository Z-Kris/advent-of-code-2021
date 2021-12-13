package aoc.day13

import aoc.Point
import aoc.Puzzle
import kotlin.math.abs

/**
 * @author Kris | 13/12/2021
 */
object Day13 : Puzzle<InstructionManual, Origami>(13) {
    private const val MARKED_CHAR = '█'
    private const val UNMARKED_CHAR = ' '
    private val POINT_REGEX = Regex("(\\d+),(\\d+)")
    private val INSTRUCTION_REGEX = Regex("fold along ([xy])=(\\d++)")

    override fun Sequence<String>.parse(): InstructionManual {
        val points = mutableListOf<Point>()
        val instructions = mutableListOf<Point>()
        forEach {
            points.addPoint(it)
            instructions.addInstruction(it)
        }
        return InstructionManual(points, instructions)
    }

    private fun MutableList<Point>.addPoint(line: String) {
        val pointMatch = POINT_REGEX.find(line) ?: return
        val (x, y) = pointMatch.destructured
        this += Point(x, y)
    }

    private fun MutableList<Point>.addInstruction(line: String) {
        val instructionMatch = INSTRUCTION_REGEX.find(line) ?: return
        val (type, count) = instructionMatch.destructured
        this += when (type.single()) {
            'x' -> Point(count, 0)
            'y' -> Point(0, count)
            else -> error("Invalid coord")
        }
    }

    override fun InstructionManual.solvePartOne(): SingleFoldOrigami = SingleFoldOrigami(foldPoints(1).count())
    override fun InstructionManual.solvePartTwo(): FullyFoldedOrigami = FullyFoldedOrigami(foldPoints(instructions.size).toSheet())

    private fun InstructionManual.foldPoints(numFolds: Int): Set<Point> = points.map { it.fold(0 until numFolds, instructions) }.toSet()
    private fun Point.fold(range: IntRange, instructions: List<Point>) = range.fold(this) { acc, idx -> acc.fold(instructions[idx]) }
    private fun Point.fold(instruction: Point) = Point(x.foldAt(instruction.x), y.foldAt(instruction.y))
    private fun Int.foldAt(instruction: Int) = if (this > instruction) abs(instruction * 2 - this) else this

    private fun Set<Point>.toSheet(): String {
        val builder = StringBuilder()
        for (y in 0..maxOf(Point::y)) {
            for (x in 0..maxOf(Point::x)) {
                builder.append(if (Point(x, y) in this) MARKED_CHAR else UNMARKED_CHAR)
            }
            builder.appendLine()
        }
        return builder.substring(0, builder.length - 1)
    }
}

data class InstructionManual(val points: List<Point>, val instructions: List<Point>)
interface Origami

@JvmInline
value class SingleFoldOrigami(val value: Int) : Origami {
    override fun toString() = value.toString()
}

@JvmInline
value class FullyFoldedOrigami(val value: String) : Origami {
    override fun toString() = value
}
