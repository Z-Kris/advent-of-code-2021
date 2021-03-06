package aoc.puzzles

/**
 * @author Kris | 05/12/2021
 */
object Day3 : Puzzle<List<Int>, Long>(3) {
    override fun List<String>.parse() = map { it.toInt(2) }

    override fun List<Int>.solvePartOne(): Long {
        val counts = getBitCounts(getHighestEnabledBitIndex())
        val gamma = counts.getCommonValue { it > size / 2 }
        val epsilon = counts.getCommonValue { it < size / 2 }
        return gamma * epsilon
    }

    private fun List<Int>.getBitCounts(highestBitIndex: Int) = IntArray(highestBitIndex + 1) { bitIndex -> getBitCount(bitIndex) }
    private fun List<Int>.getBitCount(bitIndex: Int) = count { value -> value ushr bitIndex and 0x1 == 0x1 }

    private inline fun IntArray.getCommonValue(predicate: (value: Int) -> Boolean) =
        withIndex().sumOf { (index, value) -> if (predicate(value)) (1 shl index) else 0 }.toLong()

    override fun List<Int>.solvePartTwo(): Long {
        val highestEnabledBitIndex = getHighestEnabledBitIndex()
        val oxygenRating = toMutableList().getRating(highestEnabledBitIndex) { remainingValues, value -> value >= remainingValues.size / 2.0 }
        val co2Rating = toMutableList().getRating(highestEnabledBitIndex) { remainingValues, value -> value < remainingValues.size / 2.0 }
        return oxygenRating * co2Rating
    }

    private fun List<Int>.getHighestEnabledBitIndex(): Int {
        val highestEnabledBitValue = maxOfOrNull(Int::takeHighestOneBit) ?: error("Highest one bit does not exist.")
        return Int.SIZE_BITS - highestEnabledBitValue.countLeadingZeroBits() - 1
    }

    private tailrec fun MutableList<Int>.getRating(bitIndex: Int, predicate: (remainingValues: List<Int>, value: Int) -> Boolean): Long {
        val bitValue = if (predicate(this, getBitCount(bitIndex))) 1 else 0
        removeAll { it ushr bitIndex and 0x1 != bitValue }
        require(this.isNotEmpty()) { "No valid values remaining." }
        require(bitIndex >= 0) { "Out of bounds" }
        return if (size == 1) first().toLong() else getRating(bitIndex - 1, predicate)
    }
}
