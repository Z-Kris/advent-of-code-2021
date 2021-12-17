package aoc.day17

import aoc.Point
import aoc.Puzzle
import kotlin.math.abs
import kotlin.math.sign

/**
 * @author Kris | 17/12/2021
 */
object Day17 : Puzzle<TargetArea, Int>(17) {
    private val TARGET_AREA_REGEX = Regex("""target area: x=(\d+)..(\d+), y=(-\d+)..(-\d+)""")

    override fun Sequence<String>.parse(): TargetArea =
        requireNotNull(TARGET_AREA_REGEX.matchEntire(single()))
            .groupValues
            .takeLast(4)
            .map(String::toInt)
            .let { (startX, endX, startY, endY) -> TargetArea(startX, endX, startY, endY) }

    override fun TargetArea.solvePartOne(): Int = abs(minY).let { absMin -> absMin * (absMin - 1) / 2 }
    override fun TargetArea.solvePartTwo(): Int = (0..maxX).sumOf { x ->
        (minY until -minY).count { y ->
            Velocity(x, y) in this
        }
    }
}

data class TargetArea(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {
    private val xRange = minX..maxX
    private val yRange = minY..maxY

    operator fun contains(point: Point) = point.x in xRange && point.y in yRange

    operator fun contains(velocity: Velocity): Boolean {
        val xVelocitySequence = velocity.xVelocity.sequence(Int::progressXVelocity)
        val yVelocitySequence = velocity.yVelocity.sequence(Int::progressYVelocity)
        val validXPositions = xVelocitySequence.takeWhile { (pos, _) -> pos <= maxX }
        val validYPositions = yVelocitySequence.takeWhile { (pos, _) -> pos >= minY }
        return validXPositions
            .zip(validYPositions) { xPos, yPos -> Point(xPos.first, yPos.first) }
            .any { it in this }
    }

    private fun Int.sequence(velocityFunction: (pos: Int, velocity: Int) -> Pair<Int, Int>) =
        generateSequence(0 to this) { (pos, velocity) -> velocityFunction(pos, velocity) }
}

data class Velocity(val xVelocity: Int, val yVelocity: Int)

private fun Int.progressXVelocity(velocity: Int) = this + velocity to velocity - velocity.sign
private fun Int.progressYVelocity(velocity: Int) = this + velocity to velocity - 1
