package aoc.day14

import aoc.*

/**
 * @author Kris | 14/12/2021
 */
object Day14 : Puzzle<PolymerizationEquipment, Long>(14) {
    private val INSTRUCTION_REGEX = Regex("([A-Z]{2}) -> ([A-Z])")

    override fun Sequence<String>.parse(): PolymerizationEquipment = with(toList()) { PolymerizationEquipment(first(), drop(2).toInstructions()) }

    private fun List<String>.toInstructions() = map {
        val (string, result) = requireNotNull(INSTRUCTION_REGEX.find(it)?.destructured)
        InsertionRule(string.toCharPair(), result.single())
    }

    private operator fun List<InsertionRule>.get(pair: Pair<Char, Char>) = single { it.pair == pair }
    private fun String.toInitialPolymerCounts() = windowed(2).groupingBy(String::toCharPair).eachCount().mapValues { it.value.toLong() }

    private fun IntRange.foldPolymerCounts(initialCounts: PolymerCounts, rules: List<InsertionRule>) = fold(initialCounts) { acc, _ ->
        buildMap {
            acc.forEach { (pair, count) ->
                val rule = rules[pair]
                increment(pair.first to rule.result, count)
                increment(rule.result to pair.second, count)
            }
        }
    }

    private fun mapToCharOccurrences(counts: PolymerCounts, firstChar: Char): Map<Char, Long> = buildMap {
        put(firstChar, 1)
        counts.forEach { (pair, count) -> increment(pair.second, count) }
    }

    private fun PolymerizationEquipment.reinforce(numOfIterations: Int): Long {
        val foldedCounts = (0 until numOfIterations).foldPolymerCounts(template.toInitialPolymerCounts(), rules)
        val valueCounts = mapToCharOccurrences(foldedCounts, template.first()).values
        return valueCounts.requireMax() - valueCounts.requireMin()
    }

    override fun PolymerizationEquipment.solvePartOne(): Long = reinforce(10)
    override fun PolymerizationEquipment.solvePartTwo(): Long = reinforce(40)
}

data class PolymerizationEquipment(val template: String, val rules: List<InsertionRule>)
data class InsertionRule(val pair: Pair<Char, Char>, val result: Char)
private typealias PolymerCounts = Map<Pair<Char, Char>, Long>
