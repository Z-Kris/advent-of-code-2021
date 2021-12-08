package aoc.day8

import aoc.Puzzle
import aoc.concatenate

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
data class Digits(val digits: Set<Char>) : Set<Char> by digits
data class Segment(val numbers: List<Digits>) : List<Digits> by numbers {
    private fun Digits.hasLetters(vararg letters: Char) = letters.all { it in this }
    private fun Digits.hasNone(vararg letters: Char) = letters.none { it in this }
    private fun Digits.missing(vararg letters: Char) = first { it !in letters }
    private fun Digits.isAny(vararg options: Digits) = options.any { it == this }
    private fun Digits.onlyOneIn(other: Digits) = count { it in other } == 1
    fun Digits.isPartOneDigit() = isAny(one, four, seven, eight)

    private val a by lazy { seven.first { it !in one } }
    private val b by lazy { four.first { it !in three } }
    private val c by lazy { one.first { it !in six } }
    private val d by lazy { four.missing(b, c, f) }
    private val e by lazy { two.missing(a, c, d, g) }
    private val f by lazy { one.first { it != c } }
    private val g by lazy { three.missing(a, c, d, f) }

    private val zero by lazy { first { it.size == 6 && !it.hasLetters(d) } }
    private val one by lazy { first { it.size == 2 } }
    private val two by lazy { first { it.size == 5 && it.hasLetters(a, c, d, g) && it.hasNone(f) } }
    private val three by lazy { first { it.size == 5 && it.hasLetters(f, c) } }
    private val four by lazy { first { it.size == 4 } }
    private val five by lazy { first { it.size == 5 && it.hasNone(c, e) } }
    private val six by lazy { first { it.size == 6 && it.onlyOneIn(one) } }
    private val seven by lazy { first { it.size == 3 } }
    private val eight by lazy { first { it.size == 7 } }
    private val nine by lazy { first { it.size == 6 && !it.hasLetters(e) } }

    private val digits by lazy { listOf(zero, one, two, three, four, five, six, seven, eight, nine) }

    fun getDigit(digits: Digits): Int = requireNotNull(this.digits.indexOf(digits))
}
