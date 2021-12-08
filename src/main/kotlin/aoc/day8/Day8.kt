package aoc.day8

import aoc.Puzzle
import aoc.concatenate
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Kris | 08/12/2021
 */
object Day8 : Puzzle<List<SegmentDisplay>, Int>(8) {
    override fun Sequence<String>.parse(): List<SegmentDisplay> {
        return toList().map { line ->
            val (numbers, codes) = line.split(" | ")
            val segment = numbers.split(' ').map { Digit(it.toSet()) }
            val digits = codes.split(' ').map { Digit(it.toSet()) }
            SegmentDisplay(Segment(segment), digits)
        }
    }

    override fun List<SegmentDisplay>.solvePartOne(): Int = sumOf { code -> with(code.segment) { code.digits.count { digits -> digits.isPartOneDigit() } } }
    override fun List<SegmentDisplay>.solvePartTwo(): Int = sumOf { code -> code.digits.map(code.segment::getDigit).concatenate().toInt() }
}

data class SegmentDisplay(val segment: Segment, val digits: List<Digit>)
data class Digit(private val digit: Set<Char>) : Set<Char> by digit

data class Segment(private val allDigits: List<Digit>) : List<Digit> by allDigits {
    private fun Digit.hasAll(vararg letters: Char) = letters.all { it in this }
    private fun Digit.hasNone(vararg letters: Char) = letters.none { it in this }
    private fun Digit.firstNot(vararg letters: Char) = first { it !in letters }
    private fun Digit.isAny(vararg options: Digit) = options.any { it == this }
    private fun Digit.onlyOneIn(other: Digit) = count { it in other } == 1
    fun Digit.isPartOneDigit() = isAny(one, four, seven, eight)

    private val a by lazy(NONE) { seven.first { it !in one } }
    private val b by lazy(NONE) { four.first { it !in three } }
    private val c by lazy(NONE) { one.first { it !in six } }
    private val d by lazy(NONE) { four.firstNot(b, c, f) }
    private val e by lazy(NONE) { two.firstNot(a, c, d, g) }
    private val f by lazy(NONE) { one.first { it != c } }
    private val g by lazy(NONE) { three.firstNot(a, c, d, f) }

    private val zero by lazySearch(size = 6) { it.hasNone(d) }
    private val one by lazySearch(size = 2)
    private val two by lazySearch(size = 5) { it.hasAll(a, c, d, g) && it.hasNone(f) }
    private val three by lazySearch(size = 5) { it.hasAll(f, c) }
    private val four by lazySearch(size = 4)
    private val five by lazySearch(size = 5) { it.hasNone(c, e) }
    private val six by lazySearch(size = 6) { it.onlyOneIn(one) }
    private val seven by lazySearch(size = 3)
    private val eight by lazySearch(size = 7)
    private val nine by lazySearch(size = 6) { it.hasNone(e) }

    private val digits by lazy { listOf(zero, one, two, three, four, five, six, seven, eight, nine) }
    fun getDigit(digits: Digit): Int = requireNotNull(this.digits.indexOf(digits))
}

private inline fun Segment.lazySearch(size: Int, crossinline predicate: (Digit) -> Boolean = { true }):
    ReadOnlyProperty<Any?, Digit> = object : ReadOnlyProperty<Any?, Digit> {
    private var value: Digit? = null
    override fun getValue(thisRef: Any?, property: KProperty<*>): Digit = value ?: first { size == it.size && predicate(it) }.also { value = it }
}
