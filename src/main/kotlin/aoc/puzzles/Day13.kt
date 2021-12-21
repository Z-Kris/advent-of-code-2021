package aoc.puzzles

import aoc.Point
import kotlin.math.abs

/**
 * @author Kris | 13/12/2021
 */
object Day13 : Puzzle<InstructionManual, Origami>(13) {
    private const val MARKED_CHAR = '█'
    private const val UNMARKED_CHAR = ' '
    private val POINT_REGEX = Regex("""(\d+),(\d+)""")
    private val INSTRUCTION_REGEX = Regex("""fold along ([xy])=(\d+)""")

    override fun List<String>.parse(): InstructionManual =
        let { lines -> InstructionManual(buildList { lines.forEach { addPoint(it) } }, buildList { lines.forEach { addInstruction(it) } }) }

    private fun MutableList<Point>.addPoint(line: String) {
        val (x, y) = POINT_REGEX.find(line)?.destructured ?: return
        this += Point(x, y)
    }

    private fun MutableList<Point>.addInstruction(line: String) {
        val (type, count) = INSTRUCTION_REGEX.find(line)?.destructured ?: return
        this += when (type.single()) {
            'x' -> Point(count, 0)
            'y' -> Point(0, count)
            else -> error("Invalid coord: $type")
        }
    }

    override fun InstructionManual.solvePartOne(): SingleFoldOrigami = SingleFoldOrigami(foldPoints(1).count())
    override fun InstructionManual.solvePartTwo(): FullyFoldedOrigami = FullyFoldedOrigami(foldPoints(instructions.size).toSheet())

    private fun InstructionManual.foldPoints(numFolds: Int): Set<Point> = points.map { it.fold(numFolds, instructions) }.toSet()
    private fun Point.fold(numFolds: Int, instructions: List<Point>) = (0 until numFolds).fold(this) { acc, idx -> acc.fold(instructions[idx]) }
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
