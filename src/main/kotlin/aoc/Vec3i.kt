package aoc

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KProperty1

/**
 * @author Kris | 19/12/2021
 */
data class Vec3i(val x: Int, val y: Int, val z: Int) {

    fun copy(property: KProperty1<Vec3i, Int>, value: Int): Vec3i = when (property) {
        Vec3i::x -> copy(x = value)
        Vec3i::y -> copy(y = value)
        Vec3i::z -> copy(z = value)
        else -> error("Invalid property: $property")
    }

    fun maxVec3i(other: Vec3i) = copy(x = max(x, other.x), y = max(y, other.y), z = max(z, other.z))
    fun minVec3i(other: Vec3i) = copy(x = min(x, other.x), y = min(y, other.y), z = min(z, other.z))

    operator fun minus(other: Vec3i) = Vec3i(x - other.x, y - other.y, z - other.z)

    fun rotate(rotation: Int): Vec3i = when (rotation) {
        0 -> this
        1 -> Vec3i(-y, x, z)
        2 -> Vec3i(-x, -y, z)
        3 -> Vec3i(y, -x, z)
        else -> error("Invalid rotation: $rotation")
    }

    fun orientate(orientation: Int): Vec3i = when (orientation) {
        0 -> this
        1 -> Vec3i(x, -y, -z)
        2 -> Vec3i(x, -z, y)
        3 -> Vec3i(-y, -z, x)
        4 -> Vec3i(-x, -z, -y)
        5 -> Vec3i(y, -z, -x)
        else -> error("Invalid orientation: $orientation")
    }

    fun manhattanDistance(other: Vec3i): Int = abs(other.x - x) + abs(other.y - y) + abs(other.z - z)
    fun squaredDifferenceOfDistance(other: Vec3i): Int {
        val diffX = other.x - x
        val diffY = other.y - y
        val diffZ = other.z - z
        return diffX * diffX + diffY * diffY + diffZ * diffZ
    }
}
