package aoc.day17

import aoc.Point
import aoc.Puzzle
import aoc.greaterThanOrEqual
import aoc.lesserThanOrEqual
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

    override fun TargetArea.solvePartOne(): Int = abs(minY).let(Int::decrementingSum)
    override fun TargetArea.solvePartTwo(): Int = (0..maxX).sumOf { x ->
        (minY until -minY).count { y ->
            Velocity(x, y) in this
        }
    }
}

data class TargetArea(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {
    private operator fun contains(point: Point) = point.x in minX..maxX && point.y in minY..maxY

    operator fun contains(velocity: Velocity): Boolean {
        val validXPositions = velocity.xVelocity.flightSequence(FlightStep::progressX, maxX::greaterThanOrEqual)
        val validYPositions = velocity.yVelocity.flightSequence(FlightStep::progressY, minY::lesserThanOrEqual)
        return validXPositions.zip(validYPositions, ::toPoint).any(::contains)
    }

    private fun Int.flightSequence(stepTransformer: (FlightStep) -> FlightStep, predicate: (pos: Int) -> Boolean) =
        velocitySequence(stepTransformer).takeWhile { predicate(it.position) }

    private fun Int.velocitySequence(velocityFunction: (FlightStep) -> FlightStep) =
        generateSequence(FlightStep(0, this), velocityFunction::invoke)
}

data class Velocity(val xVelocity: Int, val yVelocity: Int)
data class FlightStep(val position: Int, val velocity: Int)

private val FlightStep.progressX get() = FlightStep(position + velocity, velocity - velocity.sign)
private val FlightStep.progressY get() = FlightStep(position + velocity, velocity - 1)
private fun toPoint(xFlightStep: FlightStep, yFlightStep: FlightStep) = Point(xFlightStep.position, yFlightStep.position)
private val Int.decrementingSum get() = this * (this - 1) / 2
