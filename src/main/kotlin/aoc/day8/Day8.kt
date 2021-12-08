package aoc.day8

import aoc.Puzzle
import aoc.concatenate
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Kris | 08/12/2021
 */
object Day8 : Puzzle<List<Code>, Int>(8) {
    override fun Sequence<String>.parse(): List<Code> {
        return toList().map { line ->
            val (numbers, codes) = line.split(" | ")
            val segment = numbers.split(' ').map { Digits(it.toSet()) }
            val digits = codes.split(' ').map { Digits(it.toSet()) }
            Code(Segment(segment), digits)
        }
    }

    override fun List<Code>.solvePartOne(): Int = sumOf { code -> with(code.segment) { code.digits.count { digits -> digits.isPartOneDigit() } } }
    override fun List<Code>.solvePartTwo(): Int = sumOf { code -> code.digits.map(code.segment::getDigit).concatenate().toInt() }
}

data class Code(val segment: Segment, val digits: List<Digits>)
data class Digits(private val digits: Set<Char>) : Set<Char> by digits
data class Segment(private val numbers: List<Digits>) : List<Digits> by numbers {
    private fun Digits.hasAll(vararg letters: Char) = letters.all { it in this }
    private fun Digits.hasNone(vararg letters: Char) = letters.none { it in this }
    private fun Digits.findMissing(vararg letters: Char) = first { it !in letters }
    private fun Digits.isAny(vararg options: Digits) = options.any { it == this }
    private fun Digits.onlyOneIn(other: Digits) = count { it in other } == 1
    fun Digits.isPartOneDigit() = isAny(one, four, seven, eight)

    private val a by lazy(NONE) { seven.first { it !in one } }
    private val b by lazy(NONE) { four.first { it !in three } }
    private val c by lazy(NONE) { one.first { it !in six } }
    private val d by lazy(NONE) { four.findMissing(b, c, f) }
    private val e by lazy(NONE) { two.findMissing(a, c, d, g) }
    private val f by lazy(NONE) { one.first { it != c } }
    private val g by lazy(NONE) { three.findMissing(a, c, d, f) }

    private val zero by lazySearch(size = 6) { !it.hasAll(d) }
    private val one by lazySearch(size = 2)
    private val two by lazySearch(size = 5) { it.hasAll(a, c, d, g) && it.hasNone(f) }
    private val three by lazySearch(size = 5) { it.hasAll(f, c) }
    private val four by lazySearch(size = 4)
    private val five by lazySearch(size = 5) { it.hasNone(c, e) }
    private val six by lazySearch(size = 6) { it.onlyOneIn(one) }
    private val seven by lazySearch(size = 3)
    private val eight by lazySearch(size = 7)
    private val nine by lazySearch(size = 6) { !it.hasAll(e) }

    private val digits by lazy { listOf(zero, one, two, three, four, five, six, seven, eight, nine) }
    fun getDigit(digits: Digits): Int = requireNotNull(this.digits.indexOf(digits))
}

private inline fun Segment.lazySearch(size: Int, crossinline predicate: (Digits) -> Boolean = { true }):
    ReadOnlyProperty<Any?, Digits> = object : ReadOnlyProperty<Any?, Digits> {
    private var value: Digits? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): Digits = value ?: first { size == it.size && predicate(it) }.also { value = it }
}
