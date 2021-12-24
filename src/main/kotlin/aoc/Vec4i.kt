package aoc

import kotlin.reflect.KProperty1

/**
 * @author Kris | 24/12/2021
 */
data class Vec4i(val x: Int, val y: Int, val z: Int, val w: Int) {
    fun copy(property: KProperty1<Vec4i, Int>, value: Int): Vec4i = when (property) {
        Vec4i::x -> copy(x = value)
        Vec4i::y -> copy(y = value)
        Vec4i::z -> copy(z = value)
        Vec4i::w -> copy(w = value)
        else -> error("Invalid property: $property")
    }

    companion object {
        val ZERO = Vec4i(x = 0, y = 0, z = 0, w = 0)
    }
}
