package aoc.puzzles

import aoc.Vec3i
import aoc.component6
import aoc.inv
import kotlin.reflect.KProperty1

/**
 * @author Kris | 22/12/2021
 */
object Day22 : Puzzle<RebootSteps, Long>(22) {
    private val PART_ONE_RANGE = -50..51
    private val INSTRUCTION_REGEX = Regex("""(on|off) x=(-?\d+)\.\.(-?\d+),y=(-?\d+)\.\.(-?\d+),z=(-?\d+)\.\.(-?\d+)""")

    override fun List<String>.parse(): RebootSteps {
        val entries = map { line ->
            val match = INSTRUCTION_REGEX.matchEntire(line) ?: error("Corrupt line: $line")
            val values = match.groupValues.drop(1)
            val on = values.first() == "on"
            val (x1, x2, y1, y2, z1, z2) = values.drop(1).map(String::toInt)
            val start = Vec3i(x1, y1, z1)
            val end = Vec3i(x2 + 1, y2 + 1, z2 + 1)
            RebootStep(on, Cuboid(start, end))
        }
        return RebootSteps(entries)
    }

    private fun Vec3i.isPartOne() = x in PART_ONE_RANGE && y in PART_ONE_RANGE && z in PART_ONE_RANGE
    private fun RebootSteps.filterToPartOne() =
        filter { it.cuboid.start.isPartOne() && it.cuboid.end.isPartOne() }
            .let(::RebootSteps)

    override fun RebootSteps.solvePartOne(): Long = filterToPartOne().remainingCubesSize()
    override fun RebootSteps.solvePartTwo(): Long = remainingCubesSize()

    private fun RebootSteps.remainingCubesSize(): Long = intersectCubes().sumOf(Cuboid::size)

    private fun RebootSteps.intersectCubes() = fold(emptyList<Cuboid>()) { acc, rebootStep ->
        val (on, cuboid) = rebootStep
        val next = acc.flatMap { it - cuboid }
        if (on) next + cuboid else next
    }
}

data class RebootSteps(private val steps: List<RebootStep>) : List<RebootStep> by steps
data class RebootStep(val on: Boolean, val cuboid: Cuboid)
private typealias Vec3iProperty = KProperty1<Vec3i, Int>

data class Cuboid(val start: Vec3i, val end: Vec3i) {
    private val isEmpty get() = start.x > end.x || start.y > end.y || start.z > end.z
    val size get() = (end.x.toLong() - start.x) * (end.y - start.y) * (end.z - start.z)
    private inline fun Vec3i.slice(property: Vec3iProperty, other: Vec3i, selector: (Int, Int) -> Int) =
        copy(property, selector(property.get(other), property.get(this)))

    private fun slice(property: Vec3iProperty, other: Vec3i): Pair<Cuboid, Cuboid> {
        val slice = Cuboid(start, end.slice(property, other, Math::min))
        val remaining = Cuboid(start.slice(property, other, Math::max), end)
        return slice to remaining
    }

    operator fun minus(other: Cuboid): List<Cuboid> = if (!intersects(other)) listOf(this) else slice(other, true)

    private fun slice(other: Cuboid, start: Boolean): List<Cuboid> {
        val (remaining, results) = VEC3I_PROPERTIES.fold(this to emptyList<Cuboid>()) { acc, property ->
            val (cuboid, results) = acc
            val (slice, remaining) = when {
                start -> cuboid.slice(property, other.start)
                else -> cuboid.slice(property, other.end).inv
            }
            remaining to (if (slice.isEmpty) results else (results + slice))
        }
        return if (start) (results + remaining.slice(other, false)) else results
    }

    private fun intersects(other: Cuboid): Boolean = !intersect(other).isEmpty
    private fun intersect(other: Cuboid): Cuboid = Cuboid(start.maxVec3i(other.start), end.minVec3i(other.end))

    companion object {
        private val VEC3I_PROPERTIES = listOf(Vec3i::x, Vec3i::y, Vec3i::z)
    }
}
