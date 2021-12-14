package aoc.day14

import aoc.Puzzle
import aoc.increment
import aoc.toCharPair

/**
 * @author Kris | 14/12/2021
 */
@OptIn(ExperimentalStdlibApi::class)
object Day14 : Puzzle<PolymerizationEquipment, Long>(14) {
    private val INSTRUCTION_REGEX = Regex("([A-Z]{2}) -> ([A-Z])")
    private val TEMPLATE_REGEX = Regex("([A-Z]+)")

    override fun Sequence<String>.parse(): PolymerizationEquipment {
        val list = toList()
        val (template) = TEMPLATE_REGEX.find(list.first())?.destructured ?: error("Invalid format.")
        val instructions = mutableListOf<InsertionRule>()
        list.drop(1).forEach { line ->
            val (string, result) = INSTRUCTION_REGEX.find(line)?.destructured ?: return@forEach
            instructions += InsertionRule(string.toCharPair(), result.single())
        }
        return PolymerizationEquipment(template, instructions)
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
        return requireNotNull(valueCounts.maxOrNull()) - requireNotNull(valueCounts.minOrNull())
    }

    override fun PolymerizationEquipment.solvePartOne(): Long = reinforce(10)
    override fun PolymerizationEquipment.solvePartTwo(): Long = reinforce(40)
}

data class PolymerizationEquipment(val template: String, val rules: List<InsertionRule>)
data class InsertionRule(val pair: Pair<Char, Char>, val result: Char)
private typealias PolymerCounts = Map<Pair<Char, Char>, Long>
